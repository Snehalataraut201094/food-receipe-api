package com.food.recipe.api.exception;

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
public class EntityNotFoundException extends RuntimeException {

		private String message;
}
