/**
 * 
 */
package com.food.recipe.api.errorhandler;

import com.food.recipe.api.exception.CustomRecipeException;
import com.food.recipe.api.exception.NoRecipesFoundException;
import com.food.recipe.api.exception.RecipeNotFoundException;
import com.food.recipe.api.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Below class is the central class which handles all the exceptions.
 * 
 * @author snehalata.arun.raut
 *
 */
@Slf4j
@ControllerAdvice
public class RecipeErrorHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CustomRecipeException.class)
	public ResponseEntity<Object> handleCustomRecipeException(CustomRecipeException exception) {
		log.error("The customRecipe exception Occurred.", exception);

		Throwable cause = exception.getCause();
		if (cause instanceof DataAccessException || cause instanceof ConstraintViolationException) {
			return createErrorResponse(HttpStatus.CONFLICT, exception.getMessage());
		}
		return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
	}

	@ExceptionHandler(value = DataIntegrityViolationException.class)
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
		log.error("The data integrity exception Occurred.", exception);
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
		return new ResponseEntity<>(errorResponse, null, HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@ExceptionHandler(value ={EntityNotFoundException.class, ObjectOptimisticLockingFailureException.class})
	public ResponseEntity<Object> handleNotFoundException(RuntimeException exception) {
		log.error("The entity not found exception Occurred.", exception);
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
		return new ResponseEntity<>(errorResponse, null, HttpStatus.NOT_FOUND.value());
	}

	@ExceptionHandler(RecipeNotFoundException.class)
	public ResponseEntity<Object> handleRecipeNotFoundException(RecipeNotFoundException exception) {
		log.error("The DataNotFoundException Occurred.", exception);
		return createErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
	}

	@ExceptionHandler(NoRecipesFoundException.class)
	public ResponseEntity<Object> handleNoRecipesFoundException(NoRecipesFoundException exception) {
		log.error("The NoRecipesFoundException Occurred.", exception);
		return createErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
	}

	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		List<String> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(err -> err.getField() + ": " + err.getDefaultMessage())
				.collect(Collectors.toList());

		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), String.join(", ", errors));
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<Object> createErrorResponse(HttpStatus httpStatus, String message) {
		ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
		return new ResponseEntity<>(errorResponse, null, httpStatus.value());
	}
}
