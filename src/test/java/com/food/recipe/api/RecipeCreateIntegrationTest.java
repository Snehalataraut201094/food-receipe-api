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
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.food.recipe.api.util.RecipeTestUtil.buildInstructions;
import static com.food.recipe.api.util.RecipeTestUtil.buildListOfIngredients;
import static com.food.recipe.api.util.RecipeTestUtil.createRecipeRequestDto;
import static com.food.recipe.api.util.RecipeTestUtil.createRecipeRequestDtoForUpdate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RecipeCreateIntegrationTest extends AbstractIntegrationTest {

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
    void shouldCreateRecipeSuccessfully() throws Exception {
        var request = createRecipeRequestDto();

        postRecipe(getContent(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(request.getRecipeName()))
                .andExpect(jsonPath("$.servings").value(request.getServings()))
                .andExpect(jsonPath("$.instructions").value(request.getInstructions()))
                .andExpect(jsonPath("$.isVegetarian").value(request.getIsVegetarian()));
    }
    @Test
    void shouldReturnBadRequestWhenRequestBodyIsNull() throws Exception {

        postRecipe("").andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("invalidRecipeRequest")
    void shouldFailValidationWhenFieldsAreMissing(RecipeRequestDto invalidRecipeRequests) throws Exception {

        postRecipe(getContent(invalidRecipeRequests)).andExpect(status().isBadRequest());
    }
    @Test
    void shouldHandleLargeInstructions() throws Exception {
        String instructions = """
                Boil Potato then smash it.
                Add spices and chilly into pan roast it then add potato and mix it well.
                At last heat the oil and add made potato sabji into oil and fry it.""";

        var request = createRecipeRequestDto();
        request.setInstructions(instructions);

        postRecipe(getContent(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(request.getRecipeName()))
                .andExpect(jsonPath("$.servings").value(request.getServings()))
                .andExpect(jsonPath("$.instructions").value(request.getInstructions()));
    }

    @Test
    void shouldHandleUnicodeCharacters() throws Exception {
        var request = new RecipeRequestDto("🍜 Ramen",
                false, 1, List.of("Chicken", "Noodles"), "Delicious 東京 style 🍜");

        postRecipe(getContent(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("🍜 Ramen"));
    }

    @Test
    void shouldHandleConcurrentRequests() throws Exception {
        int threads = 5;
        var executor = Executors.newFixedThreadPool(5);

        var futures = IntStream.range(0, threads)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        postRecipe(getContent(
                                createRecipeRequestDtoForUpdate("🍜 Ramen " + i,
                                        false, i + 1,
                                        List.of("Chicken", "Noodles"),
                                        "Delicious 東京 style")
                        )).andExpect(status().isCreated());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }, executor))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        assertEquals(threads, recipeRepository.count());
    }

    @Test
    void shouldFailWhenDatabaseViolatesConstraint() throws Exception {

        var existing = createRecipe();

        var duplicate = createRecipeRequestDtoForUpdate("Fungi Pizza", false, 1,
                List.of("Cheese", "Dough", "Chicken"), "Add toppings, chicken and bake pizza.");

        postRecipe(getContent(duplicate)).andExpect(status().is4xxClientError());
    }

    private String getContent(RecipeRequestDto request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
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

    private Integer createRecipe() {
        RecipeEntity entity = new RecipeEntity();
        entity.setName("Fungi Pizza");
        entity.setIsVegetarian(true);
        entity.setIngredients(List.of("Salt", "Pepper"));
        entity.setInstructions("Mix and cook.");
        entity.setServings(2);
        return recipeRepository.save(entity).getId();
    }

    private ResultActions postRecipe(String request) throws Exception {
        return mockMvc.perform(post("/api/v1/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .accept(MediaType.APPLICATION_JSON_VALUE));
    }
}
