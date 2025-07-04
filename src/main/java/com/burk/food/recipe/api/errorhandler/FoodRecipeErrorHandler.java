/**
 * 
 */
package com.burk.food.recipe.api.errorhandler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.burk.food.recipe.api.exception.CreateRecipeException;
import com.burk.food.recipe.api.exception.NoRecipeFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Below class is the central class which handles all the exceptions.
 * 
 * @author snehalata.arun.raut
 *
 */
@Slf4j
@ControllerAdvice
public class FoodRecipeErrorHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CreateRecipeException.class)
	public ResponseEntity<Object> handleCreateRecipeException(CreateRecipeException exception) {
		log.error("The excption Occurred :{}", exception);
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NO_CONTENT.value(), exception.getMessage());
		return new ResponseEntity<Object>(errorResponse, null, HttpStatus.NO_CONTENT.value());
	}

	@ExceptionHandler(value = NoRecipeFoundException.class)
	public ResponseEntity<Object> handleCreateRecipeException(NoRecipeFoundException exception) {
		log.error("The excption Occurred :{}", exception);
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
		return new ResponseEntity<Object>(errorResponse, null, HttpStatus.NOT_FOUND.value());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> errors = new ArrayList<String>();

		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + " -> " + error.getDefaultMessage());
		}

		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + " -> " + error.getDefaultMessage());
		}

		ValidationErrorResponse apiError = new ValidationErrorResponse(HttpStatus.BAD_REQUEST, errors);
		return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);

	}

}
