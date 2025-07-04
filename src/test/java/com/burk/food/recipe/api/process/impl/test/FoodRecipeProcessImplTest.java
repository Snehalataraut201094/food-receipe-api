package com.burk.food.recipe.api.process.impl.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import com.burk.food.recipe.api.dto.response.RecipesResponseDto;
import com.burk.food.recipe.api.entity.RecipesEntity;
import com.burk.food.recipe.api.exception.NoRecipeFoundException;
import com.burk.food.recipe.api.process.impl.FoodRecipeProcessImpl;
import com.burk.food.recipe.api.service.FoodRecipeService;
import com.burt.food.recipe.api.util.CommonUtil;

/**
 * The below junits for {@link FoodRecipeProcessImpl}
 * 
 * @author snehalata.arun.raut
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class FoodRecipeProcessImplTest {

	@Mock
	private FoodRecipeService foodRecipeService;

	@InjectMocks
	private FoodRecipeProcessImpl foodRecipeProcess;

	@Before
	public void setUp() {
		foodRecipeService = Mockito.mock(FoodRecipeService.class);
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Positive test case for
	 * {@link FoodRecipeProcessImpl#createRecipe(com.burk.food.recipe.api.dto.request.RecipesRequestDto)}
	 * To create the recipe entry into database.
	 */
	@Test
	public void testCreateRecipe() {

		Mockito.when(foodRecipeService.createRecipe(any(RecipesEntity.class)))
				.thenReturn(CommonUtil.getRecipesEntity());

		RecipesResponseDto responseEntity = foodRecipeProcess.createRecipe(CommonUtil.mockRecipeRequestDto());
		assertNotNull(responseEntity);
		assertNotNull(responseEntity.getRecipeName());
		assertEquals(responseEntity.getRecipeName(), CommonUtil.mockRecipeRequestDto().getRecipeName());
	}

	/**
	 * Positive test case for
	 * {@link FoodRecipeProcessImpl#updateRecipe(com.burk.food.recipe.api.dto.request.RecipesUpdateRequestDto)}
	 * To update the recipe entry into database.
	 * 
	 */
	@Test
	public void testUpdateRecipes() {

		Mockito.when(foodRecipeService.updateRecipe(any(RecipesEntity.class)))
				.thenReturn(CommonUtil.getRecipesEntity());

		RecipesResponseDto responseEntity = foodRecipeProcess.updateRecipe(CommonUtil.mockUpdateRecipeRequest());
		assertNotNull(responseEntity);
		assertNotNull(responseEntity.getRecipeName());
	}

	/**
	 * Negative test case is for
	 * {@link FoodRecipeProcessImpl#updateRecipe(com.burk.food.recipe.api.dto.request.RecipesUpdateRequestDto)}.
	 * To test if recipeId not found for given recipeId then throw
	 * {@link NoRecipeFoundException}.
	 * 
	 */
	@Test(expected = NoRecipeFoundException.class)
	public void testUpdateRecipes_noIdFound_throwException() {

		Mockito.when(foodRecipeService.updateRecipe(any(RecipesEntity.class)))
				.thenThrow(new NoRecipeFoundException("No Id found for update"));

		RecipesResponseDto responseEntity = foodRecipeProcess.updateRecipe(CommonUtil.mockUpdateRecipeRequest());
		assertNotNull(responseEntity);
	}

	/**
	 * Positive test case for {@link FoodRecipeProcessImpl#deleteRecipe(int)}. To
	 * delete the recipe if recipeId matches.
	 * 
	 */
	@Test
	public void testDeleteRecipes() {

		Mockito.when(foodRecipeService.deleteRecipe(anyInt())).thenReturn(true);

		boolean isRowDeleted = foodRecipeProcess.deleteRecipe(CommonUtil.mockUpdateRecipeRequest().getRecipeId());
		assertTrue(isRowDeleted);
	}

	/**
	 * Negative test case for {@link FoodRecipeProcessImpl#deleteRecipe(int)}. If
	 * recipeId not found for given recipeId then throw
	 * {@link NoRecipeFoundException}.
	 * 
	 */
	@Test(expected = NoRecipeFoundException.class)
	public void testDeleteRecipe_noIdFound_throwException() {

		Mockito.when(foodRecipeService.deleteRecipe(anyInt()))
				.thenThrow(new NoRecipeFoundException("No Recipe found for given recipeId"));

		boolean isRowDeleted = foodRecipeProcess.deleteRecipe(CommonUtil.mockUpdateRecipeRequest().getRecipeId());
		assertFalse(isRowDeleted);
	}

	/**
	 * Positive test case for {@link FoodRecipeProcessImpl#getAllRecipes()} It
	 * returns the list of recipes.
	 * 
	 */
	@Test
	public void testGetAllRecipes() {

		Mockito.when(foodRecipeService.getAllRecipes()).thenReturn(CommonUtil.getListOfRecipesEntities());

		List<RecipesResponseDto> responseEntityRecipes = foodRecipeProcess.getAllRecipes();

		assertNotNull(responseEntityRecipes);
		assertNotNull(responseEntityRecipes.get(0));
		assertNotNull(responseEntityRecipes.get(0).getRecipeId());
	}

	/**
	 * Negative test case for {@link FoodRecipeProcessImpl#getAllRecipes()}. Test
	 * case to check if list returns empty throw exception
	 * {@link NoRecipeFoundException}. {@link NoRecipeFoundException}
	 * 
	 */
	@Test(expected = NoRecipeFoundException.class)
	public void testGetAllRecipes_returnEmptyList_throwException() {

		Mockito.when(foodRecipeService.getAllRecipes())
				.thenThrow(new NoRecipeFoundException("There is not data present in database."));

		List<RecipesResponseDto> responseEntityRecipes = foodRecipeProcess.getAllRecipes();

		assertNotNull(responseEntityRecipes);
	}

	/**
	 * Positive test case is for
	 * {@link FoodRecipeProcessImpl#getAllRecipesByCategory(String)} It checks the
	 * for given category matches with database rows and if matches return list
	 * other wise throw exception.
	 * 
	 * The test case asserts the return response using Assertions.
	 */
	@Test
	public void testGetAllRecipesByCategory() {

		Mockito.when(foodRecipeService.getListOfRecipesByCategory(anyString()))
				.thenReturn(CommonUtil.getListOfRecipesEntities());

		List<RecipesResponseDto> responseEntityRecipes = foodRecipeProcess
				.getAllRecipesByCategory(CommonUtil.mockRecipeRequestDto().getRecipeCategory());

		assertNotNull(responseEntityRecipes);
		assertNotNull(responseEntityRecipes.get(0));
		assertNotNull(responseEntityRecipes.get(0).getRecipeId());
	}

	/**
	 * Negative test case is for
	 * {@link FoodRecipeProcessImpl#getAllRecipesByCategory(String)} If category is
	 * not matches with row throws exception {@link NoRecipeFoundException}.
	 * 
	 * The test case asserts the return response using Assertions.
	 */
	@Test(expected = NoRecipeFoundException.class)
	public void testGetAllRecipesByCategory_returnEmptyList_throwException() {

		Mockito.when(foodRecipeService.getListOfRecipesByCategory(anyString()))
				.thenThrow(new NoRecipeFoundException("The is list not found for input category."));

		List<RecipesResponseDto> responseEntityRecipes = foodRecipeProcess
				.getAllRecipesByCategory(CommonUtil.mockRecipeRequestDto().getRecipeCategory());

		assertNotNull(responseEntityRecipes);
	}

	/**
	 * Positive test case for
	 * {@link FoodRecipeProcessImpl#checkRecipeCategoryByRecipeName(String)} It
	 * checks the given recipeName with the category if the category is valid then
	 * return string.
	 * 
	 */
	@Test
	public void testCheckRecipeCategoryByRecipeName() {

		Mockito.when(foodRecipeService.checkRecipeCategoryByRecipeName(anyString())).thenReturn(true);

		String recipeCategory = foodRecipeProcess
				.checkRecipeCategoryByRecipeName(CommonUtil.mockRecipeRequestDto().getRecipeName());

		assertNotNull(recipeCategory);
	}

	/**
	 * Negative test case for
	 * {@link FoodRecipeProcessImpl#checkRecipeCategoryByRecipeName(String)}. It
	 * checks if given recipeName is not exist then throw exception.
	 * 
	 */
	@Test(expected = NoRecipeFoundException.class)
	public void testCheckRecipeCategoryByRecipeName_noRecipeName_exist() {

		Mockito.when(foodRecipeService.checkRecipeCategoryByRecipeName(anyString()))
				.thenThrow(new NoRecipeFoundException("The recipeName is not found in databse."));

		String recipeCategory = foodRecipeProcess
				.checkRecipeCategoryByRecipeName(CommonUtil.mockRecipeRequestDto().getRecipeName());
		assertNotNull(recipeCategory);
	}

	/**
	 * Positive test case is for
	 * {@link FoodRecipeProcessImpl#getRecipeByInstructionNotIngredient(String, String)}
	 * It checks the given ingredient and instruction input into rows and return
	 * list of recipes if matching rows found.
	 * 
	 */
	@Test
	public void testGetRecipeByInstructionNotIngredient() {

		Mockito.when(foodRecipeService.getRecipeByInstructionNotIngredient(anyString(), anyString()))
				.thenReturn(CommonUtil.getListOfRecipesEntities());

		List<RecipesResponseDto> listOfRecipesResponseDtos = foodRecipeProcess.getRecipeByInstructionNotIngredient(
				CommonUtil.mockRecipeRequestDto().getIngredients(),
				CommonUtil.mockRecipeRequestDto().getInstructions());

		assertNotNull(listOfRecipesResponseDtos);
		assertNotNull(listOfRecipesResponseDtos.get(0));
		assertNotNull(listOfRecipesResponseDtos.get(0).getRecipeId());
	}

	/**
	 * Negative test case is for
	 * {@link FoodRecipeProcessImpl#getRecipeByInstructionNotIngredient(String, String)}
	 * If there is no rows into database for checking the ingredient and
	 * instruction.
	 */
	@Test(expected = NoRecipeFoundException.class)
	public void testGetRecipeByInstructionNotIngredient_noDataFound_throwException() {

		Mockito.when(foodRecipeService.getRecipeByInstructionNotIngredient(anyString(), anyString()))
				.thenThrow(new NoRecipeFoundException("The data not found for given instruction and ingredient."));

		List<RecipesResponseDto> listRecipesResponseDtos = foodRecipeProcess.getRecipeByInstructionNotIngredient(
				CommonUtil.mockRecipeRequestDto().getIngredients(),
				CommonUtil.mockRecipeRequestDto().getInstructions());
		assertNotNull(listRecipesResponseDtos);
	}

	/**
	 * Negative test case is for
	 * {@link FoodRecipeProcessImpl#getRecipeByInstructionNotIngredient(int, String)}
	 * It checks the given ingredient and instruction input into database and if now
	 * row found then throw {@link NoRecipeFoundException}
	 */
	@Test(expected = NoRecipeFoundException.class)
	public void testGetRecipeByInstructionNotIngredient_returnEmptyList_throwException() {

		Mockito.when(foodRecipeService.getRecipeByInstructionNotIngredient(anyString(), anyString()))
				.thenReturn(new ArrayList<>());

		List<RecipesResponseDto> listRecipesResponseDtos = foodRecipeProcess.getRecipeByInstructionNotIngredient(
				CommonUtil.mockRecipeRequestDto().getIngredients(),
				CommonUtil.mockRecipeRequestDto().getInstructions());
		assertNotNull(listRecipesResponseDtos);
	}

	/**
	 * Negative test case is for
	 * {@link FoodRecipeProcessImpl#getRecipeByIngredientAndServes(int, String)} It
	 * checks the given ingredient and noOfServes input into rows and returns the
	 * corresponding matching rows from database.
	 * 
	 */
	@Test(expected = NoRecipeFoundException.class)
	public void testGetRecipeByIngredientAndServes_noDataFound_throwException() {

		Mockito.when(foodRecipeService.getRecipeByIngredientAndServes(anyInt(), anyString()))
				.thenThrow(new NoRecipeFoundException("The data not found for given ingredient and serves"));

		List<RecipesResponseDto> listRecipesResponseDtos = foodRecipeProcess.getRecipeByIngredientAndServes(
				CommonUtil.mockRecipeRequestDto().getNoOfServings(),
				CommonUtil.mockRecipeRequestDto().getIngredients());

		assertNotNull(listRecipesResponseDtos);
	}

	/**
	 * Negative test case is for
	 * {@link FoodRecipeProcessImpl#getRecipeByIngredientAndServes(int, String)}. If
	 * matching rows are not found with ingredient and noOfServes the throw
	 * exception {@link NoRecipeFoundException}
	 */
	@Test(expected = NoRecipeFoundException.class)
	public void testGetRecipeByIngredientAndServes_returnEmptyList_throwException() {

		Mockito.when(foodRecipeService.getRecipeByIngredientAndServes(anyInt(), anyString()))
				.thenReturn(new ArrayList<>());

		List<RecipesResponseDto> listRecipesResponseDtos = foodRecipeProcess.getRecipeByIngredientAndServes(
				CommonUtil.mockRecipeRequestDto().getNoOfServings(),
				CommonUtil.mockRecipeRequestDto().getIngredients());

		assertNotNull(listRecipesResponseDtos);
	}

	/**
	 * Positive test case is for
	 * {@link FoodRecipeProcessImpl#getRecipeByIngredientAndServes(int, String)}.
	 * 
	 * It checks the given ingredient and noOfServes input into database and returns
	 * the corresponding matching rows from database.
	 * 
	 */
	@Test
	public void testGetRecipeByIngredientAndServes() {

		Mockito.when(foodRecipeService.getRecipeByIngredientAndServes(anyInt(), anyString()))
				.thenReturn(CommonUtil.getListOfRecipesEntities());

		List<RecipesResponseDto> listOfRecipesResponseDtos = foodRecipeProcess.getRecipeByIngredientAndServes(
				CommonUtil.mockRecipeRequestDto().getNoOfServings(),
				CommonUtil.mockRecipeRequestDto().getIngredients());

		assertNotNull(listOfRecipesResponseDtos);
		assertNotNull(listOfRecipesResponseDtos.get(0));
		assertNotNull(listOfRecipesResponseDtos.get(0).getRecipeId());
	}

	/**
	 * Negative test case is for
	 * {@link FoodRecipeProcessImpl#getAllRecipesByIngredient(String)} . It checks
	 * data present in database if not then throw exception
	 * {@link NoRecipeFoundException}.
	 * 
	 */
	@Test(expected = NoRecipeFoundException.class)
	public void testGetAllRecipesByIngredient_noDataFound_throwException() {

		Mockito.when(foodRecipeService.getAllRecipesByIngredient(anyString()))
				.thenThrow(new NoRecipeFoundException("The data not found for given ingredient."));

		List<RecipesResponseDto> listRecipesResponseDtos = foodRecipeProcess
				.getAllRecipesByIngredient(CommonUtil.mockRecipeRequestDto().getIngredients());

		assertNotNull(listRecipesResponseDtos);
	}

	/**
	 * Negative test case is for
	 * {@link FoodRecipeProcessImpl#getAllRecipesByIngredient(String)} . It checks
	 * if given ingredient is not matches with any row throw exception.
	 * 
	 * If the data from the database is not present then throw a exception.
	 * {@link NoRecipeFoundException}
	 */
	@Test(expected = NoRecipeFoundException.class)
	public void testGetAllRecipesByIngredient_returnEmptyList_throwException() {

		Mockito.when(foodRecipeService.getAllRecipesByIngredient(anyString())).thenReturn(new ArrayList<>());

		List<RecipesResponseDto> listRecipesResponseDtos = foodRecipeProcess
				.getAllRecipesByIngredient(CommonUtil.mockRecipeRequestDto().getIngredients());

		assertNotNull(listRecipesResponseDtos);
	}

	/**
	 * Positive test case is for
	 * {@link FoodRecipeProcessImpl#getAllRecipesByIngredient(String)} It checks the
	 * given ingredient input into database and returns the corresponding matching
	 * rows from database.
	 * 
	 */
	@Test
	public void testGetAllRecipesByIngredient() {

		Mockito.when(foodRecipeService.getAllRecipesByIngredient(anyString()))
				.thenReturn(CommonUtil.getListOfRecipesEntities());

		List<RecipesResponseDto> listOfRecipesResponseDtos = foodRecipeProcess
				.getAllRecipesByIngredient(CommonUtil.mockRecipeRequestDto().getIngredients());

		assertNotNull(listOfRecipesResponseDtos);
		assertNotNull(listOfRecipesResponseDtos.get(0));
		assertNotNull(listOfRecipesResponseDtos.get(0).getRecipeId());
	}
}
