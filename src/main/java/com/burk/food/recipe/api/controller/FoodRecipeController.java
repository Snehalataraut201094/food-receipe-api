package com.burk.food.recipe.api.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.burk.food.recipe.api.dto.request.RecipesRequestDto;
import com.burk.food.recipe.api.dto.request.RecipesUpdateRequestDto;
import com.burk.food.recipe.api.dto.response.RecipesResponseDto;
import com.burk.food.recipe.api.process.FoodRecipeProcess;
import com.burk.food.recipe.api.util.RecipeConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * The below class is the controller class which handles or control the incoming
 * HTTP request and delegates the request to process layer method.
 * 
 * @author snehalata.arun.raut
 */
@RestController
@RequestMapping("/food-recipe-api/v1")
@Slf4j
public class FoodRecipeController {

	private FoodRecipeProcess foodRecipeProcess;

	public FoodRecipeController(FoodRecipeProcess foodRecipeProcess) {
		super();
		this.foodRecipeProcess = foodRecipeProcess;
	}

	@RequestMapping(value = "/createRecipe", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<RecipesResponseDto> createRecipes(@Valid @RequestBody RecipesRequestDto recipes) {
		RecipesResponseDto createdRecipe = foodRecipeProcess.createRecipe(recipes);
		log.debug("The created recipe is:{}", createdRecipe);
		return new ResponseEntity<>(createdRecipe, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/updateRecipe", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public ResponseEntity<RecipesResponseDto> updateRecipe(
			@Valid @RequestBody RecipesUpdateRequestDto recipesUpdateRequestDto) {
		RecipesResponseDto updatedRecipe = foodRecipeProcess.updateRecipe(recipesUpdateRequestDto);
		log.debug("The updated recipe is:{}", updatedRecipe);
		return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);
	}

	@RequestMapping(value = "/deleteRecipe/{recipeId}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<String> deleteRecipe(@PathVariable int recipeId) {
		boolean isRowDeleted = foodRecipeProcess.deleteRecipe(recipeId);
		String rowDeletedMessage = isRowDeleted ? RecipeConstant.ROW_DELETED : RecipeConstant.ROW_NOT_DELETED;
		return new ResponseEntity<>(rowDeletedMessage, HttpStatus.OK);

	}

	@RequestMapping(value = "/getAllRecipe", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<RecipesResponseDto>> getAllRecipes() {
		List<RecipesResponseDto> listOfRecipe = foodRecipeProcess.getAllRecipes();
		log.debug("The list of all the recipes are:{}", listOfRecipe);
		return new ResponseEntity<>(listOfRecipe, HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllRecipe/category/{recipeCategory}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<RecipesResponseDto>> getAllRecipesByCategory(
			@NotEmpty @PathVariable String recipeCategory) {
		List<RecipesResponseDto> listOfRecipesByCategory = foodRecipeProcess.getAllRecipesByCategory(recipeCategory);
		log.debug("The list of all the recipes are:{}", listOfRecipesByCategory);
		return new ResponseEntity<>(listOfRecipesByCategory, HttpStatus.OK);
	}

	@RequestMapping(value = "/checkRecipe/recipeName/{recipeName}", method = RequestMethod.GET,produces = "application/json")
	public ResponseEntity<String> checkRecipeCategoryByRecipeName(@NotEmpty @PathVariable String recipeName) {
		String recipeCategory = foodRecipeProcess.checkRecipeCategoryByRecipeName(recipeName);
		log.debug("The given recipe is:{}", recipeCategory);
		return new ResponseEntity<>(recipeCategory, HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllRecipe/numberOfServes/{numberOfServes}/ingredient/{ingredient}", method = RequestMethod.GET,produces = "application/json")
	public ResponseEntity<List<RecipesResponseDto>> getRecipeByIngredientAndServes(@PathVariable int numberOfServes,
			@NotEmpty @PathVariable String ingredient) {
		List<RecipesResponseDto> listOfRecipesDto = foodRecipeProcess.getRecipeByIngredientAndServes(numberOfServes,
				ingredient);
		log.debug("The list of recipes for given numberOfServes and ingredient:{}", listOfRecipesDto);
		return new ResponseEntity<>(listOfRecipesDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllRecipe/ingredient/{ingredient}/instruction/{instruction}", method = RequestMethod.GET,produces = "application/json")
	public ResponseEntity<List<RecipesResponseDto>> getRecipeByInstructionNotIngredient(
			@NotEmpty @PathVariable String ingredient, @NotEmpty @PathVariable String instruction) {
		List<RecipesResponseDto> listOfRecipesDto = foodRecipeProcess.getRecipeByInstructionNotIngredient(ingredient,
				instruction);
		log.debug("The list of recipes for given instruction and ingredient:{}", listOfRecipesDto);
		return new ResponseEntity<>(listOfRecipesDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/getAllRecipe/ingredient/{ingredient}", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<RecipesResponseDto>> getAllRecipesByIngredient(
			@NotEmpty @PathVariable String ingredient) {
		List<RecipesResponseDto> listOfRecipesByCategory = foodRecipeProcess.getAllRecipesByIngredient(ingredient);
		log.debug("The list of all the recipes for given ingredient:{}", listOfRecipesByCategory);
		return new ResponseEntity<>(listOfRecipesByCategory, HttpStatus.OK);
	}
}
