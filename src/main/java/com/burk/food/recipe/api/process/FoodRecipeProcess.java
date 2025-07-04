/**
 * 
 */
package com.burk.food.recipe.api.process;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.burk.food.recipe.api.dto.request.RecipesRequestDto;
import com.burk.food.recipe.api.dto.request.RecipesUpdateRequestDto;
import com.burk.food.recipe.api.dto.response.RecipesResponseDto;
import com.burk.food.recipe.api.entity.RecipesEntity;
import com.burk.food.recipe.api.exception.NoRecipeFoundException;
import com.burk.food.recipe.api.service.FoodRecipeService;

import lombok.NonNull;

/**
 * The {@link FoodRecipeProcess} class contains all the business logic regarding
 * the food recipe API.
 * 
 * @author snehalata.arun.raut
 */
@Component
public interface FoodRecipeProcess {

	/**
	 * The below method saves the input entity into the database.
	 * 
	 * @param recipe of type {@link RecipesRequestDto}
	 * @return recipes of type {@link RecipesResponseDto}
	 */
	public RecipesResponseDto createRecipe(RecipesRequestDto recipe);

	/**
	 * The below method checks whether input entity is not null if non-null then
	 * calls the service layer method to add the entity into database otherwise
	 * throw exception {@link NoRecipeFoundException}
	 * 
	 * @param recipe of type {@link RecipesUpdateRequestDto}
	 * @return recipeResponseDto of type {@link RecipesResponseDto}
	 */
	public RecipesResponseDto updateRecipe(RecipesUpdateRequestDto recipe);

	/**
	 * The below method delete the entity from database using recipeId. If deleted
	 * from database then return true if not then return false.;
	 * 
	 * @param recipeId of type {@link Integer}
	 */
	public boolean deleteRecipe(int recipeId);

	/**
	 * The below method return all recipes from database. If return empty list of
	 * recipe then throw exception {@link NoRecipeFoundException}
	 */
	public List<RecipesResponseDto> getAllRecipes();

	/**
	 * The below method return list of recipes which matches with input category. If
	 * not return any list or empty list then throw exception {@link NoRecipeFoundException}.
	 * 
	 * @param category of type {@link String}
	 * @return listOfRecipiesByCategory of type {@link List<RecipesResponseDto>}
	 */
	public List<RecipesResponseDto> getAllRecipesByCategory(String category);

	/**
	 * The below process check whether the input recipeName of which category(either
	 * vegetarian,non-vegetarian etc).
	 * 
	 * @param recipeName of type {@link String}
	 * @return string value.
	 */
	public String checkRecipeCategoryByRecipeName(String recipeName);

	/**
	 * Below method return the list of recipe who has matching given numberOfServes
	 * and ingredient from database.If empty rows return or not found from database
	 * then it will throw exception as {@link NoRecipeFoundException}
	 * 
	 * @param numberOfServes of {@link Integer}
	 * @param ingredient     of type {@link String}
	 * @return list of recipes of type {@link RecipesResponseDto}
	 */
	public List<RecipesResponseDto> getRecipeByIngredientAndServes(int numberOfServes, String ingredient);

	/**
	 * Below method return the list of recipe who has matching given ingredient and
	 * instruction from database. If empty rows return or not found any data from
	 * database then it will throw exception as {@link NoRecipeFoundException}
	 * 
	 * @param ingredient  of type {@link String}
	 * @param instruction of type {@link String}
	 * @return list of {@link RecipesResponseDto}
	 */
	public List<RecipesResponseDto> getRecipeByInstructionNotIngredient(String ingredient, String instruction);

	/**
	 * The below method checks into the database for given ingredient if matchable
	 * rows found for given ingredient then returns the record from database
	 * otherwise it will throw {@link NoRecipeFoundException}
	 * 
	 * @param ingredient of type {@link String}
	 * @return listOfRecipesDto of type {@link RecipesResponseDto}
	 */
	public List<RecipesResponseDto> getAllRecipesByIngredient(String ingredient);
}
