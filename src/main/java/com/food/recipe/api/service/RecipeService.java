package com.food.recipe.api.service;

import com.food.recipe.api.entity.RecipeEntity;
import com.food.recipe.api.exception.CustomRecipeException;
import com.food.recipe.api.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * The below class acts as a service layer for calling the repository to access
 * and save the data into database.
 *
 * @author snehalata.arun.raut
 */
public interface RecipeService {

    /**
     * Saves the given recipe to the database.
     *
     * @param recipe the recipe to save
     * @return the saved {@link RecipeEntity}
     * @throws CustomRecipeException if an error occurs while saving the recipe
     */
    RecipeEntity createRecipe(RecipeEntity recipe);

    /**
     * Updates the recipe with the specified ID if it exists.
     *
     * @param id     the ID of the recipe to update
     * @param recipe the updated recipe data
     * @return the updated {@link RecipeEntity}
     * @throws EntityNotFoundException if the recipe does not exist
     */
    RecipeEntity updateRecipe(int id, RecipeEntity recipe);

    /**
     * Deletes the recipe with the specified ID.
     *
     * @param id the ID of the recipe
     * @return {@code true} if deletion was successful; {@code false} otherwise
     */
    boolean deleteRecipe(int id);

    /**
     * Retrieves all recipes from the database.
     *
     * @return a list of {@link RecipeEntity}; empty if none found
     */
    List<RecipeEntity> getAllRecipes();

    /**
     * Retrieves a recipe by its ID.
     *
     * @param id the ID of the recipe
     * @return an {@link Optional} containing the recipe if found, or empty otherwise
     */
    Optional<RecipeEntity> getRecipeById(int id);

    /**
     * Searches for {@link RecipeEntity} objects based on multiple optional filter criteria.
     * <p>
     * This method dynamically builds a {@link org.springframework.data.jpa.domain.Specification}
     * based on the provided parameters and queries the database using Spring Data JPA.
     *
     * @param isVegetarian       Optional filter for vegetarian recipes.
     *                           - If {@code true}, returns only vegetarian recipes.
     *                           - If {@code false}, returns only non-vegetarian recipes.
     *                           - If {@code null}, includes both.
     * @param servings           Optional filter for the number of servings.
     *                           - If specified, returns only recipes that exactly match this value.
     *                           - If {@code null}, this filter is ignored.
     * @param includeIngredients Optional list of ingredient names that must be included in the recipe.
     *                           - Each ingredient in the list must be present in the recipe's ingredients.
     *                           - If {@code null} or empty, this filter is ignored.
     * @param excludeIngredients Optional list of ingredient names that must be excluded from the recipe.
     *                           - Each ingredient in the list must not be present in the recipe's ingredients.
     *                           - If {@code null} or empty, this filter is ignored.
     * @param instructionText    Optional text to search for in the recipe's instructions.
     *                           - Performs a case-insensitive substring search.
     *                           - If {@code null} or blank, this filter is ignored.
     * @return A list of {@link RecipeEntity} objects that match all the specified filters.
     * Returns an empty list if no matches are found.
     */
    List<RecipeEntity> searchRecipes(Boolean isVegetarian,
                                     int servings,
                                     List<String> includeIngredients,
                                     List<String> excludeIngredients,
                                     String instructionText);

}
