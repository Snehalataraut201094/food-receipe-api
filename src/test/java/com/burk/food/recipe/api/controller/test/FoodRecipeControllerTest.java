package com.burk.food.recipe.api.controller.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.burk.food.recipe.api.controller.FoodRecipeController;
import com.burk.food.recipe.api.dto.request.RecipesRequestDto;
import com.burk.food.recipe.api.dto.request.RecipesUpdateRequestDto;
import com.burk.food.recipe.api.dto.response.RecipesResponseDto;
import com.burk.food.recipe.api.process.FoodRecipeProcess;
import com.burk.food.recipe.api.util.RecipeConstant;
import com.burt.food.recipe.api.util.CommonUtil;

/**
 * The below class is test case for {@link FoodRecipeController}
 * 
 * @author snehalata.arun.raut
 *
 */
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class FoodRecipeControllerTest {

	@Mock
	private FoodRecipeProcess foodRecipeProcess;

	@InjectMocks
	private FoodRecipeController foodRecipeController;

	@Before
	public void setUp() {
		foodRecipeProcess = Mockito.mock(FoodRecipeProcess.class);
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Positive test case for create operation for recipe
	 */
	@Test
	public void testCreateRecipes() {
		RecipesResponseDto recipesRequestDto = CommonUtil.getRecipesResponse();

		Mockito.when(foodRecipeProcess.createRecipe(any(RecipesRequestDto.class))).thenReturn(recipesRequestDto);

		ResponseEntity<RecipesResponseDto> recResponseEntity = foodRecipeController
				.createRecipes(CommonUtil.mockRecipeRequestDto());
		assertNotNull(recResponseEntity);
		assertNotNull(recResponseEntity.getBody());
		assertThat(recResponseEntity.getBody()).isNotNull();
		assertThat(recResponseEntity.getBody().getRecipeName()).isEqualTo(recipesRequestDto.getRecipeName());
	}

	/**
	 * Positive test case for update operation.
	 */
	@Test
	public void testUpdateRecipes() {

		RecipesResponseDto recipesResponseDto = CommonUtil.getRecipesResponse();

		Mockito.when(foodRecipeProcess.updateRecipe(any(RecipesUpdateRequestDto.class))).thenReturn(recipesResponseDto);

		ResponseEntity<RecipesResponseDto> recResponseEntity = foodRecipeController
				.updateRecipe(CommonUtil.mockUpdateRecipeRequest());
		assertNotNull(recResponseEntity);
		assertNotNull(recResponseEntity.getBody());
		assertThat(recResponseEntity.getBody()).isNotNull();
	}

	/**
	 * Positive test case for delete operation.
	 */
	@Test
	public void testDeleteRecipes() {

		Mockito.when(foodRecipeProcess.deleteRecipe(anyInt())).thenReturn(true);

		ResponseEntity<String> recResponseEntity = foodRecipeController
				.deleteRecipe(CommonUtil.mockUpdateRecipeRequest().getRecipeId());
		assertNotNull(recResponseEntity);
		assertNotNull(recResponseEntity.getBody());
		assertThat(recResponseEntity.getBody()).isNotNull();
	}

	/**
	 * Positive test case for getAllRecipe operation.
	 */
	@Test
	public void testGetAllRecipe() {

		List<RecipesResponseDto> recipesResponseDto = CommonUtil.getListOfRecipesResponseDto();

		Mockito.when(foodRecipeProcess.getAllRecipes()).thenReturn(recipesResponseDto);

		ResponseEntity<List<RecipesResponseDto>> recResponseEntity = foodRecipeController.getAllRecipes();
		assertNotNull(recResponseEntity);
		assertNotNull(recResponseEntity.getBody());
		assertThat(recResponseEntity.getStatusCode().is2xxSuccessful());
	}

	/**
	 * Positive test case for getAllRecipeByCategory operation.
	 */
	@Test
	public void testGetAllRecipeByCategory() {

		List<RecipesResponseDto> recipesResponseDto = CommonUtil.getListOfRecipesResponseDto();

		Mockito.when(foodRecipeProcess.getAllRecipesByCategory(anyString())).thenReturn(recipesResponseDto);

		ResponseEntity<List<RecipesResponseDto>> recResponseEntity = foodRecipeController
				.getAllRecipesByCategory(CommonUtil.mockUpdateRecipeRequest().getRecipeCategory());
		assertNotNull(recResponseEntity);
		assertNotNull(recResponseEntity.getBody());
		assertThat(recResponseEntity.getStatusCode().is2xxSuccessful());
	}

	/**
	 * Positive test case for getAllRecipeByName operation.
	 */
	@Test
	public void testCheckRecipeCategoryByRecipeName() {

		Mockito.when(foodRecipeProcess.checkRecipeCategoryByRecipeName(anyString()))
				.thenReturn(RecipeConstant.DISH_IS_VEGETERIAN);

		ResponseEntity<String> recResponseEntity = foodRecipeController
				.checkRecipeCategoryByRecipeName(CommonUtil.mockUpdateRecipeRequest().getRecipeName());
		assertNotNull(recResponseEntity);
		assertNotNull(recResponseEntity.getBody());
		assertThat(recResponseEntity.getStatusCode().is2xxSuccessful());
	}

	/**
	 * Positive test case for getRecipeByIngredientAndServes operation.
	 */
	@Test
	public void testGetRecipeByIngredientAndServes() {

		List<RecipesResponseDto> recipesResponseDto = CommonUtil.getListOfRecipesResponseDto();

		Mockito.when(foodRecipeProcess.getRecipeByIngredientAndServes(anyInt(), anyString()))
				.thenReturn(recipesResponseDto);

		ResponseEntity<List<RecipesResponseDto>> recResponseEntity = foodRecipeController
				.getRecipeByIngredientAndServes(CommonUtil.mockUpdateRecipeRequest().getNoOfServings(),
						CommonUtil.mockUpdateRecipeRequest().getIngredients());
		assertNotNull(recResponseEntity);
		assertNotNull(recResponseEntity.getBody());
		assertThat(recResponseEntity.getStatusCode().is2xxSuccessful());
	}

	/**
	 * Positive test case for getRecipeByInstructionNotIngredient operation.
	 */
	@Test
	public void testGetRecipeByInstructionNotIngredient() {

		List<RecipesResponseDto> recipesResponseDto = CommonUtil.getListOfRecipesResponseDto();

		Mockito.when(foodRecipeProcess.getRecipeByInstructionNotIngredient(anyString(), anyString()))
				.thenReturn(recipesResponseDto);

		ResponseEntity<List<RecipesResponseDto>> recResponseEntity = foodRecipeController
				.getRecipeByInstructionNotIngredient(CommonUtil.mockUpdateRecipeRequest().getInstructions(),
						CommonUtil.mockUpdateRecipeRequest().getIngredients());
		assertNotNull(recResponseEntity);
		assertNotNull(recResponseEntity.getBody());
		assertThat(recResponseEntity.getStatusCode().is2xxSuccessful());
	}

	/**
	 * Positive test case for getAllRecipeByIngredient operation.
	 */
	@Test
	public void testGetAllRecipesByIngredient() {

		List<RecipesResponseDto> recipesResponseDto = CommonUtil.getListOfRecipesResponseDto();

		Mockito.when(foodRecipeProcess.getAllRecipesByIngredient(anyString())).thenReturn(recipesResponseDto);

		ResponseEntity<List<RecipesResponseDto>> recResponseEntity = foodRecipeController
				.getAllRecipesByIngredient(CommonUtil.mockUpdateRecipeRequest().getIngredients());
		assertNotNull(recResponseEntity);
		assertNotNull(recResponseEntity.getBody());
		assertThat(recResponseEntity.getStatusCode().is2xxSuccessful());
	}
}
