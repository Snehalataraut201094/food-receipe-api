package com.food.recipe.api.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RecipeNotFoundException extends RuntimeException {

    private String message;

    public RecipeNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
