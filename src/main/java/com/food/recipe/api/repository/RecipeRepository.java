package com.food.recipe.api.repository;

import com.food.recipe.api.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The below class acts as a repository for saving and accessing the data from database.
 *
 * @author snehalata.arun.raut
 */
public interface RecipeRepository extends JpaRepository<RecipeEntity, Integer>, JpaSpecificationExecutor<RecipeEntity> {

}
