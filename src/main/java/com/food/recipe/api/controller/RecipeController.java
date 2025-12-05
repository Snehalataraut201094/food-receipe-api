package com.food.recipe.api.controller;

import com.food.recipe.api.model.RecipeRequestDto;
import com.food.recipe.api.model.RecipeSearchFilterRequest;
import com.food.recipe.api.model.RecipesResponse;
import com.food.recipe.api.process.RecipeProcess;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * The below controller class handles the incoming HTTP request and delegates it to process layer method.
 *
 * @author snehalata.arun.raut
 */
@RestController
@RequestMapping("/api/v1/recipes")
@Slf4j
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeProcess recipeProcess;

    @PostMapping
    public ResponseEntity<RecipesResponse> createRecipe(@Valid @RequestBody RecipeRequestDto requestDto) {
        var createdRecipe = recipeProcess.createRecipe(requestDto);
        log.debug("Created recipe: {}", createdRecipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipesResponse> updateRecipe(@PathVariable int id,
                                                        @Valid @RequestBody RecipeRequestDto requestDto) {

        var updatedRecipe = recipeProcess.updateRecipe(id, requestDto);
        log.debug("Updated recipe with ID :{}, {}", updatedRecipe, id);
        return ResponseEntity.ok(updatedRecipe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable int id) {

        boolean isRowDeleted = recipeProcess.deleteRecipe(id);
        log.debug("Deleted status for id {}: {}", id, isRowDeleted);
        return isRowDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<RecipesResponse>> getAllRecipes() {
        var recipes = recipeProcess.getAllRecipes();
        log.debug("List of recipes :{}", recipes);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipesResponse> getRecipeById(@PathVariable @Min(1) int id) {
        var recipe = recipeProcess.getRecipeById(id);
        log.debug("Fetching recipe by id: {} and corresponding entity :{}", id, recipe);
        return ResponseEntity.ok(recipe);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RecipesResponse>> searchRecipes(@ModelAttribute RecipeSearchFilterRequest request) {
        var filtered = recipeProcess.searchRecipes(request.isVegetarian(), request.servings(),
                request.includeIngredients(), request.excludeIngredients(), request.instructionText());

        log.debug("Filtered recipes: {}", filtered);
        return CollectionUtils.isEmpty(filtered) ? ResponseEntity.notFound().build() : ResponseEntity.ok(filtered);
    }
}
