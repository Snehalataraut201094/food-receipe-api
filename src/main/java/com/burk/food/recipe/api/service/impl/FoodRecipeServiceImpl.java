package com.burk.food.recipe.api.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.burk.food.recipe.api.entity.RecipesEntity;
import com.burk.food.recipe.api.exception.NoRecipeFoundException;
import com.burk.food.recipe.api.repository.FoodRecipeRepository;
import com.burk.food.recipe.api.service.FoodRecipeService;

import lombok.extern.slf4j.Slf4j;

/**
 * The below class is service class which provides the service.Here this class
 * calls the repository .
 * 
 * @author snehalata.arun.raut
 *
 */
@Service
@Slf4j
public class FoodRecipeServiceImpl implements FoodRecipeService {

	private FoodRecipeRepository foodRecipeRepository;

	public FoodRecipeServiceImpl(FoodRecipeRepository foodRecipeRepository) {
		super();
		this.foodRecipeRepository = foodRecipeRepository;
	}

	@Override
	public RecipesEntity createRecipe(RecipesEntity recipe) {
		log.debug("The recipe entity to add :{}", recipe);
		RecipesEntity addedRecipe = foodRecipeRepository.save(recipe);
		log.debug("The added recipe into database is :{}", addedRecipe);
		return addedRecipe;
	}

	@Override
	public RecipesEntity updateRecipe(RecipesEntity toUpdateRecipe) {
		log.debug("The recipe entity to update :{}", toUpdateRecipe);
		return foodRecipeRepository.findById(toUpdateRecipe.getRecipeId()).map(oldRecipe -> {
			oldRecipe.setRecipeId(toUpdateRecipe.getRecipeId());
			oldRecipe.setRecipeName(toUpdateRecipe.getRecipeName());
			oldRecipe.setNoOfServings(toUpdateRecipe.getNoOfServings());
			oldRecipe.setIngredients(toUpdateRecipe.getIngredients());
			oldRecipe.setInstructions(toUpdateRecipe.getInstructions());
			oldRecipe.setRecipeCategory(toUpdateRecipe.getRecipeCategory());
			return foodRecipeRepository.save(oldRecipe);
		}).orElseThrow(() -> new NoRecipeFoundException("For the given recipeId the data not found for update"));
	}

	@Override
	public boolean deleteRecipe(int recipeId) {

		log.debug("The recipeId to delete from databse is:{}", recipeId);
		Optional<RecipesEntity> recipeToDelete = foodRecipeRepository.findById(recipeId);
		if (recipeToDelete.isPresent()) {
			foodRecipeRepository.deleteById(recipeId);
			return true;
		} else {
			throw new NoRecipeFoundException("No record found with given recipeId");
		}
	}

	@Override
	public List<RecipesEntity> getAllRecipes() {
		List<RecipesEntity> recipesResponseDtos = foodRecipeRepository.findAll();
		if (CollectionUtils.isEmpty(recipesResponseDtos)) {
			throw new NoRecipeFoundException("The data is not present in databse.");
		}
		return recipesResponseDtos;
	}

	@Override
	public List<RecipesEntity> getListOfRecipesByCategory(String category) {
		log.debug("The input category for which need to retrieve list of recipies from databse is:{}", category);
		return foodRecipeRepository.findAllByRecipeCategoryIgnoreCase(category);
	}

	@Override
	public boolean checkRecipeCategoryByRecipeName(String recipeName) {

		List<RecipesEntity> listOfRecipes = foodRecipeRepository.findAllByRecipeNameIgnoreCase(recipeName);
		if (CollectionUtils.isEmpty(listOfRecipes)) {
			throw new NoRecipeFoundException("There is not data found for given recipeName.");
		}
		return listOfRecipes.stream()
				.anyMatch((listOfReturnRecipes) -> listOfReturnRecipes.getRecipeCategory().equalsIgnoreCase("veg"));
	}

	@Override
	public List<RecipesEntity> getRecipeByIngredientAndServes(int numberOfServes, String ingredient) {
		List<RecipesEntity> listOfRecipeEntity = foodRecipeRepository.findAllByIngredientAndServes(numberOfServes,
				ingredient);
		if (!CollectionUtils.isEmpty(listOfRecipeEntity)) {
			return listOfRecipeEntity;
		} else {
			throw new NoRecipeFoundException("No recipe found for given numberOfServes and ingredients.");
		}
	}

	@Override
	public List<RecipesEntity> getRecipeByInstructionNotIngredient(String ingredient, String instruction) {
		List<RecipesEntity> listOfRecipeEntity = foodRecipeRepository.findAllByInstructionNotIngredient(ingredient,
				instruction);
		if (!CollectionUtils.isEmpty(listOfRecipeEntity)) {
			return listOfRecipeEntity;
		} else {
			throw new NoRecipeFoundException("No data in the database for given ingredient and instruction.");
		}
	}

	@Override
	public List<RecipesEntity> getAllRecipesByIngredient(String ingredient) {
		return foodRecipeRepository.findAllByIngredientsIgnoreCase(ingredient);
	}
}
