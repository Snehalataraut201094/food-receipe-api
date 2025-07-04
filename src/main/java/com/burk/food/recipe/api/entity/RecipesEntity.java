/**
 * 
 */
package com.burk.food.recipe.api.entity;

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
@Entity
@Table(name = "recipe_details")
@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecipesEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 717768084918110723L;

	@Column(name = "recipe_Id")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int recipeId;

	@Column(name = "recipe_Name",nullable=false)
	private String recipeName;

	@Column(name = "recipe_Category", nullable = false)
	@Size(min = 3, max = 20)
	private String recipeCategory;

	@Column(name = "no_Of_Servings",nullable=false)
	private int noOfServings;

	@Column(name = "ingredients")
	private String ingredients;

	@Column(name = "instructions")
	private String instructions;
}
