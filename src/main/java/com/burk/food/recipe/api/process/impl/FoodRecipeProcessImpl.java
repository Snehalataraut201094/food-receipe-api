package com.burk.food.recipe.api.process.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.burk.food.recipe.api.dto.request.RecipesRequestDto;
import com.burk.food.recipe.api.dto.request.RecipesUpdateRequestDto;
import com.burk.food.recipe.api.dto.response.RecipesResponseDto;
import com.burk.food.recipe.api.entity.RecipesEntity;
import com.burk.food.recipe.api.exception.NoRecipeFoundException;
import com.burk.food.recipe.api.mapper.RecipesApiRequestMapper;
import com.burk.food.recipe.api.mapper.RecipesApiResponseMapper;
import com.burk.food.recipe.api.process.FoodRecipeProcess;
import com.burk.food.recipe.api.service.FoodRecipeService;
import com.burk.food.recipe.api.util.RecipeConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * The below class is the process class contains the business logic for the
 * corresponding operation.
 * 
 * @author snehalata.arun.raut
 *
 */
@Component
@Slf4j
public class FoodRecipeProcessImpl implements FoodRecipeProcess {

	private FoodRecipeService foodRecipeService;

	public FoodRecipeProcessImpl(FoodRecipeService foodRecipeService) {
		super();
		this.foodRecipeService = foodRecipeService;
	}

	@Override
	public RecipesResponseDto createRecipe(RecipesRequestDto recipe) {
		RecipesEntity processRequestRecipesEntity = RecipesApiRequestMapper.mapRecipeDtoToRecipeEntity(recipe);
		log.debug("The recipesRequest after mapping into recipesEntity used for create operation :{}" ,processRequestRecipesEntity);
		return RecipesApiResponseMapper
				.mapRecipeEntityToRecipeDto(foodRecipeService.createRecipe(processRequestRecipesEntity));
	}

	@Override
	public RecipesResponseDto updateRecipe(RecipesUpdateRequestDto recipe) {
		try {
			RecipesEntity processRequestRecipesEntity = RecipesApiRequestMapper.mapRecipeUpdateDtoToRecipeEntity(recipe);
			log.debug("The recipesRequest after mapping into recipesEntity used for update operation is :{}",
					processRequestRecipesEntity);
			return RecipesApiResponseMapper
					.mapRecipeEntityToRecipeDto(foodRecipeService.updateRecipe(processRequestRecipesEntity));
		} catch (NoRecipeFoundException ex) {
			log.error("The error occured :{}", ex.getMessage());
			throw ex;
		}
	}

	@Override
	public boolean deleteRecipe(int recipeId) {
		try {
			return foodRecipeService.deleteRecipe(recipeId);
		} catch (NoRecipeFoundException ex) {
			log.error("The error occured :{}", ex.getMessage());
			throw ex;
		}
	}

	@Override
	public List<RecipesResponseDto> getAllRecipes() {
		try {
			return RecipesApiResponseMapper.mapRecipeEntityToRecipeDto(foodRecipeService.getAllRecipes());
		} catch (NoRecipeFoundException ex) {
			log.error("The error occured :{}", ex.getMessage());
			throw ex;
		}
	}

	@Override
	public List<RecipesResponseDto> getAllRecipesByCategory(String category) {
		try {
			List<RecipesResponseDto> lisOfRecipesResponseDto = RecipesApiResponseMapper
					.mapRecipeEntityToRecipeDto(foodRecipeService.getListOfRecipesByCategory(category));
			if (CollectionUtils.isEmpty(lisOfRecipesResponseDto)) {
				throw new NoRecipeFoundException("No recipes found for given category");
			}
			return lisOfRecipesResponseDto;
		} catch (NoRecipeFoundException ex) {
			log.error("The error occured :{}", ex.getMessage());
			throw ex;
		}
	}

	@Override
	public String checkRecipeCategoryByRecipeName(String recipeName) {
		try {
			return foodRecipeService.checkRecipeCategoryByRecipeName(recipeName) ? RecipeConstant.DISH_IS_VEGETERIAN
					: RecipeConstant.DISH_IS_NOT_VEGETERIAN;
		} catch (NoRecipeFoundException ex) {
			log.error("The error occured :{}", ex.getMessage());
			throw ex;
		}
	}

	@Override
	public List<RecipesResponseDto> getRecipeByIngredientAndServes(int numberOfServes, String ingredient) {
		try {
			List<RecipesEntity> listOfRecipesEntities = foodRecipeService.getRecipeByIngredientAndServes(numberOfServes,
					ingredient);
			if (CollectionUtils.isEmpty(listOfRecipesEntities)) {
				throw new NoRecipeFoundException("No recipe found for given numberOfServes and ingredient. ");
			}
			return RecipesApiResponseMapper.mapRecipeEntityToRecipeDto(listOfRecipesEntities);
		} catch (NoRecipeFoundException ex) {
			log.error("The error occured :{}", ex.getMessage());
			throw ex;
		}
	}

	@Override
	public List<RecipesResponseDto> getRecipeByInstructionNotIngredient(String ingredient, String instruction) {
		try {
			List<RecipesEntity> listOfRecipeEntity = foodRecipeService.getRecipeByInstructionNotIngredient(ingredient,
					instruction);
			if (CollectionUtils.isEmpty(listOfRecipeEntity)) {
				throw new NoRecipeFoundException("No recipe found for given ingredient and instruction. ");
			}
			return RecipesApiResponseMapper.mapRecipeEntityToRecipeDto(listOfRecipeEntity);
		} catch (NoRecipeFoundException ex) {
			log.error("The error occured :{}", ex.getMessage());
			throw ex;
		}
	}

	@Override
	public List<RecipesResponseDto> getAllRecipesByIngredient(String ingredient) {
		try {
			List<RecipesEntity> listOfRescipesEntity = foodRecipeService.getAllRecipesByIngredient(ingredient);
			if (CollectionUtils.isEmpty(listOfRescipesEntity)) {
				throw new NoRecipeFoundException("No recipe found for given ingredient");
			}
			return RecipesApiResponseMapper.mapRecipeEntityToRecipeDto(listOfRescipesEntity);
		} catch (NoRecipeFoundException ex) {
			log.error("The error occured :{}", ex.getMessage());
			throw ex;
		}
	}
}
