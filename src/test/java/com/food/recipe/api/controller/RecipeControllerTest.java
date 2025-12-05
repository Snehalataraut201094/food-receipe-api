package com.food.recipe.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.recipe.api.model.RecipeRequestDto;
import com.food.recipe.api.process.RecipeProcess;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;
import java.util.stream.Stream;

import static com.food.recipe.api.util.RecipeTestUtil.buildInstructions;
import static com.food.recipe.api.util.RecipeTestUtil.buildListOfIngredients;
import static com.food.recipe.api.util.RecipeTestUtil.createRecipeRequestDto;
import static com.food.recipe.api.util.RecipeTestUtil.createRecipeResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RecipeProcess recipeProcess;

    @InjectMocks
    private RecipeController recipeController;

    @Test
    void testCreateRecipe() throws Exception {

        RecipeRequestDto request = createRecipeRequestDto();

        when(recipeProcess.createRecipe(any())).thenReturn(createRecipeResponse());

        ResultActions response = mockMvc.perform(post("/api/v1/recipes")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getContent(request))
                .accept(MediaType.APPLICATION_JSON_VALUE));

        assertSuccessResponse(status().isCreated(), response);
    }

    @ParameterizedTest
    @MethodSource("invalidRecipeRequest")
    void testMissingFieldsInRequest_shouldFailValidation(RecipeRequestDto invalidRecipeRequests) throws Exception {

        mockMvc.perform(post("/api/v1/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContent(invalidRecipeRequests)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateRecipe() throws Exception {

        RecipeRequestDto request = createRecipeRequestDto();

        when(recipeProcess.updateRecipe(anyInt(), any())).thenReturn(createRecipeResponse());

        ResultActions response = mockMvc.perform(put("/api/v1/recipes/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(getContent(request))
                .accept(MediaType.APPLICATION_JSON_VALUE));

        assertSuccessResponse(status().isOk(), response);
    }

    @ParameterizedTest
    @MethodSource("invalidRecipeRequest")
    void testUpdateRecipe_whenMissingFieldsInRequest_returnBadRequestException(
            RecipeRequestDto invalidRecipeRequests) throws Exception {

        mockMvc.perform(put("/api/v1/recipes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContent(invalidRecipeRequests)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteRecipe() throws Exception {

        when(recipeProcess.deleteRecipe(anyInt())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/recipes/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllRecipes() throws Exception {

        when(recipeProcess.getAllRecipes()).thenReturn(List.of(createRecipeResponse()));

        ResultActions response = mockMvc.perform(get("/api/v1/recipes")
                .accept(MediaType.APPLICATION_JSON_VALUE));

        assertListOfSuccessResponse(response);
    }

    @Test
    void testGetRecipesById() throws Exception {

        when(recipeProcess.getRecipeById(anyInt())).thenReturn(createRecipeResponse());

        ResultActions response = mockMvc.perform(get("/api/v1/recipes/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE));

        assertSuccessResponse(status().isOk(), response);
    }

    @Test
    void testSearchRecipes() throws Exception {

        when(recipeProcess.searchRecipes(any(), anyInt(), any(), any(), any()))
                .thenReturn(List.of(createRecipeResponse()));

        ResultActions response = mockMvc.perform(get("/api/v1/recipes/search")
                .param("isVegetarian", "true")
                .param("servings", "4")
                .param("includeIngredients", "Potato, Spices")
                .param("excludeIngredients", "bake")
                .param("instructionText", "Boil")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE));

        assertListOfSuccessResponse(response);
    }

    @Test
    void testSearchRecipes_whenFilterParaNotFound_returnNotFoundResponse() throws Exception {

        when(recipeProcess.searchRecipes(any(), anyInt(), any(), any(), any()))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/recipes/search")
                        .param("isVegetarian", "true")
                        .param("servings", "2")
                        .param("includeIngredients", "Onion, Tomato")
                        .param("excludeIngredients", "bake")
                        .param("instructionText", "Boil")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    private void assertSuccessResponse(ResultMatcher status, ResultActions response) throws Exception {
        response
                .andExpect(status)
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Vada Pav"))
                .andExpect(jsonPath("$.isVegetarian").value(true))
                .andExpect(jsonPath("$.ingredients[0]").value("Chutney"))
                .andExpect(jsonPath("$.ingredients[1]").value("Potato"))
                .andExpect(jsonPath("$.instructions").value(buildInstructions()));
    }

    private void assertListOfSuccessResponse(ResultActions response) throws Exception {
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Vada Pav"))
                .andExpect(jsonPath("$[0].isVegetarian").value(true))
                .andExpect(jsonPath("$[0].ingredients[0]").value("Chutney"))
                .andExpect(jsonPath("$[0].ingredients[1]").value("Potato"))
                .andExpect(jsonPath("$[0].instructions").value(buildInstructions()));
    }

    public static Stream<RecipeRequestDto> invalidRecipeRequest() {
        List<String> ingredients = buildListOfIngredients();
        String instructions = buildInstructions();

        RecipeRequestDto missingName = RecipeRequestDto.builder()
                .isVegetarian(true)
                .instructions(instructions)
                .ingredients(ingredients)
                .servings(4)
                .build();

        RecipeRequestDto missingIngredients = RecipeRequestDto.builder()
                .recipeName("Pasta")
                .servings(4)
                .isVegetarian(true)
                .instructions(instructions)
                .build();

        RecipeRequestDto missingInstructions = RecipeRequestDto.builder()
                .recipeName("Pasta")
                .servings(4)
                .isVegetarian(true)
                .ingredients(ingredients)
                .build();

        RecipeRequestDto missingServings = RecipeRequestDto.builder()
                .recipeName("Pasta")
                .isVegetarian(true)
                .ingredients(ingredients)
                .instructions(instructions)
                .build();

        return Stream.of(missingName, missingIngredients, missingInstructions, missingServings);
    }

    private String getContent(RecipeRequestDto request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }
}
