package com.food.recipe.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * The below class act as entity class in the persistence layer and will create table
 * with name "recipes" into database.
 *
 * @author snehalata.arun.raut
 */
@Entity
@Table(name = "recipes_table", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecipeEntity implements Serializable {

    @Id
    @Min(1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Boolean isVegetarian;

    @Column(nullable = false)
    @Min(1)
    @Max(10000)
    private Integer servings;

    @Column(nullable = false)
    @ElementCollection
    private List<String> ingredients;

    @Column(nullable = false)
    @Lob
    private String instructions;
}
