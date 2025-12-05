package com.food.recipe.api;

import com.food.recipe.api.entity.RecipeEntity;
import com.food.recipe.api.repository.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RecipeDeleteIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @AfterEach
    void setup() {
        recipeRepository.deleteAll();
    }

    @Test
    void shouldDeleteRecipeSuccessfully() throws Exception {
        var id = createRecipe("Fungi Pizza", true);

        mockMvc.perform(delete("/api/v1/recipes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        boolean isIdPresent = isIdPresent(id);

        assertFalse(isIdPresent);
    }

    @Test
    void shouldReturnBadRequestWhenIdIsInvalid() throws Exception {

        mockMvc.perform(delete("/api/v1/recipes/abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenIdIsNull() throws Exception {

        mockMvc.perform(delete("/api/v1/recipes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnNotFoundWhenIdNotExistInDB() throws Exception {

        createRecipe("Fungi Pizza", true);

        mockMvc.perform(delete("/api/v1/recipes/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturn200Then404WhenDeletingSameRecipeTwice() throws Exception {

        var id = createRecipe("Fungi Pizza", true);

        mockMvc.perform(delete("/api/v1/recipes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        boolean isIdExist = isIdPresent(id);

        assertFalse(isIdExist);

        mockMvc.perform(delete("/api/v1/recipes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        boolean isIdExistSecondTime = isIdPresent(id);

        assertFalse(isIdExistSecondTime);

    }

    @Test
    void shouldDeleteOnlyOneRecipeAndKeepOthers() throws Exception {

        var firstId = createRecipe("Pizza", true);
        var secondId = createRecipe("Chicken Pizza", false);
        var thirdId = createRecipe("Salami Pizza", false);

        mockMvc.perform(delete("/api/v1/recipes/{id}", secondId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        assertFalse(isIdPresent(secondId));
        assertTrue(isIdPresent(firstId));
        assertTrue(isIdPresent(thirdId));
    }

    @Test
    void shouldHandleConcurrentDeletes() {

        var id = createRecipe("Pasta", true);
        int threads = 2;
        var executor = Executors.newFixedThreadPool(threads);

        var futures = IntStream.range(0, threads)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                    try {
                        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/recipes/{id}", id)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON_VALUE))
                                .andReturn();
                        return mvcResult.getResponse().getStatus();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }, executor))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        var statuses = futures.stream().map(CompletableFuture::join).toList();

        long okCount = statuses.stream().filter(s -> s == HttpStatus.OK.value()).count();
        long notFoundCount = statuses.stream().filter(s -> s == HttpStatus.NOT_FOUND.value()).count();

        assertEquals(1, okCount, "Exactly one thread should delete successfully");
        assertEquals(threads - 1, notFoundCount, "Remaining threads should get 404");

        assertEquals(0, recipeRepository.count(), "Recipe must be deleted");
    }

    private Integer createRecipe(String name, boolean isVegetarian) {
        RecipeEntity entity = new RecipeEntity();
        entity.setName(name);
        entity.setIsVegetarian(isVegetarian);
        entity.setIngredients(List.of("Salt", "Pepper"));
        entity.setInstructions("Mix and cook.");
        entity.setServings(2);
        return recipeRepository.save(entity).getId();
    }

    private boolean isIdPresent(Integer secondId) {
        return recipeRepository.existsById(secondId);
    }
}
