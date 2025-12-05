package com.food.recipe.api.util;

import com.food.recipe.api.entity.RecipeEntity;
import com.food.recipe.api.model.RecipeRequestDto;
import com.food.recipe.api.model.RecipesResponse;

import java.util.List;

public class RecipeTestUtil {

    private String instructions;

    public static RecipeRequestDto createRecipeRequestDto() {
        return RecipeRequestDto.builder()
                .recipeName("Vada Pav")
                .isVegetarian(true)
                .ingredients(buildListOfIngredients())
                .instructions(buildInstructions())
                .servings(4)
                .build();
    }

    public static RecipeRequestDto createRecipeRequestDtoForUpdate(String recipeName,
                                                                   boolean isVegetarian,
                                                                   int servings,
                                                                   List<String> ingredients,
                                                                   String instructions) {
        return RecipeRequestDto.builder()
                .recipeName(recipeName)
                .isVegetarian(isVegetarian)
                .ingredients(ingredients)
                .instructions(instructions)
                .servings(servings)
                .build();
    }

    public static RecipesResponse createRecipeResponse() {
        return RecipesResponse.builder()
                .id(1)
                .name("Vada Pav")
                .servings(4)
                .instructions(buildInstructions())
                .ingredients(buildListOfIngredients())
                .isVegetarian(true)
                .build();
    }

    public static RecipeEntity createRecipeEntity() {
        return RecipeEntity.builder()
                .name("Vada Pav")
                .isVegetarian(true)
                .ingredients(buildListOfIngredients())
                .instructions(buildInstructions())
                .servings(4)
                .build();
    }

    public static List<RecipeEntity> createRecipeEntities() {
        RecipeEntity vegRecipeEntity = RecipeEntity.builder()
                .name("Vada Pav")
                .isVegetarian(true)
                .ingredients(buildListOfIngredients())
                .instructions(buildInstructions())
                .servings(4)
                .build();

        RecipeEntity nonVegRecipeEntity = RecipeEntity.builder()
                .name("🍜 Ramen")
                .isVegetarian(false)
                .ingredients(List.of("Chicken", "Noodles", "Kimchi", "Sauce"))
                .instructions("Delicious style 🍜")
                .servings(3)
                .build();

        return List.of(vegRecipeEntity, nonVegRecipeEntity);
    }

    public static List<String> buildListOfIngredients() {
        return List.of("Chutney", "Potato", "Spices", "Pav", "Flour", "Onion", "Chilli");
    }

    public static String buildInstructions() {
        return "Boil Potato and smash it. Make bhaji of it. Then deep fry it into oil.";
    }
}
