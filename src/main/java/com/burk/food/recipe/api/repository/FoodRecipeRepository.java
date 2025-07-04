package com.burk.food.recipe.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.burk.food.recipe.api.entity.RecipesEntity;

/**
 * The below class acts as a repository for saving and accessing the data from
 * repository or database.
 * 
 * @author snehalata.arun.raut
 */
public interface FoodRecipeRepository extends JpaRepository<RecipesEntity, Integer> {

	/**
	 * The below method perform finding all the record which contains matching input
	 * category.
	 * 
	 * @param category of type {@link String}
	 * @return list of {@link RecipesEntity}
	 */
	public List<RecipesEntity> findAllByRecipeCategoryIgnoreCase(String category);

	/**
	 * The below method perform finding all the record which contains matching input
	 * recipeName.
	 * 
	 * @param recipeName of type {@link String}
	 * @return list of {@link RecipesEntity}
	 */
	public List<RecipesEntity> findAllByRecipeNameIgnoreCase(String recipeName);

	/**
	 * The below method perform finding all the record which contains matching input
	 * ingredient.
	 * 
	 * @param ingredient of type {@link String}
	 * @return list of {@link RecipesEntity}
	 */
	public List<RecipesEntity> findAllByIngredientsIgnoreCase(String ingredient);

	/**
	 * The below method perform finding all the record which contains input
	 * instruction but not contains ingredient.
	 * 
	 * @param ingredient  of type {@link String}
	 * @param instruction of type {@link String}
	 * @return list of {@link RecipesEntity}
	 */
	@Query(value = "SELECT * FROM Recipe_Details  WHERE UPPER(ingredients) != UPPER(:ingredient) and UPPER(instructions) = UPPER(:instruction)", nativeQuery = true)
	public List<RecipesEntity> findAllByInstructionNotIngredient(@Param("ingredient") String ingredient,
			@Param("instruction") String instruction);

	/**
	 * The below method perform finding all the record which contains input
	 * ingredient and numberOfServes.
	 * 
	 * @param ingredient     of type {@link String}
	 * @param numberOfServes of type int
	 * @return list of {@link RecipesEntity}
	 */
	@Query(value = "SELECT * FROM Recipe_Details  WHERE no_of_servings = :numberOfServes and UPPER(ingredients) = UPPER(:ingredient)", nativeQuery = true)
	public List<RecipesEntity> findAllByIngredientAndServes(@Param("numberOfServes") int numberOfServes,
			@Param("ingredient") String ingredient);
}
