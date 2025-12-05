package com.food.recipe.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.recipe.api.entity.RecipeEntity;
import com.food.recipe.api.model.RecipeRequestDto;
import com.food.recipe.api.repository.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.food.recipe.api.util.RecipeTestUtil.buildInstructions;
import static com.food.recipe.api.util.RecipeTestUtil.buildListOfIngredients;
import static com.food.recipe.api.util.RecipeTestUtil.createRecipeRequestDto;
import static com.food.recipe.api.util.RecipeTestUtil.createRecipeRequestDtoForUpdate;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RecipeUpdateIntegrationTest extends AbstractIntegrationTest {

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
    void shouldUpdateRecipeSuccessfully() throws Exception {
        var savedRecipeId = createRecipe("Vada Pav", true);

        var toUpdateRequest = createRecipeRequestDtoForUpdate("Pasta", true, 2,
                List.of("Pasta", "Olive Oil", "Toppings"), "Boil pasta and fry it into pan and add toppings.");

        putRecipeWithId(getContent(toUpdateRequest), savedRecipeId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedRecipeId))
                .andExpect(jsonPath("$.name").value(toUpdateRequest.getRecipeName()))
                .andExpect(jsonPath("$.servings").value(toUpdateRequest.getServings()))
                .andExpect(jsonPath("$.instructions").value(toUpdateRequest.getInstructions()));
    }

    @ParameterizedTest
    @MethodSource("invalidRecipeRequest")
    void shouldFailValidation_whenMissingFieldsInRequest(
            RecipeRequestDto invalidRecipeRequests) throws Exception {

        putRecipe("/api/v1/recipes/1", getContent(invalidRecipeRequests)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_whenRequestBodyIsNull() throws Exception {

        putRecipe("/api/v1/recipes/1", "null").andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_whenIdIsInvalid() throws Exception {

        putRecipe("/api/v1/recipes/abc", getContent(createRecipeRequestDto())).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFound_whenIdIsNotPresentInDB() throws Exception {

        var id = createRecipe("Pizza", true);

        putRecipe("/api/v1/recipes/9", getContent(createRecipeRequestDto())).andExpect(status().isNotFound());
    }

    @Test
    void shouldFail_whenDatabaseViolatesConstraint() throws Exception {
        var firstSavedId = createRecipe("Pizza", true);
        var secondSavedId = createRecipe("Pasta", true);

        var updateDto = createRecipeRequestDtoForUpdate(
                "Pizza", false, 2, List.of("Tomato", "Cheese"), "Bake it.");

        putRecipeWithId(getContent(updateDto), secondSavedId).andExpect(status().is5xxServerError());
    }

    @Test
    void shouldUpdateRecipeWithUnicodeCharacters() throws Exception {
        var id = createRecipe("Chicken Soup", false);

        var updateDto = createRecipeRequestDtoForUpdate("🍜 Ramen 東京", true,
                1, List.of("Noodles", "Chicken"), "Delicious 東京 style with 🍥 fish cake");

        putRecipeWithId(getContent(updateDto), id)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateDto.getRecipeName()))
                .andExpect(jsonPath("$.instructions").value(updateDto.getInstructions()));
    }

    @Test
    void shouldUpdateWithTooLongInstructions() throws Exception {
        var id = createRecipe(" Veg Burger", true);

        var longInstructions = "Cook".repeat(5000);

        var updateDto = createRecipeRequestDtoForUpdate("Burger Deluxe", false,
                1, List.of("Bun", "Meat"), longInstructions);

        putRecipeWithId(getContent(updateDto), id)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateDto.getRecipeName()))
                .andExpect(jsonPath("$.instructions").value(updateDto.getInstructions()));
    }

    @Test
    void shouldHandleConcurrentUpdates() throws Exception {
        var id = createRecipe("Stew", true);
        int threads = 5;
        var executor = Executors.newFixedThreadPool(threads);

        try {
            var futures = IntStream.range(0, threads)
                    .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                        try {
                            return putRecipeWithId(getContent(createRecipeRequestDtoForUpdate(
                                    "Stew v" + i,
                                    i % 2 == 0,
                                    2,
                                    List.of("Ingredient" + i),
                                    "Instruction " + i
                            )), id);
                        } catch (Exception e) {
                            throw new CompletionException(e);
                        }
                    }, executor)).toList();

            // Wait for all futures to complete and assert status
            futures.forEach(cf -> {
                try {
                    cf.join().andExpect(status().isOk());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            RecipeEntity finalEntity = recipeRepository.findById(id).orElseThrow();
            assertTrue(finalEntity.getName().startsWith("Stew v"));

        } finally {
            executor.shutdown();
        }
    }

    private static Stream<RecipeRequestDto> invalidRecipeRequest() {
        List<String> ingredients = buildListOfIngredients();
        String instructions = buildInstructions();

        var missingName = RecipeRequestDto.builder()
                .isVegetarian(true)
                .ingredients(ingredients)
                .instructions(instructions)
                .servings(4).build();

        var missingIngredients = RecipeRequestDto.builder()
                .recipeName("Pasta")
                .isVegetarian(true)
                .instructions(instructions)
                .servings(4).build();

        var missingInstructions = RecipeRequestDto.builder()
                .recipeName("Pasta")
                .isVegetarian(true)
                .ingredients(ingredients)
                .servings(4).build();

        var missingServings = RecipeRequestDto.builder()
                .recipeName("Pasta")
                .isVegetarian(true)
                .ingredients(ingredients)
                .instructions(instructions).build();

        var emptyIngredients = RecipeRequestDto.builder()
                .recipeName("Pasta")
                .isVegetarian(true)
                .ingredients(List.of())
                .servings(4)
                .instructions(instructions).build();

        return Stream.of(missingName, missingIngredients, missingInstructions, missingServings, emptyIngredients);
    }

    private ResultActions putRecipeWithId(String request, int id) throws Exception {
        return mockMvc.perform(put("/api/v1/recipes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .accept(MediaType.APPLICATION_JSON_VALUE));
    }

    private ResultActions putRecipe(String url, String request) throws Exception {
        return mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request));
    }

    private String getContent(RecipeRequestDto request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
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
}
