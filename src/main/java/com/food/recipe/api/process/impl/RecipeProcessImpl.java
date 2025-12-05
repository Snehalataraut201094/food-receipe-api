package com.food.recipe.api.process.impl;

import com.food.recipe.api.entity.RecipeEntity;
import com.food.recipe.api.exception.EntityNotFoundException;
import com.food.recipe.api.exception.NoRecipesFoundException;
import com.food.recipe.api.exception.RecipeNotFoundException;
import com.food.recipe.api.mapper.RecipeRequestMapper;
import com.food.recipe.api.mapper.RecipeResponseMapper;
import com.food.recipe.api.model.RecipeRequestDto;
import com.food.recipe.api.model.RecipesResponse;
import com.food.recipe.api.process.RecipeProcess;
import com.food.recipe.api.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

/**
 * Handles recipe-related business logic for CRUD operations.
 *
 * @author snehalata.arun.raut
 */
@Component
@Slf4j
public class RecipeProcessImpl implements RecipeProcess {

    private final RecipeService recipeService;

    public RecipeProcessImpl(RecipeService recipeService) {
        super();
        this.recipeService = recipeService;
    }

    @Override
    public RecipesResponse createRecipe(RecipeRequestDto requestDto) {

        var recipeEntity = createRecipeEntity(requestDto);
        log.debug("Persisting new recipe: {}", recipeEntity);

        var serviceRecipeEntity = recipeService.createRecipe(recipeEntity);

        if (serviceRecipeEntity == null) {
            log.warn("Entity returned from Service is null.");
            throw new RecipeNotFoundException("The recipeEntity from service layer is null or empty.");
        }
        return createResponseDto(serviceRecipeEntity);
    }

    @Override
    public RecipesResponse updateRecipe(int id, RecipeRequestDto requestDto) {

        var entityToUpdate = createRecipeEntity(requestDto);
        log.debug("Updating existing recipe: {}", entityToUpdate);

        var updatedEntity = recipeService.updateRecipe(id, entityToUpdate);

        if (updatedEntity == null) {
            log.warn("Entity returned from Service is null while update operation.");
            throw new RecipeNotFoundException("The updatedEntity from service layer is null.");
        }
        return createResponseDto(updatedEntity);
    }

    @Override
    public boolean deleteRecipe(int id) {
        return recipeService.deleteRecipe(id);
    }

    @Override
    public List<RecipesResponse> getAllRecipes() {

		List<RecipeEntity> recipes = recipeService.getAllRecipes();
		log.debug("Retrieved recipes from the service layer :{}", recipes);

		return Optional.ofNullable(recipes)
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream()
                        .map(RecipeResponseMapper::mapToResponseDto)
                        .toList())
                .orElseThrow(() -> new NoRecipesFoundException("No recipes found"));
    }

    @Override
    public RecipesResponse getRecipeById(int id) {

        var recipe = recipeService.getRecipeById(id);
		log.debug("Retrieved recipe:{} for ID:{} from the service layer.", recipe, id);

        return recipe
                .filter(recipeEntity -> !ObjectUtils.isEmpty(recipe))
                .map(RecipeResponseMapper::mapToResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("Record not found for given ID. " + id));
    }

    @Override
    public List<RecipesResponse> searchRecipes(Boolean isVegetarian,
                                               int servings,
                                               List<String> includeIngredients,
                                               List<String> excludeIngredients,
                                               String instructionText) {

		List<RecipesResponse> filteredRecipes = recipeService.searchRecipes(isVegetarian, servings,
						includeIngredients, excludeIngredients, instructionText)
				.stream()
				.map(RecipeResponseMapper::mapToResponseDto)
				.toList();
		log.debug("Retrieved filtered recipes:{} from the service layer.",filteredRecipes);

		return filteredRecipes;

	}

    private RecipeEntity createRecipeEntity(RecipeRequestDto requestDto) {
        return RecipeRequestMapper.createRecipeEntity(requestDto);
    }

    private RecipesResponse createResponseDto(RecipeEntity recipeEntity) {
        return RecipeResponseMapper.mapToResponseDto(recipeEntity);
    }
}
