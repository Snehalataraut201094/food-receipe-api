package com.food.recipe.api.process.impl;

import com.food.recipe.api.entity.RecipeEntity;
import com.food.recipe.api.exception.EntityNotFoundException;
import com.food.recipe.api.exception.NoRecipesFoundException;
import com.food.recipe.api.exception.RecipeNotFoundException;
import com.food.recipe.api.model.RecipesResponse;
import com.food.recipe.api.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.food.recipe.api.util.RecipeTestUtil.buildInstructions;
import static com.food.recipe.api.util.RecipeTestUtil.buildListOfIngredients;
import static com.food.recipe.api.util.RecipeTestUtil.createRecipeEntity;
import static com.food.recipe.api.util.RecipeTestUtil.createRecipeRequestDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class RecipeProcessImplTest {

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeProcessImpl recipeProcess;

    @Test
    void testCreateRecipe_whenRequestBodyIsValid_shouldReturnSuccess() {

        when(recipeService.createRecipe(any())).thenReturn(createRecipeEntity());
        var response = recipeProcess.createRecipe(createRecipeRequestDto());

        assertResponse(response, "Vada Pav", 4);
    }

    @Test
    void testCreateRecipe_whenResponseEntityIsNull_shouldReturnNotFoundException() {
        when(recipeService.createRecipe(any())).thenReturn(null);

        assertThatThrownBy(() -> recipeProcess.createRecipe(createRecipeRequestDto()))
                .isInstanceOf(RecipeNotFoundException.class)
                .hasMessage("The recipeEntity from service layer is null or empty.");
    }

    @Test
    void testUpdateRecipe_whenResponseEntityIsNull_shouldReturnNotFoundException() {
        when(recipeService.updateRecipe(anyInt(), any())).thenReturn(null);

        assertThatThrownBy(() -> recipeProcess.updateRecipe(1, createRecipeRequestDto()))
                .isInstanceOf(RecipeNotFoundException.class)
                .hasMessage("The updatedEntity from service layer is null.");
    }

    @Test
    void testUpdateRecipe_whenRequestBodyIsValid_shouldReturnSuccess() {

        var request = createRecipeRequestDto();
        request.setRecipeName("Samosa");
        request.setServings(2);
        request.setIngredients(List.of("Potato", "Green Chutney", "Spices", "Green Chilly"));

        RecipeEntity recipeEntity = createRecipeEntity();
        recipeEntity.setName("Samosa");
        recipeEntity.setServings(2);
        recipeEntity.setIngredients(List.of("Potato", "Green Chutney", "Spices", "Green Chilly"));

        when(recipeService.updateRecipe(anyInt(), any())).thenReturn(recipeEntity);
        var response = recipeProcess.updateRecipe(1, request);

        assertResponse(response, "Samosa", 2);
    }

    @Test
    void testDeleteRecipe_whenRequestBodyIsValid_shouldReturnTrue() {
        when(recipeService.deleteRecipe(anyInt())).thenReturn(true);

        var response = recipeProcess.deleteRecipe(1);

        assertThat(response).isTrue();
    }

    @Test
    void testDeleteRecipe_whenIdNotExist_shouldReturnFalse() {
        when(recipeService.deleteRecipe(anyInt())).thenReturn(false);

        var response = recipeProcess.deleteRecipe(1);

        assertThat(response).isFalse();
    }

    @Test
    void testGetRecipeById_whenResponseEntityIsEmpty_shouldReturnNotFoundException() {
        when(recipeService.getRecipeById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recipeProcess.getRecipeById(1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Record not found for given ID. " + 1L);
    }

    @Test
    void testGetRecipeById_whenRequestBodyIsValid_shouldReturnRecipeEntity() {
        when(recipeService.getRecipeById(anyInt())).thenReturn(Optional.of(createRecipeEntity()));

        var response = recipeProcess.getRecipeById(1);

        assertResponse(response, "Vada Pav", 4);
        assertThat(response.getIngredients()).isEqualTo(buildListOfIngredients());
        assertThat(response.getInstructions()).isEqualTo(buildInstructions());
    }

    @Test
    void testGetAllRecipes_whenRequestBodyIsValid_shouldReturnRecipeEntities() {
        when(recipeService.getAllRecipes()).thenReturn(List.of(createRecipeEntity()));

        var response = recipeProcess.getAllRecipes();

        assertListOfResponse(response);
    }

    @Test
    void testGetAllRecipes_whenReturnListOfEntityIsEmpty_shouldNotFoundException() {
        when(recipeService.getAllRecipes()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> recipeProcess.getAllRecipes())
                .isInstanceOf(NoRecipesFoundException.class)
                .hasMessage("No recipes found");
    }

    @Test
    void testGetAllRecipes_whenResponseReturnNull_shouldReturnNotFoundException() {
        when(recipeService.getAllRecipes()).thenReturn(null);

        assertThatThrownBy(() -> recipeProcess.getAllRecipes())
                .isInstanceOf(NoRecipesFoundException.class)
                .hasMessage("No recipes found");
    }

    @Test
    void testSearchRecipes_whenRequestBodyIsValid_shouldReturnRecipeEntities() {
        when(recipeService.searchRecipes(anyBoolean(), anyInt(), any(), any(), any()))
                .thenReturn(List.of(createRecipeEntity()));

        var response = recipeProcess.searchRecipes(true, 4,
                List.of("Potato", "Chutney"), List.of("Tomato"), "Boil Potato and Smash it.");

        assertListOfResponse(response);
    }

    private void assertListOfResponse(List<RecipesResponse> response) {
        assertThat(response).isNotNull();
        assertThat(response.getFirst()).isNotNull();
        assertThat(response.getFirst().getName()).isEqualTo("Vada Pav");
        assertThat(response.getFirst().getServings()).isEqualTo(4);
        assertThat(response.getFirst().getIsVegetarian()).isTrue();
        assertThat(response.getFirst().getIngredients()).isEqualTo(buildListOfIngredients());
        assertThat(response.getFirst().getInstructions()).isEqualTo(buildInstructions());
    }

    private void assertResponse(RecipesResponse response, String menu, int noOfServings) {
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(menu);
        assertThat(response.getIsVegetarian()).isTrue();
        assertThat(response.getServings()).isEqualTo(noOfServings);
    }
}
