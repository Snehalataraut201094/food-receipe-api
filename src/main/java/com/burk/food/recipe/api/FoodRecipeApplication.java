package com.burk.food.recipe.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The below class is the main class work as a entry point for spring boot application.
 * 
 * @author snehalata.arun.raut
 */
@SpringBootApplication
@EnableSwagger2
public class FoodRecipeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodRecipeApplication.class, args);
	}
}
