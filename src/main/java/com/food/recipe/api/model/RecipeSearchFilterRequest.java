package com.food.recipe.api.model;

import java.util.List;

public record RecipeSearchFilterRequest(
         Boolean isVegetarian,
         Integer servings,
         List<String> includeIngredients,
         List<String> excludeIngredients,
         String instructionText
){}
