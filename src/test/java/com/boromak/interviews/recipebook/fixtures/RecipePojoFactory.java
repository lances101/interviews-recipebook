package com.boromak.interviews.recipebook.fixtures;

import com.boromak.interviews.recipebook.generated.model.RecipeDto;
import com.boromak.interviews.recipebook.recipe.Recipe;
import com.boromak.interviews.recipebook.recipe.RecipeMapper;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecipePojoFactory {
  private static final Random random = new Random();

  @Autowired private RecipeMapper recipeMapper;

  public Recipe getRecipeWithAllAttributes() {
    return Recipe.builder()
        .id(1L)
        .name("Recipe " + random.nextInt())
        .ingredients(List.of("salmon", "potato"))
        .numberOfServings(2.0)
        .vegetarian(false)
        .instructions("Some long instructions string")
        .build();
  }

  public RecipeDto getRecipeWithAllAttributesAsDto() {
    return RecipeDto.builder()
        .id(1L)
        .name("Recipe " + random.nextInt())
        .ingredients(List.of("salmon", "potato"))
        .numberOfServings(2.0)
        .vegetarian(false)
        .instructions("Some long instructions string")
        .build();
  }

  public Recipe getRecipeWithMostlyEmptyFields() {
    return Recipe.builder()
        .id(null)
        .name("Recipe " + random.nextInt())
        .ingredients(List.of())
        .numberOfServings(0d)
        .vegetarian(false)
        .instructions("text")
        .build();
  }
}
