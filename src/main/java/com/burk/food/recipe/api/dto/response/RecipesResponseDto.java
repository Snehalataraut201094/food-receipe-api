package com.burk.food.recipe.api.dto.response;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The POJO class will acts as a persistence class will create the table at
 * runtime into the database as Recipe_Table.
 * 
 * @author snehalata.arun.raut
 *
 */
@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecipesResponseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9180201404679098852L;

	private int recipeId;

	private String recipeName;

	private String recipeCategory;

	private int noOfServings;

	private String ingredients;

	private String instructions;
}
