package com.burk.food.recipe.api.service.impl.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
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
import com.burk.food.recipe.api.repository.FoodRecipeRepository;
import com.burk.food.recipe.api.service.FoodRecipeService;
import com.burk.food.recipe.api.service.impl.FoodRecipeServiceImpl;
import com.burk.food.recipe.api.util.RecipeConstant;
import com.burt.food.recipe.api.util.CommonUtil;

/**
 * Junit for class {@link FoodRecipeService}
 * 
 * @author snehalata.arun.raut
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class FoodRecipeServiceImplTest {

	@Mock
	private FoodRecipeRepository foodRecipeRepository;

	@InjectMocks
	private FoodRecipeServiceImpl foodRecipeService;

	@Before
	public void setUp() {
		foodRecipeRepository = Mockito.mock(FoodRecipeRepository.class);
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Positive test case for {@link FoodRecipeService#createRecipe(RecipesEntity)}
	 * To create the recipe entry into database.
	 */
	@Test
	public void testCreateRecipe() {

		RecipesEntity responseEntity = createRecipeCall();
		assertNotNull(responseEntity);
		assertNotNull(responseEntity.getRecipeName());
		assertEquals(responseEntity.getRecipeName(), CommonUtil.getRecipesEntity().getRecipeName());
	}

	/**
	 * Positive test case for {@link FoodRecipeService#updateRecipe(RecipesEntity)}
	 * To update the recipe entry into database.
	 * 
	 */
	@Test
	public void testUpdateRecipes() {

		RecipesEntity recipesEntity = createRecipeCall();
		System.out.println("The created data :" + recipesEntity);

		Mockito.when(foodRecipeRepository.findById(anyInt()))
				.thenReturn(Optional.ofNullable(CommonUtil.getRecipesEntity()));

		RecipesEntity recipeEntityForUpdate = CommonUtil.getRecipesEntityForUpdate();
		recipeEntityForUpdate.setRecipeId(recipesEntity.getRecipeId());
		System.out.println("The recipeEntity for update :" + recipeEntityForUpdate);

		RecipesEntity responseEntity = foodRecipeService.updateRecipe(recipeEntityForUpdate);
		assertNotNull(responseEntity);
		assertNotNull(responseEntity.getRecipeName());
	}

	/**
	 * Negative test case is for
	 * {@link FoodRecipeService#updateRecipe(RecipesEntity)}. To test if recipeId
	 * not found for given recipeId then throw {@link NoRecipeFoundException}.
	 * 
	 */
	@Test(expected = NoRecipeFoundException.class)
	public void testUpdateRecipes_noIdFound_throwException() {

		Mockito.when(foodRecipeRepository.findById(anyInt()))
				.thenThrow(new NoRecipeFoundException("The recipeId not found for update operation."));

		RecipesEntity responseEntity = foodRecipeService.updateRecipe(CommonUtil.getRecipesEntity());
		assertNotNull(responseEntity);
	}

	/**
	 * Positive test case for {@link FoodRecipeService#deleteRecipe(int)}. To delete
	 * the recipe if recipeId matches.
	 * 
	 */
	@Test
	public void testDeleteRecipes() {

		createRecipeCall();
		Mockito.when(foodRecipeRepository.findById(anyInt()))
				.thenReturn(Optional.ofNullable(CommonUtil.getRecipesEntity()));

		boolean isRowDeleted = foodRecipeService.deleteRecipe(createRecipeCall().getRecipeId());
		assertTrue(isRowDeleted);
	}

	/**
	 * Negative test case for {@link FoodRecipeService#deleteRecipe(int)}. If
	 * recipeId not found for given recipeId then throw
	 * {@link NoRecipeFoundException}.
	 * 
	 */
	@Test(expected = NoRecipeFoundException.class)
	public void testDeleteRecipe_noIdFound_throwException() {

		Mockito.when(foodRecipeRepository.findById(anyInt())).thenReturn(Optional.empty());

		boolean isRowDeleted = foodRecipeService.deleteRecipe(CommonUtil.getRecipesEntity().getRecipeId());
		assertFalse(isRowDeleted);
	}

	/**
	 * Positive test case for {@link FoodRecipeServiceImpl#getAllRecipes()} It
	 * returns the list of recipes.
	 * 
	 */
	@Test
	public void testGetAllRecipes() {

		createRecipeCall();
		Mockito.when(foodRecipeRepository.findAll()).thenReturn(CommonUtil.getListOfRecipesEntities());

		List<RecipesEntity> responseEntityRecipes = foodRecipeService.getAllRecipes();

		assertNotNull(responseEntityRecipes);
		assertNotNull(responseEntityRecipes.get(0));
		assertNotNull(responseEntityRecipes.get(0).getRecipeId());
	}

	/**
	 * Negative test case for {@link FoodRecipeServicel#getAllRecipes()}. Test case
	 * to check if list returns empty throw exception
	 * {@link NoRecipeFoundException}. {@link NoRecipeFoundException}
	 * 
	 */
	@Test(expected = NoRecipeFoundException.class)
	public void testGetAllRecipes_returnEmptyList_throwException() {

		Mockito.when(foodRecipeRepository.findAll()).thenReturn(new ArrayList<>());

		List<RecipesEntity> responseEntityRecipes = foodRecipeService.getAllRecipes();

		assertNotNull(responseEntityRecipes);
	}

	/**
	 * Positive test case is for
	 * {@link FoodRecipeService#getAllRecipesByCategory(String)} It checks the for
	 * given category matches with database rows and if matches return list other
	 * wise throw exception.
	 * 
	 * The test case asserts the return response using Assertions.
	 */
	@Test
	public void testGetAllRecipesByCategory() {

		createRecipeCall();
		Mockito.when(foodRecipeRepository.findAllByRecipeCategoryIgnoreCase(anyString()))
				.thenReturn(CommonUtil.getListOfRecipesEntities());

		List<RecipesEntity> responseEntityRecipes = foodRecipeService
				.getListOfRecipesByCategory(CommonUtil.getRecipesEntity().getRecipeCategory());

		assertNotNull(responseEntityRecipes);
		assertNotNull(responseEntityRecipes.get(0));
		assertNotNull(responseEntityRecipes.get(0).getRecipeId());
	}

	/**
	 * Positive test case for
	 * {@link FoodRecipeService#checkRecipeCategoryByRecipeName(String)} It checks
	 * the given recipeName with the category if the category is valid then return
	 * string.
	 * 
	 */
	@Test
	public void testCheckRecipeCategoryByRecipeName() {

		createRecipeCall();
		Mockito.when(foodRecipeRepository.findAllByRecipeNameIgnoreCase(anyString()))
				.thenReturn(CommonUtil.getListOfRecipesEntities());

		boolean recipeCategory = foodRecipeService
				.checkRecipeCategoryByRecipeName(CommonUtil.getRecipesEntity().getRecipeName());

		assertTrue(recipeCategory);
	}

	/**
	 * Negative test case for
	 * {@link FoodRecipeService#checkRecipeCategoryByRecipeName(String)}. It checks
	 * if given recipeName is not exist then throw exception.
	 * 
	 */
	@Test(expected = NoRecipeFoundException.class)
	public void testCheckRecipeCategoryByRecipeName_returnEmptyList_throwException() {

		Mockito.when(foodRecipeRepository.findAllByRecipeNameIgnoreCase(anyString())).thenReturn(new ArrayList());

		boolean recipeCategory = foodRecipeService
				.checkRecipeCategoryByRecipeName(CommonUtil.getRecipesEntity().getRecipeName());

		assertNotNull(recipeCategory);
	}

	/**
	 * Positive test case is for
	 * {@link FoodRecipeService#getRecipeByInstructionNotIngredient(String, String)}
	 * It checks the not contain given ingredient and contain given instruction
	 * input into rows and return list of recipes if matching rows found.
	 * 
	 */
	@Test
	public void testGetRecipeByInstructionNotIngredient() {

		createRecipeCall();
		Mockito.when(foodRecipeRepository.findAllByInstructionNotIngredient(anyString(), anyString()))
				.thenReturn(CommonUtil.getListOfRecipesEntities());

		List<RecipesEntity> listOfRecipeEntityResponseDtos = foodRecipeService.getRecipeByInstructionNotIngredient(
				CommonUtil.getRecipesEntity().getIngredients(), CommonUtil.getRecipesEntity().getInstructions());

		assertNotNull(listOfRecipeEntityResponseDtos);
		assertNotNull(listOfRecipeEntityResponseDtos.get(0));
		assertNotNull(listOfRecipeEntityResponseDtos.get(0).getRecipeId());
	}

	/**
	 * Negative test case is for
	 * {@link FoodRecipeService#getRecipeByInstructionNotIngredient(String, String)}
	 * If there is no rows into database which not contain given ingredient and
	 * contain instruction. instruction.
	 */
	@Test(expected = NoRecipeFoundException.class)
	public void testGetRecipeByInstructionNotIngredient_noDataFound_throwException() {

		Mockito.when(foodRecipeRepository.findAllByInstructionNotIngredient(anyString(), anyString()))
				.thenReturn(new ArrayList());

		List<RecipesEntity> listRecipesResponseDtos = foodRecipeService.getRecipeByInstructionNotIngredient(
				CommonUtil.mockRecipeRequestDto().getIngredients(),
				CommonUtil.mockRecipeRequestDto().getInstructions());
		assertNotNull(listRecipesResponseDtos);
	}

	/**
	 * Positive test case is for
	 * {@link FoodRecipeService#getRecipeByIngredientAndServes(int, String)}.
	 * 
	 * It checks the given ingredient and noOfServes input into database and returns
	 * the corresponding matching rows from database.
	 * 
	 */
	@Test
	public void testGetRecipeByIngredientAndServes() {

		createRecipeCall();
		Mockito.when(foodRecipeRepository.findAllByIngredientAndServes(anyInt(), anyString()))
				.thenReturn(CommonUtil.getListOfRecipesEntities());

		List<RecipesEntity> listOfRecipesResponseDtos = foodRecipeService.getRecipeByIngredientAndServes(
				CommonUtil.getRecipesEntity().getNoOfServings(), CommonUtil.getRecipesEntity().getIngredients());

		assertNotNull(listOfRecipesResponseDtos);
		assertNotNull(listOfRecipesResponseDtos.get(0));
		assertNotNull(listOfRecipesResponseDtos.get(0).getRecipeId());
	}

	/**
	 * Negative test case is for
	 * {@link FoodRecipeService#getRecipeByIngredientAndServes(int, String)} It
	 * checks the given ingredient and noOfServes input into rows and returns the
	 * corresponding matching rows from database.
	 * 
	 */
	@Test(expected = NoRecipeFoundException.class)
	public void testGetRecipeByIngredientAndServes_noDataFound_throwException() {

		Mockito.when(foodRecipeRepository.findAllByIngredientAndServes(anyInt(), anyString()))
				.thenReturn(new ArrayList<>());

		List<RecipesEntity> listRecipesResponseDtos = foodRecipeService.getRecipeByIngredientAndServes(
				CommonUtil.mockRecipeRequestDto().getNoOfServings(),
				CommonUtil.mockRecipeRequestDto().getIngredients());

		assertNotNull(listRecipesResponseDtos);
	}

	/**
	 * Positive test case is for
	 * {@link FoodRecipeService#getAllRecipesByIngredient(String)} It checks the
	 * given ingredient input into database and returns the corresponding matching
	 * rows from database.
	 * 
	 */
	@Test
	public void testGetAllRecipesByIngredient() {

		createRecipeCall();
		Mockito.when(foodRecipeRepository.findAllByIngredientsIgnoreCase(anyString()))
				.thenReturn(CommonUtil.getListOfRecipesEntities());

		List<RecipesEntity> listOfRecipesResponseDtos = foodRecipeService
				.getAllRecipesByIngredient(CommonUtil.getRecipesEntity().getIngredients());

		assertNotNull(listOfRecipesResponseDtos);
		assertNotNull(listOfRecipesResponseDtos.get(0));
		assertNotNull(listOfRecipesResponseDtos.get(0).getRecipeId());
	}

	private RecipesEntity createRecipeCall() {
		Mockito.when(foodRecipeRepository.save(any(RecipesEntity.class))).thenReturn(CommonUtil.getRecipesEntity());
		RecipesEntity responseEntity = foodRecipeService.createRecipe(CommonUtil.getRecipesEntity());
		return responseEntity;
	}

}
