package com.burk.food.recipe.api.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.burk.food.recipe.api.dto.request.RecipesRequestDto;
import com.burk.food.recipe.api.dto.response.RecipesResponseDto;
import com.burk.food.recipe.api.entity.RecipesEntity;
import com.burk.food.recipe.api.exception.NoRecipeFoundException;

/**
 * The below class acts as a service layer for calling the repository to access
 * and save the data into database.
 * 
 * @author snehalata.arun.raut
 */
@Service
public interface FoodRecipeService {

	/**
	 * The below method saves the input entity into the database.
	 * 
	 * @param recipe of type {@link RecipesEntity}
	 * @return recipes of type {@link RecipesEntity}
	 */
	public RecipesEntity createRecipe(RecipesEntity recipe);

	/**
	 * The below method firstly check if the given entity present into the database
	 * if yes then update the entity into the database otherwise throw
	 * {@link NoRecipeFoundException} which handles into process layer.
	 * 
	 * @param recipe of type {@link RecipesEntity}
	 * @return recipes of type {@link RecipesEntity}
	 */
	public RecipesEntity updateRecipe(RecipesEntity recipe);
	
	/**
	 * The below method delete the entity from database using recipeId;
	 * 
	 * @param recipeId of type {@link Integer}
	 * @return boolean value
	 */
	public boolean deleteRecipe(int recipeId);
	
	/**
	 * The below method return all recipes from database. If return empty list of
	 * recipe then throw exception {@link NoRecipeFoundException}
	 */
	public List<RecipesEntity> getAllRecipes();
	
	/**
	 * The below method return list of recipes which matches with input category. If
	 * not return any list or empty list  then throw exception {@link NoRecipeFoundException}.
	 * 
	 * @param category of type {@link String}
	 * @return listOfRecipesEntity of type {@link List<RecipesEntity>}
	 */
	public List<RecipesEntity> getListOfRecipesByCategory(String category);
	
	/**
	 * The below method check whether the input recipeName of which category(either
	 * vegetarian,non-vegetarian etc).
	 * 
	 * @param recipeName of type {@link String}
	 * @return boolean value of Boolean type
	 */
	public boolean checkRecipeCategoryByRecipeName(String recipeName);
	
	/**
	 * Below method return the list of recipe who has matching given numberOfServes
	 * and ingredient from database.If empty rows return or not found from database
	 * then it will throw exception as {@link NoRecipeFoundException}
	 * 
	 * @param numberOfServes of {@link Integer}
	 * @param ingredient     of type {@link String}
	 * @return list of recipesEntity of type {@link RecipesEntity}
	 */
	public List<RecipesEntity> getRecipeByIngredientAndServes(int numberOfServes, String ingredient);
	
	/**
	 * Below method return the list of recipe who has matching given ingredient and
	 * instruction from database. If empty rows return or not found any data from
	 * database then it will throw exception as {@link NoRecipeFoundException}
	 * 
	 * @param ingredient  of type {@link String}
	 * @param instruction of type {@link String}
	 * @return list of recipesEntity {@link RecipesEntity}
	 */
	public List<RecipesEntity> getRecipeByInstructionNotIngredient(String ingredient,String instruction);
	
	/**
	 * The below method checks into the database for given ingredient if matchable
	 * rows found for given ingredient then returns the record from database
	 * otherwise it will throw {@link NoRecipeFoundException}
	 * 
	 * @param ingredient of type {@link String}
	 * @return listOfRecipesEntity of type {@link RecipesEntity}
	 */
	public List<RecipesEntity> getAllRecipesByIngredient(String ingredient);
}
