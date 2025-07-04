/**
 * 
 */
package com.burk.food.recipe.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author snehalata.arun.raut
 *
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoRecipeFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8209555732216435211L;
	
		private String message;
}
