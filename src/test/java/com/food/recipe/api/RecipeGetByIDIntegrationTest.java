package com.food.recipe.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.recipe.api.entity.RecipeEntity;
import com.food.recipe.api.repository.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.food.recipe.api.util.RecipeTestUtil.createRecipeEntity;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RecipeGetByIDIntegrationTest extends AbstractIntegrationTest {

    public static final String GET_PATH = "/api/v1/recipes/{id}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RecipeRepository recipeRepository;

    @AfterEach
    void setup() {
        recipeRepository.deleteAll();
    }

    @Test
    void shouldGetRecipeByIdSuccessfully() throws Exception {
        var savedRecipeEntity = saveRecipeEntity();

        performGetRecipeByIdRequest(savedRecipeEntity.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedRecipeEntity.getId()))
                .andExpect(jsonPath("$.name").value(savedRecipeEntity.getName()))
                .andExpect(jsonPath("$.servings").value(savedRecipeEntity.getServings()))
                .andExpect(jsonPath("$.instructions").value(savedRecipeEntity.getInstructions()))
                .andExpect(jsonPath("$.isVegetarian").value(true))
                .andExpect(jsonPath("$.ingredients", hasSize(savedRecipeEntity.getIngredients().size())));
    }

    @Test
    void shouldReturnNotFound_whenGivenIdIsNotPresentInDB() throws Exception {
        saveRecipeEntity();

        performGetRecipeByIdRequest(999)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("Record not found")));
    }

    @Test
    void shouldReturn400_whenGivenIdIsInvalidType() throws Exception {
        saveRecipeEntity();

        mockMvc.perform(get("/api/v1/recipes/abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400_whenGivenIdIsNegativeOrZero() throws Exception {
        saveRecipeEntity();

        mockMvc.perform(get("/api/v1/recipes/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnCorrectContentType() throws Exception {
        var savedRecipeEntity = saveRecipeEntity();

        mockMvc.perform(get(GET_PATH, savedRecipeEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldReturnBadRequestWhenIdIsNull() throws Exception {

        mockMvc.perform(get("/api/v1/recipes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError());
    }


    private RecipeEntity saveRecipeEntity() {
        var entity = createRecipeEntity();
        return recipeRepository.save(entity);
    }

    private ResultActions performGetRecipeByIdRequest(int id) throws Exception {
        return mockMvc.perform(get(GET_PATH, id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }
}
