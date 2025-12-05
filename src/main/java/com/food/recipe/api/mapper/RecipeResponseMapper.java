package com.food.recipe.api.mapper;

import com.food.recipe.api.entity.RecipeEntity;
import com.food.recipe.api.model.RecipesResponse;

/**
 * The below class is for mapping the returned response from {@link RecipeEntity} to {@link RecipesResponse}.
 *
 * @author snehalata.arun.raut
 */
public class RecipeResponseMapper {

    public static RecipesResponse mapToResponseDto(RecipeEntity recipeEntity) {

        return RecipesResponse
                .builder()
                .id(recipeEntity.getId())
                .name(recipeEntity.getName())
                .servings(recipeEntity.getServings())
                .isVegetarian(recipeEntity.getIsVegetarian())
                .instructions(recipeEntity.getInstructions())
                .ingredients(recipeEntity.getIngredients())
                .build();
    }
}
