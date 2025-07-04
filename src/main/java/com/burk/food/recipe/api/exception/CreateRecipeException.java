package com.burk.food.recipe.api.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CreateRecipeException extends RuntimeException {

	/**
	 * The generated serial-version uuid
	 */
	private static final long serialVersionUID = -2185030348426030402L;

	private String message;
}
