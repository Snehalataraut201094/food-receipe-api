package com.food.recipe.api.service.impl;

import com.food.recipe.api.entity.RecipeEntity;
import com.food.recipe.api.exception.CustomRecipeException;
import com.food.recipe.api.exception.EntityNotFoundException;
import com.food.recipe.api.process.impl.RecipesSpecificationBuilder;
import com.food.recipe.api.repository.RecipeRepository;
import com.food.recipe.api.service.RecipeService;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing Recipe entities.
 * Acts as a bridge between controllers and data persistence layer.
 *
 * @author snehalata.arun.raut
 */
@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        super();
        this.recipeRepository = recipeRepository;
    }

    @Override
    public RecipeEntity createRecipe(RecipeEntity recipe) {
        try {
            var savedRecipe = recipeRepository.save(recipe);
            log.debug("Saved recipe into DB is : {}", savedRecipe);
            return savedRecipe;
        } catch (DataAccessException | PersistenceException ex) {
            log.error("Failed to persist recipe entity to the database", ex);
            throw new CustomRecipeException("Could not save recipe to the database", ex);
        }
    }

    @Override
    public RecipeEntity updateRecipe(int id, RecipeEntity recipeEntity) {
        log.info("Starting to update recipe entity.");

        return recipeRepository.findById(id)
                .map(oldEntity -> {
                    oldEntity.setName(recipeEntity.getName());
                    oldEntity.setIsVegetarian(recipeEntity.getIsVegetarian());
                    oldEntity.setIngredients(recipeEntity.getIngredients());
                    oldEntity.setInstructions(recipeEntity.getInstructions());

                    RecipeEntity updatedRecipe = recipeRepository.save(oldEntity);
                    log.debug("Updated recipe: {}", updatedRecipe);
                    return updatedRecipe;
                })
                .orElseThrow(() -> new EntityNotFoundException("Recipe with ID " + id + " not found"));
    }

    @Override
    public boolean deleteRecipe(int id) {
        log.debug("The id to delete from database :{}", id);

        return recipeRepository.findById(id)
                .map(recipeEntity -> {
                    recipeRepository.deleteById(id);
                    return true;
                }).orElseThrow(() -> new EntityNotFoundException("Recipe with ID " + id + " not found"));
    }

    @Override
    public List<RecipeEntity> getAllRecipes() {
        log.info("Retrieving all recipes from the database.");
        return recipeRepository.findAll();
    }

    @Override
    public Optional<RecipeEntity> getRecipeById(int id) {
        log.debug("Retrieving Recipe for ID :{}", id);
        return recipeRepository.findById((id));
    }

    @Override
    public List<RecipeEntity> searchRecipes(Boolean isVegetarian,
                                            int servings,
                                            List<String> includeIngredients,
                                            List<String> excludeIngredients,
                                            String instructionText) {

        log.debug("Searching recipes with filters - Vegetarian: {}, Servings: {}, Includes: {}, Excludes: {}," +
                        "Instructions: {}", isVegetarian, servings, includeIngredients,
                excludeIngredients, instructionText);

        Specification<RecipeEntity> specification = RecipesSpecificationBuilder.build(isVegetarian, servings,
                includeIngredients, excludeIngredients, instructionText);

        return recipeRepository.findAll(specification);
    }
}
