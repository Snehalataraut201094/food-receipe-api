package com.burt.food.recipe.api.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.burk.food.recipe.api.dto.request.RecipesRequestDto;
import com.burk.food.recipe.api.dto.request.RecipesUpdateRequestDto;
import com.burk.food.recipe.api.dto.response.RecipesResponseDto;
import com.burk.food.recipe.api.entity.RecipesEntity;

public class CommonUtil {

	public static RecipesUpdateRequestDto mockUpdateRecipeRequest() {
		RecipesUpdateRequestDto recipesUpdateRequestDto = new RecipesUpdateRequestDto();
		recipesUpdateRequestDto.setIngredients("Potato");
		recipesUpdateRequestDto.setInstructions("cooker");
		recipesUpdateRequestDto.setNoOfServings(2);
		recipesUpdateRequestDto.setRecipeName("Vada-Pav");
		recipesUpdateRequestDto.setRecipeId(1);
		recipesUpdateRequestDto.setRecipeCategory("veg");
		return recipesUpdateRequestDto;
	}

	public static RecipesResponseDto getRecipesResponse() {
		RecipesResponseDto recipesResponseDto = new RecipesResponseDto();
		recipesResponseDto.setIngredients("Onion");
		recipesResponseDto.setInstructions("Oven");
		recipesResponseDto.setNoOfServings(1);
		recipesResponseDto.setRecipeCategory("veg");
		recipesResponseDto.setRecipeName("pav-bhaji");
		return recipesResponseDto;
	}

	public static List<RecipesResponseDto> getListOfRecipesResponseDto() {

		List<RecipesResponseDto> listOfRecipesRequestDtos = new ArrayList<>();
		RecipesResponseDto recipesRequestDto = new RecipesResponseDto();
		recipesRequestDto.setIngredients("Onion");
		recipesRequestDto.setInstructions("Oven");
		recipesRequestDto.setNoOfServings(1);
		recipesRequestDto.setRecipeCategory("veg");
		recipesRequestDto.setRecipeName("pav-bhaji");

		RecipesResponseDto recipeDto = new RecipesResponseDto();
		recipeDto.setRecipeName("Pav-Bhaji");
		recipeDto.setRecipeCategory("veg");
		recipeDto.setNoOfServings(3);
		recipeDto.setInstructions("Utensils");
		recipeDto.setIngredients("Potatoes");
		recipeDto.setRecipeId(4);

		listOfRecipesRequestDtos.add(recipeDto);
		listOfRecipesRequestDtos.add(recipesRequestDto);

		return listOfRecipesRequestDtos;
	}

	public static RecipesRequestDto mockRecipeRequestDto() {
		RecipesRequestDto recipeDto = new RecipesRequestDto();
		recipeDto.setRecipeName("pav-bhaji");
		recipeDto.setRecipeCategory("veg");
		recipeDto.setNoOfServings(3);
		recipeDto.setInstructions("Utensils");
		recipeDto.setIngredients("Potatoes");
		return recipeDto;
	}

	public static RecipesEntity getRecipesEntity() {
		RecipesEntity recipesEntity = new RecipesEntity();
		recipesEntity.setIngredients("Onion");
		recipesEntity.setInstructions("utensils");
		recipesEntity.setNoOfServings(3);
		recipesEntity.setRecipeCategory("veg");
		recipesEntity.setRecipeName("Oninon-Paratha");
		recipesEntity.setRecipeId(5);
		return recipesEntity;
	}
	
	public static RecipesEntity mockRecipeEntityForUpdate() {
		RecipesEntity recipesEntity = new RecipesEntity();
		recipesEntity.setIngredients("Potato");
		recipesEntity.setInstructions("Cooker");
		recipesEntity.setNoOfServings(2);
		recipesEntity.setRecipeCategory("Non-veg");
		recipesEntity.setRecipeName("vada-pav");
		return recipesEntity;
	}

	public static List<RecipesEntity> getListOfRecipesEntities() {

		List<RecipesEntity> listOfEntities = new ArrayList<>();
		RecipesEntity recipesEntity = new RecipesEntity();
		recipesEntity.setIngredients("Onion");
		recipesEntity.setInstructions("Oven");
		recipesEntity.setNoOfServings(3);
		recipesEntity.setRecipeCategory("veg");
		recipesEntity.setRecipeName("pav-bhaji");

		RecipesEntity recipesEntityVal = new RecipesEntity();
		recipesEntityVal.setIngredients("chiken");
		recipesEntityVal.setInstructions("cooker");
		recipesEntityVal.setNoOfServings(1);
		recipesEntityVal.setRecipeCategory("non-veg");
		recipesEntityVal.setRecipeName("chiken-fry");
		
		RecipesEntity recipesEntitys = new RecipesEntity();
		recipesEntitys.setIngredients("Potato");
		recipesEntitys.setInstructions("utensils");
		recipesEntitys.setNoOfServings(2);
		recipesEntitys.setRecipeCategory("veg");
		recipesEntitys.setRecipeName("vada-pav");

		listOfEntities.add(recipesEntityVal);
		listOfEntities.add(recipesEntitys);
		listOfEntities.add(recipesEntity);

		return listOfEntities;
	}
	
	public static RecipesEntity getRecipesEntityForUpdate() {
		RecipesEntity recipesEntity = new RecipesEntity();
		recipesEntity.setIngredients("Potato");
		recipesEntity.setInstructions("Cooker");
		recipesEntity.setNoOfServings(2);
		recipesEntity.setRecipeCategory("Non-veg");
		recipesEntity.setRecipeName("vada-pav");
		return recipesEntity;
	}
}
