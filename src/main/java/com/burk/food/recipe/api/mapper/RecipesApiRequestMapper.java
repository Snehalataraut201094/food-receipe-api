package com.burk.food.recipe.api.mapper;

import com.burk.food.recipe.api.dto.request.RecipesRequestDto;
import com.burk.food.recipe.api.dto.request.RecipesUpdateRequestDto;
import com.burk.food.recipe.api.entity.RecipesEntity;

/**
 * The {@link RecipesApiRequestMapper} class is for mapping the request from
 * {@link RecipesRequestDto} or {@link RecipesUpdateRequestDto} to
 * {@link RecipesEntity}.
 * 
 * @author snehalata.arun.raut
 *
 */
public class RecipesApiRequestMapper {

	/**
	 * Below method maps the {@link RecipesRequestDto} to {@link RecipesEntity}
	 * 
	 * @param recipesDto of type {@link RecipesRequestDto}
	 * @return recipesEntity of type {@link RecipesEntity}
	 */
	public static RecipesEntity mapRecipeDtoToRecipeEntity(RecipesRequestDto recipesDto) {

		RecipesEntity requestRecipesEntity = new RecipesEntity();
		requestRecipesEntity.setIngredients(recipesDto.getIngredients());
		requestRecipesEntity.setInstructions(recipesDto.getInstructions());
		requestRecipesEntity.setNoOfServings(recipesDto.getNoOfServings());
		requestRecipesEntity.setRecipeCategory(recipesDto.getRecipeCategory());
		requestRecipesEntity.setRecipeName(recipesDto.getRecipeName());
		return requestRecipesEntity;
	}

	/**
	 * Below method maps the {@link RecipesUpdateRequestDto} to
	 * {@link RecipesEntity}
	 * 
	 * @param recipesDto of type {@link RecipesUpdateRequestDto}
	 * @return recipesEntity of type {@link RecipesEntity}
	 */
	public static RecipesEntity mapRecipeUpdateDtoToRecipeEntity(RecipesUpdateRequestDto recipesUpdateRequestDto) {

		RecipesEntity requestRecipesEntity = new RecipesEntity();
		requestRecipesEntity.setRecipeId(recipesUpdateRequestDto.getRecipeId());
		requestRecipesEntity.setIngredients(recipesUpdateRequestDto.getIngredients());
		requestRecipesEntity.setInstructions(recipesUpdateRequestDto.getInstructions());
		requestRecipesEntity.setNoOfServings(recipesUpdateRequestDto.getNoOfServings());
		requestRecipesEntity.setRecipeCategory(recipesUpdateRequestDto.getRecipeCategory());
		requestRecipesEntity.setRecipeName(recipesUpdateRequestDto.getRecipeName());
		return requestRecipesEntity;
	}
}
