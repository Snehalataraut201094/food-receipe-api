package com.food.recipe.api.process.impl;

import com.food.recipe.api.entity.RecipeEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.List;

public class RecipesSpecificationBuilder {

    public static Specification<RecipeEntity> build(Boolean isVegetarian,
                                                    Integer servings,
                                                    List<String> includeIngredients,
                                                    List<String> excludeIngredients,
                                                    String instructionText) {

        Specification<RecipeEntity> spec = Specification.where(null);

        if (isVegetarian != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("isVegetarian"), isVegetarian));
        }

        if (servings != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("servings"), servings));
        }

        Specification<RecipeEntity> includeIngredientSpec = buildIngredientSpecification(includeIngredients, false);
        if (includeIngredientSpec != null) {
            spec = spec.and(includeIngredientSpec);
        }

        Specification<RecipeEntity> excludeIngredientSpec = buildIngredientSpecification(excludeIngredients, true);
        if (excludeIngredientSpec != null) {
            spec = spec.and(excludeIngredientSpec);
        }

        if (StringUtils.hasText(instructionText)) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("instructions")), "%" + instructionText.toLowerCase() + "%"));
        }
        return spec;
    }

    private static Specification<RecipeEntity> buildIngredientSpecification(List<String> ingredients, boolean exclude) {

        return ingredients
                .stream()
                .map(ingredient -> (Specification<RecipeEntity>) (root, query, cb) -> {
                    Predicate predicate = cb.isMember(ingredient, root.get("ingredients"));
                    return exclude ? cb.not(predicate) : predicate;
                })
                .reduce(Specification::and)
                .orElse(null);
    }
}
