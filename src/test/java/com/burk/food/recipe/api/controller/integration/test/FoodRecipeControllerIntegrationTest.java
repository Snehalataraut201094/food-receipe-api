package com.burk.food.recipe.api.controller.integration.test;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.burk.food.recipe.api.controller.FoodRecipeController;
import com.burk.food.recipe.api.controller.integration.test.util.IntegrationCommonUtil;
import com.burk.food.recipe.api.dto.request.RecipesRequestDto;
import com.burk.food.recipe.api.dto.request.RecipesUpdateRequestDto;
import com.burk.food.recipe.api.dto.response.RecipesResponseDto;
import com.burk.food.recipe.api.exception.NoRecipeFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The below IntegrationTest class for {@link FoodRecipeController}
 * 
 * @author snehalata.arun.raut
 */
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FoodRecipeControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@LocalServerPort
	private int port;

	@BeforeEach
	void setup() {
	}

	/**
	 * Positive test case for createRecipe operation
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void createRecipe_thenReturnSavedRecipe() throws JsonProcessingException, Exception {

		ResultActions createRecipeCall = createRecipecall();
		String content = createRecipeCall.andReturn().getResponse().getContentAsString();
		RecipesResponseDto recipesResponseDto = objectMapper.readValue(content, RecipesResponseDto.class);

		createRecipeCall.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$.recipeName",
						CoreMatchers.is(recipesResponseDto.getRecipeName())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.recipeCategory",
						CoreMatchers.is(recipesResponseDto.getRecipeCategory())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.noOfServings",
						CoreMatchers.is(recipesResponseDto.getNoOfServings())));
	}

	/**
	 * Negative test case for updateRecipe operation . If recipeId not found for
	 * update.
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void updateRecipe_idNotPresent_thenReturnException() throws JsonProcessingException, Exception {
		RecipesUpdateRequestDto recipesUpdateRequestDto = new RecipesUpdateRequestDto();
		recipesUpdateRequestDto.setRecipeId(222);
		recipesUpdateRequestDto.setIngredients("onion");
		recipesUpdateRequestDto.setInstructions("cooker");
		recipesUpdateRequestDto.setNoOfServings(3);
		recipesUpdateRequestDto.setRecipeCategory("veg");
		recipesUpdateRequestDto.setRecipeName("Khichadi");

		ResultActions response = mockMvc.perform(
				MockMvcRequestBuilders.put(createUrl("/food-recipe-api/v1/updateRecipe"), recipesUpdateRequestDto)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(recipesUpdateRequestDto)));

		response.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	/**
	 * Positive test case for update the recipe for found recipeId.
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void updateRecipe_ifIdPresent_thenReturnResponseDto() throws JsonProcessingException, Exception {

		ResultActions responseOfgetRecipesData = getRecipesData();
		String content = responseOfgetRecipesData.andReturn().getResponse().getContentAsString();
		List<RecipesResponseDto> listOfResponsedto = Arrays
				.asList(objectMapper.readValue(content, RecipesResponseDto[].class));

		RecipesUpdateRequestDto recipeUpdateRequestDto = IntegrationCommonUtil
				.mapResponseDtoToRequestDtoForUpdate(listOfResponsedto.get(0).getRecipeId());

		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.put(createUrl("/food-recipe-api/v1/updateRecipe")).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(recipeUpdateRequestDto)));

		response.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Negative test case for deleteRecipe operation . If recipeId not found for
	 * delete.
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void deleteRecipeById_idNotPresent_throwNotFoundException() throws JsonProcessingException, Exception {

		RecipesUpdateRequestDto recipesRequestDto = new RecipesUpdateRequestDto();
		recipesRequestDto.setRecipeId(0000);
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.delete(createUrl("/food-recipe-api/v1/deleteRecipe/{recipeId}"), recipesRequestDto.getRecipeId())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(recipesRequestDto)));

		response.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	/**
	 * Positive test case for deleteRecipe operation for found recipe.
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void deleteRecipeById_idPresent_deleteRecipe() throws JsonProcessingException, Exception {

		ResultActions responseOfgetRecipesData = getRecipesData();
		String content = responseOfgetRecipesData.andReturn().getResponse().getContentAsString();
		List<RecipesResponseDto> listOfResponsedto = Arrays
				.asList(objectMapper.readValue(content, RecipesResponseDto[].class));

		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.delete(createUrl("/food-recipe-api/v1/deleteRecipe/{recipeId}"),
						listOfResponsedto.get(0).getRecipeId())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		response.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Positive test case for deleteRecipe operation for found recipe.
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void getAllRecipe_positive() throws JsonProcessingException, Exception {

		ResultActions response = getRecipesData();
		response.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Positive test case for getAllRecipeByCategory operation. If category found
	 * return the list of recipe.
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void getAllRecipe_byCategory() throws JsonProcessingException, Exception {

		ResultActions responseOfgetRecipesData = getRecipesData();
		String content = responseOfgetRecipesData.andReturn().getResponse().getContentAsString();
		List<RecipesResponseDto> listOfResponsedto = Arrays
				.asList(objectMapper.readValue(content, RecipesResponseDto[].class));

		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.get(createUrl("/food-recipe-api/v1/getAllRecipe/category/{recipeCategory}"),
						listOfResponsedto.get(0).getRecipeCategory())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		response.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Negative test case for getAllRecipeByCategory operation. If data not found in
	 * database throw exception {@link NoRecipeFoundException}
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void getAllRecipe_noCategoryFound_thenThrowException() throws JsonProcessingException, Exception {

		String category = "Vegetable";

		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.get(createUrl("/food-recipe-api/v1/getAllRecipe/category/{recipeCategory}"), category)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		response.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	/**
	 * Positive test case for checkCategoryByRecipeName operation.If recipeName
	 * found return data and check category if matches return string value.
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void checkCategoryOfRecipe_byRecipeName() throws JsonProcessingException, Exception {

		ResultActions responseOfgetRecipesData = getRecipesData();
		String content = responseOfgetRecipesData.andReturn().getResponse().getContentAsString();
		List<RecipesResponseDto> listOfResponsedto = Arrays
				.asList(objectMapper.readValue(content, RecipesResponseDto[].class));

		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.get(createUrl("/food-recipe-api/v1/checkRecipe/recipeName/{recipeName}"),
						listOfResponsedto.get(0).getRecipeName())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		response.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Negative test case for checkCategoryByRecipeName operation.If recipeName not
	 * found then throw {@link NoRecipeFoundException}
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void checkCategoryOfRecipe_byRecipeName_notFound_throwNotFoundException()
			throws JsonProcessingException, Exception {

		String recipeName = "salmon";

		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.get(createUrl("/food-recipe-api/v1/checkRecipe/recipeName/{recipeName}"), recipeName)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		response.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	/**
	 * Positive test case for getAllRecipeByIngredientAndInstruction operation.If
	 * matching data found for given ingredient and instruction then return data.
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void getRecipeBy_ingredientAndInstruction_ifFound_returnResponseDto()
			throws JsonProcessingException, Exception {

		ResultActions responseOfgetRecipesData = getRecipesData();
		String content = responseOfgetRecipesData.andReturn().getResponse().getContentAsString();
		List<RecipesResponseDto> listOfResponsedto = Arrays
				.asList(objectMapper.readValue(content, RecipesResponseDto[].class));

		listOfResponsedto.get(0).setIngredients("Not Applicable");
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.get(createUrl("/food-recipe-api/v1/getAllRecipe/ingredient/{ingredient}/instruction/{instruction}"),
						listOfResponsedto.get(0).getIngredients(), listOfResponsedto.get(0).getInstructions())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		response.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Negative test case for getAllRecipeByIngredientAndInstruction operation.If
	 * matching data not found for given ingredient and instruction then throw
	 * {@link NoRecipeFoundException}
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void getRecipeBy_ingredientAndInstruction_ifNotFound_throwException()
			throws JsonProcessingException, Exception {

		ResultActions responseOfgetRecipesData = getRecipesData();
		String content = responseOfgetRecipesData.andReturn().getResponse().getContentAsString();
		List<RecipesResponseDto> listOfResponsedto = Arrays
				.asList(objectMapper.readValue(content, RecipesResponseDto[].class));

		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.get(createUrl("/food-recipe-api/v1/getAllRecipe/ingredient/{ingredient}/instruction/{instruction}"),
						listOfResponsedto.get(0).getIngredients(), listOfResponsedto.get(0).getInstructions())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		response.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	/**
	 * Positive test case for getAllRecipeByIngredientAndServes operation.If
	 * matching data found for given ingredient and noOfServes then return data.
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void getRecipeBy_ingredientAndServes_ifFound_returnResponseDto() throws JsonProcessingException, Exception {

		ResultActions responseOfgetRecipesData = getRecipesData();
		String content = responseOfgetRecipesData.andReturn().getResponse().getContentAsString();
		List<RecipesResponseDto> listOfResponsedto = Arrays
				.asList(objectMapper.readValue(content, RecipesResponseDto[].class));

		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.get(createUrl(
						"/food-recipe-api/v1/getAllRecipe/numberOfServes/{numberOfServes}/ingredient/{ingredient}"),
						listOfResponsedto.get(0).getNoOfServings(), listOfResponsedto.get(0).getIngredients())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		response.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Negative test case for getAllRecipeByIngredientAndServes operation.If
	 * matching data not found for given ingredient and noOfServes then throw
	 * {@link NoRecipeFoundException}
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void getRecipeBy_ingredientAndServes_ifNotFound_throwException() throws JsonProcessingException, Exception {

		ResultActions responseOfgetRecipesData = getRecipesData();
		String content = responseOfgetRecipesData.andReturn().getResponse().getContentAsString();
		List<RecipesResponseDto> listOfResponsedto = Arrays
				.asList(objectMapper.readValue(content, RecipesResponseDto[].class));

		String ingredients = "ingredients";

		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.get(createUrl(
						"/food-recipe-api/v1/getAllRecipe/numberOfServes/{numberOfServes}/ingredient/{ingredient}"),
						listOfResponsedto.get(0).getNoOfServings(), ingredients)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		response.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	/**
	 * Positive test case for getAllRecipesByIngredient operation.If matching data
	 * found for given ingredient then return data.
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void getRecipes_byIngredient_found_returnResponse() throws JsonProcessingException, Exception {

		ResultActions responseOfgetRecipesData = getRecipesData();
		String content = responseOfgetRecipesData.andReturn().getResponse().getContentAsString();
		List<RecipesResponseDto> listOfResponsedto = Arrays
				.asList(objectMapper.readValue(content, RecipesResponseDto[].class));

		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.get(createUrl("/food-recipe-api/v1/getAllRecipe/ingredient/{ingredient}"),
						listOfResponsedto.get(0).getIngredients())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		response.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Negative test case for getAllRecipeByIngredient operation.If matching data
	 * not found for given ingredient then throw {@link NoRecipeFoundException}
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void getRecipes_byIngredient_notFound_throwException() throws JsonProcessingException, Exception {

		String ingredients = "Not Applicable";

		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.get(createUrl("/food-recipe-api/v1/getAllRecipe/ingredient/{ingredient}"), ingredients)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		response.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	private ResultActions createRecipecall() throws Exception, JsonProcessingException {
		RecipesRequestDto createRecipesRequestDto = IntegrationCommonUtil.createRecipesRequestDto();
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders
				.post(createUrl("/food-recipe-api/v1/createRecipe")).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createRecipesRequestDto)));
		return response;
	}

	private ResultActions getRecipesData() throws Exception {
		createRecipecall();
		ResultActions response = mockMvc
				.perform(MockMvcRequestBuilders.get(createUrl("/food-recipe-api/v1/getAllRecipe"))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		return response;
	}

	private String createUrl(String resourcePath) {
		return "http://localhost:" + port + resourcePath;
	}
}
