package com.burk.food.recipe.api.controller.integration.test.util;

import com.burk.food.recipe.api.dto.request.RecipesRequestDto;
import com.burk.food.recipe.api.dto.request.RecipesUpdateRequestDto;

public class IntegrationCommonUtil {
	
	public static RecipesRequestDto createRecipesRequestDto() {
		RecipesRequestDto recipesRequestDto = new RecipesRequestDto();
		recipesRequestDto.setIngredients("potatoes");
		recipesRequestDto.setInstructions("cooker");
		recipesRequestDto.setNoOfServings(4);
		recipesRequestDto.setRecipeCategory("veg");
		recipesRequestDto.setRecipeName("Veg-Sandwich");
		return recipesRequestDto;
	}

	public static RecipesUpdateRequestDto mapResponseDtoToRequestDtoForUpdate(int recipeId) {
		RecipesUpdateRequestDto updateRecipesRequestDto = new RecipesUpdateRequestDto();
		updateRecipesRequestDto.setRecipeId(recipeId);
		updateRecipesRequestDto.setIngredients("potatoes");
		updateRecipesRequestDto.setInstructions("utensils");
		updateRecipesRequestDto.setNoOfServings(4);
		updateRecipesRequestDto.setRecipeCategory("veg");
		updateRecipesRequestDto.setRecipeName("pav-bhaji");
		return updateRecipesRequestDto;
	}

}
