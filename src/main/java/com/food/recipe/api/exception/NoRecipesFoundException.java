package com.food.recipe.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoRecipesFoundException extends RuntimeException {
    private String message;
}
