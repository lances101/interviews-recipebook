package com.boromak.interviews.recipebook.recipe;

import static org.assertj.core.api.Assertions.assertThat;

import com.boromak.interviews.recipebook.generated.model.RecipeDto;
import java.util.List;
import org.junit.jupiter.api.Test;

class RecipeMapperTest {
  private final RecipeMapper recipeMapper = new RecipeMapperImpl();

  @Test
  void mapping_from_entity_to_dto_should_map_all_attributes() {
    var recipe =
        Recipe.builder()
            .id(1L)
            .name("Recipe")
            .ingredients(List.of("salmon", "potato"))
            .numberOfServings(2.0)
            .vegetarian(false)
            .instructions("Some long instructions string")
            .build();

    var expectedDto =
        RecipeDto.builder()
            .id(1L)
            .name("Recipe")
            .ingredients(List.of("salmon", "potato"))
            .numberOfServings(2.0)
            .vegetarian(false)
            .instructions("Some long instructions string")
            .build();

    var mappedDto = recipeMapper.toDto(recipe);

    assertThat(mappedDto).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedDto);
  }

  @Test
  void mapping_from_dto_to_entity_should_map_all_attributes() {
    var dto =
        RecipeDto.builder()
            .id(1L)
            .name("Recipe")
            .ingredients(List.of("salmon", "potato"))
            .numberOfServings(2.0)
            .vegetarian(false)
            .instructions("Some long instructions string")
            .build();

    var expectedRecipe =
        Recipe.builder()
            .id(1L)
            .name("Recipe")
            .ingredients(List.of("salmon", "potato"))
            .numberOfServings(2.0)
            .vegetarian(false)
            .instructions("Some long instructions string")
            .build();

    var mappedRecipe = recipeMapper.toEntity(dto);

    assertThat(mappedRecipe)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(expectedRecipe);
  }
}
