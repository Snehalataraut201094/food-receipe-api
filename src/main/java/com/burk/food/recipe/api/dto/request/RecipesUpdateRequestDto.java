package com.burk.food.recipe.api.dto.request;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The POJO class will acts as request class for update operation.
 * 
 * @author snehalata.arun.raut
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RecipesUpdateRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7278492120204506176L;

	@Min(value = 1, message = "The recipeId value should be minimum 1.")
	private int recipeId;

	@NotEmpty(message = "Recipe Name can not be null")
	@Size(min = 3, max = 200)
	private String recipeName;

	@NotEmpty(message = "Recipe Category can not be null")
	@Size(min = 3, max = 20)
	private String recipeCategory;

	@NotNull(message = "No Of Serving can not be null")
	@Positive(message = "The noOfServings should be positive value.")
	@Min(value = 1, message = "The noOfServings value should be minimum 1")
	private int noOfServings;

	private String ingredients;

	private String instructions;
}
