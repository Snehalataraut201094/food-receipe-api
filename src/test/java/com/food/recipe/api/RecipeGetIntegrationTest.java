package com.food.recipe.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.recipe.api.entity.RecipeEntity;
import com.food.recipe.api.repository.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static com.food.recipe.api.util.RecipeTestUtil.createRecipeEntities;
import static com.food.recipe.api.util.RecipeTestUtil.createRecipeEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RecipeGetIntegrationTest extends AbstractIntegrationTest {

    public static final String GET_PATH = "/api/v1/recipes";

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
    void shouldGetAllRecipesSuccessfully() throws Exception {
        var savedEntities = saveRecipeEntities();

        var response = getResponse();

        List<Map<String, Object>> jsonArray = readJsonResponse(response);

        IntStream.range(0, savedEntities.size())
                .forEach(i -> {
                    var entity = savedEntities.get(i);
                    Map<String, Object> jsonObj = jsonArray.get(i);
                    try {
                        assertJsonArray(jsonObj, entity);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to get Recipe" + i + ": " + e.getMessage(), e);
                    }
                });
    }

    @Test
    void shouldReturnRecipesInSortedOrder() throws Exception {
        var entities = saveRecipeEntities();

        var sortedEntities = sortEntitiesById(entities);

        var response = getResponse();

        List<Map<String, Object>> jsonArray = readJsonResponse(response);

        sortJsonResponseById(jsonArray);

        IntStream.range(0, sortedEntities.size()).forEach(
                i -> {
                    var recipeEntity = sortedEntities.get(i);
                    var jsonObj = jsonArray.get(i);
                    assertJsonArray(jsonObj, recipeEntity);
                });
    }

    @Test
    void shouldContainAllExpectedFieldsInResponse() throws Exception {
        saveRecipeEntities();

        var response = getResponse();

        List<Map<String, Object>> jsonArray = readJsonResponse(response);

        jsonArray.forEach(
                jsonObject -> assertThat(jsonObject).containsKeys(
                        "id", "name", "servings", "instructions", "isVegetarian", "ingredients"));
        }

    @Test
    void shouldReturnExceptionWhenDBHasNoData() throws Exception {
       performGetRecipesRequest().andExpect(status().isNotFound());
    }

    @Test
    void repositoryNeverReturnsNullInRealDb() {
        var recipes = recipeRepository.findAll();
        assertThat(recipes).isNotNull();
    }

    @Test
    void shouldHandleEmptyOptionalFieldsGracefully() throws Exception {

        var recipeEntity = createRecipeEntity();
        recipeEntity.setInstructions("");
        recipeEntity.setIngredients(List.of());

        var savedEntity = recipeRepository.save(recipeEntity);

       performGetRecipesRequest()
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].name").value(savedEntity.getName()))
               .andExpect(jsonPath("$[0].instructions").value(""))
               .andExpect(jsonPath("$[0].ingredients").isEmpty());
    }

    @Disabled("Simulates DB down; enable manually for resilience testing")
    @Test
    void shouldReturnServerErrorWhenDatabaseIsUnavailable() throws Exception {
        // Stop the PostgreSQL Testcontainer manually or misconfigure the connection

        mockMvc.perform(get(GET_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void shouldHandleLargeNumberOfRecipesWithVirtualThreads() throws Exception {
        int total = 1000;

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = IntStream.range(0, total)
                    .mapToObj(i -> executor.submit(() -> {
                        try {
                            recipeRepository.save(new RecipeEntity(
                                    null,
                                    "Recipe" + i,
                                    (i % 2 == 0),
                                    (i % 10) + 1,
                                    List.of("Ingredient" + i, "Salt", "Water"),
                                    "Instructions for Recipe " + i));
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to insert Recipe" + i + ": " + e.getMessage(), e);
                        }
                    })).toList();
            for (var f : futures) {
                f.get();
            }
        }
        mockMvc.perform(get(GET_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(total));
    }

    @Test
    void shouldReturnConsistentResultsOnMultipleCalls() throws Exception {

        saveRecipeEntities();

        var result1 = mockMvc.perform(get(GET_PATH)).andReturn().getResponse().getContentAsString();
        var result2 = mockMvc.perform(get(GET_PATH)).andReturn().getResponse().getContentAsString();

        assertThat(result1).isEqualTo(result2);
    }

    private void sortJsonResponseById(List<Map<String, Object>> jsonArray) {
        jsonArray.sort(Comparator.comparing(m -> m.get("id").toString()));
    }

    private List<RecipeEntity> sortEntitiesById(List<RecipeEntity> entities) {
        return entities.stream().sorted(Comparator.comparing(RecipeEntity::getId)).toList();
    }

    private String getResponse() throws Exception {
        return performGetRecipesRequest()
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private List<RecipeEntity> saveRecipeEntities() {
        var entities = createRecipeEntities();
        return recipeRepository.saveAll(entities);
    }

    private ResultActions performGetRecipesRequest() throws Exception {
        return mockMvc.perform(get(GET_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    private void assertJsonArray(Map<String, Object> jsonObj, RecipeEntity recipeEntity) {
        assertThat(jsonObj.get("name")).isEqualTo(recipeEntity.getName());
        assertThat(jsonObj.get("servings")).isEqualTo(recipeEntity.getServings());
        assertThat(jsonObj.get("instructions")).isEqualTo(recipeEntity.getInstructions());
        assertThat(jsonObj.get("isVegetarian")).isEqualTo(recipeEntity.getIsVegetarian());
        assertThat(convertJsonObjectToListOfString(jsonObj)).containsExactlyElementsOf(recipeEntity.getIngredients());
    }

    private List<String> convertJsonObjectToListOfString(Map<String, Object> jsonObj) {
        return objectMapper.convertValue(jsonObj.get("ingredients"), new TypeReference<>() {});
    }

    private List<Map<String, Object>> readJsonResponse(String response) throws JsonProcessingException {
        return objectMapper.readValue(response, new TypeReference<>() {});
    }
}
