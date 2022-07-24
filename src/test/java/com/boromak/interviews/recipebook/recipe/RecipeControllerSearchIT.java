package com.boromak.interviews.recipebook.recipe;

import com.boromak.interviews.recipebook.fixtures.RecipePojoFactory;
import com.boromak.interviews.recipebook.testutils.TestMariaDbContainer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.util.Strings;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
class RecipeControllerSearchIT {

  @Autowired private RecipeRepository recipeRepository;
  @Autowired private RecipePojoFactory recipePojoFactory;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private TestRestTemplate restTemplate;

  @ClassRule private final TestMariaDbContainer container = TestMariaDbContainer.getInstance();

  @BeforeEach
  void init() {
    recipeRepository.deleteAll();
    var atomicInteger = new AtomicInteger(0);
    var baseRecipe = recipePojoFactory.getRecipeWithMostlyEmptyFields();
    var recipesToCreate =
        Stream.of(
                // base
                baseRecipe.toBuilder().build(),

                // vegetarian
                baseRecipe.toBuilder().vegetarian(true).build(),
                baseRecipe.toBuilder().vegetarian(false).build(),

                // ingredients list
                baseRecipe.toBuilder().ingredients(List.of("salmon")).build(),
                baseRecipe.toBuilder().ingredients(List.of("potato")).build(),
                baseRecipe.toBuilder().ingredients(List.of("salmon", "potato")).build(),

                // servings
                baseRecipe.toBuilder().numberOfServings(0.0).build(),
                baseRecipe.toBuilder().numberOfServings(5.0).build(),
                baseRecipe.toBuilder().numberOfServings(Double.MIN_VALUE).build(),
                baseRecipe.toBuilder().numberOfServings(Double.MAX_VALUE).build(),

                // instructions
                baseRecipe.toBuilder().instructions("").build(),
                baseRecipe.toBuilder().instructions("some instructions with oven").build())
            .map(
                recipe ->
                    recipe.toBuilder()
                        .id(null)
                        .name("recipe " + atomicInteger.incrementAndGet())
                        .build())
            .collect(Collectors.toList());
    recipeRepository.saveAll(recipesToCreate);
  }

  @ParameterizedTest(name = "search by {0} should result in {1}")
  @CsvSource({
    // base
    ",12",

    // vegetarian
    "vegetarian=true,1",
    "vegetarian=false,11",

    // include ingredients
    "include_ingredients=salmon,2",
    "include_ingredients=mon,2",

    // exclude ingredients
    "exclude_ingredients=salmon,10",
    "exclude_ingredients=salmon&exclude_ingredients=potato,9",

    // include and exclude
    "include_ingredients=salmon&include_ingredients=potato,1",
    "include_ingredients=salmon&exclude_ingredients=potato,1",

    // servings
    "min_number_of_servings=0,12",
    "min_number_of_servings=5,2",

    // instructions
    "instructions=,12",
    "instructions=some instructions,1",
    "instructions=ins,1",

    // none match
    "include_ingredients=carrot,0",

    // explicit test cases
    // - all vegetarian recipes
    "vegetarian=true,1",
    // - recipes that can serve 4 persons and have “potatoes” as an ingredient
    "number_of_servings=4&include_ingredients=potato,2",
    // - recipes without “salmon” as an ingredient that has “oven” in the instructions.
    "exclude_ingredients=salmon&instructions=oven,1",
  })
  void search_by_x_should_return_y(String filterString, Integer expectedCount) {
    var sb = new StringBuilder();
    sb.append("/recipe/search");
    if (!Strings.isNullOrEmpty(filterString)) {
      sb.append(String.format("?%s", filterString));
    }
    var response = restTemplate.getForEntity(sb.toString(), JsonNode.class);

    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    softly.assertThat(response.getBody().get("totalElements").asInt()).isEqualTo(expectedCount);
    softly.assertAll();
  }

  @Test
  void searching_by_all_parameters_should_return_the_matching_entry() {
    var recipe =
        recipePojoFactory.getRecipeWithMostlyEmptyFields().toBuilder()
            .id(null)
            .vegetarian(true)
            .ingredients(List.of("cheese", "carrot"))
            .numberOfServings(5d)
            .instructions("put some sheets into the oven")
            .build();
    recipeRepository.save(recipe);

    var queryString =
        "/recipe/search?"
            + "vegetarian=true"
            + "&include_ingredients=cheese"
            + "&exclude_ingredients=potato"
            + "&min_number_of_servings=5"
            + "&instructions=sheet";

    var response = restTemplate.getForEntity(queryString, JsonNode.class);

    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    softly.assertThat(response.getBody().get("totalElements").asInt()).isEqualTo(1);
    softly.assertAll();
  }
}
