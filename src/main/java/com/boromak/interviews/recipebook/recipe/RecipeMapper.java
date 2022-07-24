package com.boromak.interviews.recipebook.recipe;

import com.boromak.interviews.recipebook.generated.model.RecipeDto;
import org.mapstruct.Mapper;

/** Mapstruct mapper between {@link Recipe} and {@link RecipeDto}. */
@Mapper
public interface RecipeMapper {
  /**
   * Convert a {@link Recipe} to a {@link RecipeDto}.
   *
   * @param recipe the recipe to convert
   * @return the converted recipe dto
   */
  RecipeDto toDto(Recipe recipe);

  /**
   * Convert a {@link RecipeDto} to a {@link Recipe}.
   *
   * @param recipeDto the recipe dto to convert
   * @return the converted recipe
   */
  Recipe toEntity(RecipeDto recipeDto);
}
