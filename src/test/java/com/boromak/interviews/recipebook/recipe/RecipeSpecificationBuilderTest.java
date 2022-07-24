package com.boromak.interviews.recipebook.recipe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.cartesian.ArgumentSets;
import org.junitpioneer.jupiter.cartesian.CartesianTest;

class RecipeSpecificationBuilderTest {

  @Test
  void specification_with_all_empty_parameters_should_still_build_successfully() {
    var spec = RecipeSpecificationBuilder.searchRecipes(null, null, null, null, null);
    assertThat(spec).isNotNull();
  }

  @CartesianTest
  @CartesianTest.MethodFactory("all_args_test_argument_factory")
  void specification_with_all_kinds_of_permutations_should_build_successfully(
      Boolean vegetarian,
      Double minNumberOfServings,
      List<String> includeIngredients,
      List<String> excludeIngredients,
      String instructions) {
    var spec =
        RecipeSpecificationBuilder.searchRecipes(
            vegetarian, minNumberOfServings, includeIngredients, excludeIngredients, instructions);
    assertThat(spec).isNotNull();
  }

  static ArgumentSets all_args_test_argument_factory() {
    return ArgumentSets.argumentsForFirstParameter(null, true, false)
        .argumentsForNextParameter(null, 1.0, 2.0)
        .argumentsForNextParameter(
            null, List.of("salmon", "potato"), List.of("salmon", "potato", "carrot"))
        .argumentsForNextParameter(
            null, List.of("salmon", "potato"), List.of("salmon", "potato", "carrot"))
        .argumentsForNextParameter(null, "", "Some long instructions string");
  }
}
