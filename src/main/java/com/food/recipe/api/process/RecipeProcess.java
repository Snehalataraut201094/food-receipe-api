package com.food.recipe.api.process;

import com.food.recipe.api.exception.EntityNotFoundException;
import com.food.recipe.api.model.RecipeRequestDto;
import com.food.recipe.api.model.RecipesResponse;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Interface defining business operations for food recipes.
 * Implementations contain the business logic for handling recipe data.
 *
 * @author snehalata.arun.raut
 */
public interface RecipeProcess {

    /**
     * Creates a new recipe entity in the database
     *
     * @param requestDto the data for the new recipe
     * @return the created recipe as {@link RecipesResponse}
     */
    RecipesResponse createRecipe(@Valid RecipeRequestDto requestDto);

    /**
     * Updates an existing recipe identified by the given ID.
     *
     * @param id         the ID of the recipe to update
     * @param requestDto the recipe data to update
     * @return the updated recipes as {@link RecipesResponse}
     * @throws EntityNotFoundException if the recipe with the given ID does not exist
     */
    RecipesResponse updateRecipe(int id, RecipeRequestDto requestDto);


    /**
     * Deletes an existing recipe identified by the given ID.
     *
     * @param id the ID of the recipe to delete
     * @return true if the recipe was found and deleted, false otherwise
     * @throws EntityNotFoundException if the recipe with the given ID does not exist
     */
    boolean deleteRecipe(int id);

    /**
     * Retrieves all recipes from the database.
     *
     * @return a list of {@link RecipesResponse}; never null but may be empty
     */
    List<RecipesResponse> getAllRecipes();

    /**
     * Retrieves a recipe by its ID.
     *
     * @param id the ID of the recipe
     * @return the recipe as {@link RecipesResponse}
     * @throws EntityNotFoundException if no recipe is found with the given ID
     */
    RecipesResponse getRecipeById(int id);


    /**
     * Searches for recipes based on provided optional filters.
     *
     * @param isVegetarian       if true, only vegetarian recipes are included; if false, only non-vegetarian; if null, all
     * @param servings           exact number of servings; if null, any servings
     * @param includeIngredients list of ingredients to include; if null or empty, no filter applied
     * @param excludeIngredients list of ingredients to exclude; if null or empty, no filter applied
     * @param instructionText    keyword/phrase in instructions; if null or empty, no filter applied
     * @return list of matching recipes; never null but may be empty
     */
    List<RecipesResponse> searchRecipes(
            Boolean isVegetarian,
            int servings,
            List<String> includeIngredients,
            List<String> excludeIngredients,
            String instructionText);
}
