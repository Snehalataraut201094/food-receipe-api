package com.food.recipe.api.mapper;

import com.food.recipe.api.entity.RecipeEntity;
import com.food.recipe.api.model.RecipeRequestDto;

/**
 * The below class is for mapping the request from {@link RecipeRequestDto} to {@link RecipeEntity}.
 * for persisting the entity into database.
 *
 * @author snehalata.arun.raut
 */
public class RecipeRequestMapper {

    /**
     * Below method maps the {@link RecipeRequestDto} to {@link RecipeEntity}
     *
     * @param recipesDto of type {@link RecipeRequestDto}
     * @return recipesEntity of type {@link RecipeEntity}
     */
    public static RecipeEntity createRecipeEntity(RecipeRequestDto recipesDto) {
        return RecipeEntity
                .builder()
                .name(recipesDto.getRecipeName())
                .isVegetarian(recipesDto.getIsVegetarian())
                .ingredients(recipesDto.getIngredients())
                .instructions(recipesDto.getInstructions())
                .servings(recipesDto.getServings())
                .build();
    }
}
