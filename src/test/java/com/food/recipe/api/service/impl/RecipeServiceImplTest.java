package com.food.recipe.api.service.impl;

import com.food.recipe.api.entity.RecipeEntity;
import com.food.recipe.api.exception.CustomRecipeException;
import com.food.recipe.api.exception.EntityNotFoundException;
import com.food.recipe.api.process.impl.RecipesSpecificationBuilder;
import com.food.recipe.api.repository.RecipeRepository;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static com.food.recipe.api.util.RecipeTestUtil.createRecipeEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;


    @Test
   void shouldCreateRecipe_whenInputIsValid() {

        RecipeEntity recipeEntity = createRecipeEntity();

        when(recipeRepository.save(any())).thenReturn(recipeEntity);

        RecipeEntity response = recipeService.createRecipe(recipeEntity);

        assertResponseMatchesExpected(response, recipeEntity);
    }

    @Test
    void shouldThrowCustomException_whenDataAccessFailsOnCreate() {

        when(recipeRepository.save(any())).thenThrow(new DataAccessResourceFailureException("DB is down."));

        assertThatThrownBy(() -> recipeService.createRecipe(createRecipeEntity()))
                .isInstanceOf( CustomRecipeException.class)
                .hasMessage("Could not save recipe to the database")
                .hasCauseInstanceOf(DataAccessResourceFailureException.class);
    }

    @Test
    void shouldThrowCustomException_whenPersistenceExceptionOnCreate() {

        when(recipeRepository.save(any()))
                .thenThrow(new PersistenceException("Could not persist entity due to a database error."));

        assertThatThrownBy(() -> recipeService.createRecipe(createRecipeEntity()))
                .isInstanceOf( CustomRecipeException.class)
                .hasMessage("Could not save recipe to the database")
                .hasCauseInstanceOf(PersistenceException.class);
    }

    @Test
    void shouldUpdateRecipe_whenInputIsValid() {
        when(recipeRepository.findById(any())).thenReturn(Optional.of( createRecipeEntity()));

        RecipeEntity updatedRecipeEntity = RecipeEntity.builder().
                name("Samosa")
                .isVegetarian(true)
                .instructions("Boil Potato and smash it. Add spices and potato into pan and smash it.")
                .ingredients(List.of("Potato", "Green Chutney", "Spices", "Green Chilly"))
                .servings(2)
                .build();

        when(recipeRepository.save(any())).thenReturn(updatedRecipeEntity);

        RecipeEntity response = recipeService.updateRecipe(1, updatedRecipeEntity);

        assertResponseMatchesExpected(response, updatedRecipeEntity);
    }

    @Test
    void shouldThrowNotFoundException_whenUpdateIdNotFound() {
        int id = 2;
        when(recipeRepository.findById(anyInt())).thenThrow(new EntityNotFoundException("Recipe with ID " + id + " not found"));

        assertThatThrownBy(() -> recipeService.updateRecipe(id, createRecipeEntity()))
                .isInstanceOf( EntityNotFoundException.class)
                .hasMessage("Recipe with ID " + id + " not found");
    }

    @Test
    void shouldThrowNotFoundException_whenDeleteIdNotFound() {
        when(recipeRepository.findById(any())).thenReturn(Optional.empty());

        int id =2;
        assertThatThrownBy(() -> recipeService.deleteRecipe(id))
                .isInstanceOf( EntityNotFoundException.class)
                .hasMessage("Recipe with ID " + id + " not found");
    }

    @Test
    void shouldDeleteRecipe_whenInputIsValid() {
        when(recipeRepository.findById(1)).thenReturn(Optional.of(createRecipeEntity()));

        doNothing().when(recipeRepository).deleteById(any());

        boolean isRowDeleted = recipeService.deleteRecipe(1);

        assertThat(isRowDeleted).isTrue();
    }

    @Test
    void shouldReturnRecipes_whenGetAllRecipes() {
        RecipeEntity expectedRecipe = createRecipeEntity();

        when(recipeRepository.findAll()).thenReturn(List.of(expectedRecipe));

        List<RecipeEntity> actualResponse = recipeService.getAllRecipes();

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse).isNotEmpty();

        RecipeEntity recipeEntity = actualResponse.getFirst();

        assertThat(recipeEntity.getName()).isEqualTo("Vada Pav");
        assertThat(recipeEntity.getIsVegetarian()).isTrue();
        assertThat(recipeEntity.getServings()).isEqualTo(4);
    }

    @Test
    void shouldReturnRecipe_whenGetRecipeById() {

        RecipeEntity expectedRecipe = createRecipeEntity();
        when(recipeRepository.findById(any())).thenReturn(Optional.of(expectedRecipe));

        Optional<RecipeEntity> actualResponse = recipeService.getRecipeById(1);

        assertThat(actualResponse)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedRecipe);
    }

    @Test
    void searchRecipes_withAllFilters_shouldCallRepositoryWithSpecification() {

        List<String> includes = List.of("Spices", "Potato");
        List<String> excludes = List.of("Onion");
        Specification<RecipeEntity> spec = (root, query, cb) -> null;

        try (MockedStatic<RecipesSpecificationBuilder> mockedBuilder = Mockito.mockStatic(RecipesSpecificationBuilder.class)) {
            mockedBuilder.when(() -> RecipesSpecificationBuilder.build(
                    true, 2, includes, excludes, "boil")).thenReturn(spec);

            when(recipeRepository.findAll(any(Specification.class))).thenReturn(List.of(createRecipeEntity()));

            List<RecipeEntity> result = recipeService.searchRecipes(true, 2, includes, excludes, "boil");

            assertThat(result).hasSize(1);
            verify(recipeRepository).findAll(any(Specification.class));
        }
    }

    private void assertResponseMatchesExpected(RecipeEntity actual, RecipeEntity expected) {
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
