package com.burk.food.recipe.api.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.burk.food.recipe.api.dto.request.RecipesRequestDto;
import com.burk.food.recipe.api.dto.request.RecipesUpdateRequestDto;
import com.burk.food.recipe.api.dto.response.RecipesResponseDto;
import com.burk.food.recipe.api.entity.RecipesEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * The {@link RecipesApiResponseMapper} class is for mapping the response from
 * {@link RecipesEntity} or {@link RecipesUpdateRequestDto} to
 * {@link RecipesRequestDto}.
 * 
 * @author snehalata.arun.raut
 *
 */
@Slf4j
public class RecipesApiResponseMapper {

	public static List<RecipesResponseDto> mapRecipeEntityToRecipeDto(List<RecipesEntity> recipesEntity) {

		List<RecipesResponseDto> listOfRecipesDto = new ArrayList<>();
		;
		if (!CollectionUtils.isEmpty(recipesEntity)) {
			recipesEntity.forEach((recipesEntityList) -> {
				RecipesResponseDto recipesDto = new RecipesResponseDto();
				recipesDto.setRecipeId(recipesEntityList.getRecipeId());
				recipesDto.setIngredients(recipesEntityList.getIngredients());
				recipesDto.setInstructions(recipesEntityList.getInstructions());
				recipesDto.setNoOfServings(recipesEntityList.getNoOfServings());
				recipesDto.setRecipeCategory(recipesEntityList.getRecipeCategory());
				recipesDto.setRecipeName(recipesEntityList.getRecipeName());
				listOfRecipesDto.add(recipesDto);
			});
		}
		log.debug("After mapping entity into dto list of recipeDto the response is :{}" + listOfRecipesDto);
		return listOfRecipesDto;
	}

	public static RecipesResponseDto mapRecipeEntityToRecipeDto(RecipesEntity responseEntity) {

		RecipesResponseDto recipesResponseDto = new RecipesResponseDto();
		if (!ObjectUtils.isEmpty(responseEntity)) {
			recipesResponseDto.setRecipeId(responseEntity.getRecipeId());
			recipesResponseDto.setIngredients(responseEntity.getIngredients());
			recipesResponseDto.setInstructions(responseEntity.getInstructions());
			recipesResponseDto.setNoOfServings(responseEntity.getNoOfServings());
			recipesResponseDto.setRecipeCategory(responseEntity.getRecipeCategory());
			recipesResponseDto.setRecipeName(responseEntity.getRecipeName());
		}
		log.debug("After mapping entity into dto the recipeDto of the response is :{}" + recipesResponseDto);
		return recipesResponseDto;
	}
}
