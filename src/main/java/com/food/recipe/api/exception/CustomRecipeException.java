package com.food.recipe.api.exception;

public class CustomRecipeException extends RuntimeException {

	public CustomRecipeException(String message, Throwable cause) {
		super(message, cause);
	}
}
