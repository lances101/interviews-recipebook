package com.boromak.interviews.recipebook.recipe;

import com.boromak.interviews.recipebook.fixtures.RecipePojoFactory;
import com.boromak.interviews.recipebook.generated.model.RecipeDto;
import com.boromak.interviews.recipebook.testutils.TestMariaDbContainer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.assertj.core.api.SoftAssertions;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
class RecipeControllerCrudIT {

  @Autowired private RecipeRepository recipeRepository;
  @Autowired private RecipePojoFactory recipePojoFactory;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private TestRestTemplate restTemplate;

  @ClassRule private final TestMariaDbContainer container = TestMariaDbContainer.getInstance();

  @Test
  void post_create_recipe_with_all_attributes_should_succeed_and_return_all_attributes() {
    var recipeDto = recipePojoFactory.getRecipeWithAllAttributesAsDto();
    var response = restTemplate.postForEntity("/recipe", recipeDto, RecipeDto.class);

    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    softly
        .assertThat(response.getBody())
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(recipeDto);
    softly.assertAll();
  }

  @ParameterizedTest(name = "post_create_recipe_with_nonnull_attribute_{0}_nulled_should_fail")
  @ValueSource(strings = {"name", "ingredients", "numberOfServings", "vegetarian", "instructions"})
  void post_create_recipe_with_nonnull_attributes_nulled_should_fail(String attribute) {
    var recipeDto = recipePojoFactory.getRecipeWithAllAttributesAsDto();
    var recipeJsonNode = objectMapper.valueToTree(recipeDto);
    // nullify the parametrized attribute
    ((ObjectNode) recipeJsonNode).set(attribute, null);
    var response = restTemplate.postForEntity("/recipe", recipeJsonNode, JsonNode.class);

    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    softly
        .assertThat(response.getBody())
        .extracting(
            jsonNode -> response.getBody().get("errors").get(0).get("defaultMessage").asText())
        .contains("must not be null");
    softly.assertAll();
  }

  @Test
  void put_update_recipe_with_all_attributes_should_succeed_and_return_all_attributes() {
    // setup
    var existingRecipe = recipePojoFactory.getRecipeWithAllAttributes();
    recipeRepository.save(existingRecipe);

    // test
    var recipeDto = recipePojoFactory.getRecipeWithAllAttributesAsDto();
    recipeDto.setName(existingRecipe.getName() + " updated");
    var response =
        restTemplate.exchange(
            "/recipe/" + existingRecipe.getId(),
            HttpMethod.PUT,
            new HttpEntity<>(recipeDto),
            RecipeDto.class);

    // verify
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    softly
        .assertThat(response.getBody())
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(recipeDto);
    softly.assertAll();
  }

  @ParameterizedTest(name = "put_update_recipe_with_nonnull_attribute_{0}_nulled_should_fail")
  @ValueSource(strings = {"name", "ingredients", "numberOfServings", "vegetarian", "instructions"})
  void put_update_recipe_with_nonnull_attributes_nulled_should_fail(String attribute) {
    var existingRecipe = recipePojoFactory.getRecipeWithAllAttributes();
    recipeRepository.save(existingRecipe);

    var recipeJsonNode = objectMapper.valueToTree(existingRecipe);
    ((ObjectNode) recipeJsonNode).set(attribute, null);
    var response =
        restTemplate.exchange(
            "/recipe/" + existingRecipe.getId(),
            HttpMethod.PUT,
            new HttpEntity<>(recipeJsonNode),
            JsonNode.class);

    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    softly
        .assertThat(response.getBody())
        .extracting(
            jsonNode -> response.getBody().get("errors").get(0).get("defaultMessage").asText())
        .contains("must not be null");
    softly.assertAll();
  }

  @Test
  void get_retrieve_recipe_by_id_should_return_recipe() {
    var recipe = recipePojoFactory.getRecipeWithAllAttributes();
    recipeRepository.save(recipe);
    var recipeDto = recipePojoFactory.getRecipeWithAllAttributesAsDto();
    recipeDto.setName(recipe.getName());

    var response = restTemplate.getForEntity("/recipe/" + recipe.getId(), RecipeDto.class);

    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    softly
        .assertThat(response.getBody())
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(recipeDto);
    softly.assertAll();
  }

  @Test
  void get_retrieve_recipe_by_id_should_return_404_if_id_doesnt_exist() {
    // we could use some specific number, but this should be more than safe enough
    var response = restTemplate.getForEntity("/recipe/999999999", JsonNode.class);

    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    softly
        .assertThat(response.getBody())
        .extracting(jsonNode -> response.getBody().get("message").asText())
        .contains("Recipe with id `999999999` not found");
    softly.assertAll();
  }

  @Test
  void delete_existing_recipe_should_succeed() {
    // setup
    var existingRecipe = recipePojoFactory.getRecipeWithAllAttributes();
    recipeRepository.save(existingRecipe);

    var response =
        restTemplate.exchange(
            "/recipe/" + existingRecipe.getId(),
            HttpMethod.DELETE,
            new HttpEntity<>(null),
            Void.class);

    // verify
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    softly.assertAll();
  }

  @Test
  void delete_recipe_with_invalid_id_should_fail() {
    var response =
        restTemplate.exchange(
            "/recipe/999999999", HttpMethod.DELETE, new HttpEntity<>(null), JsonNode.class);

    // verify
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    softly
        .assertThat(response.getBody())
        .extracting(jsonNode -> response.getBody().get("message").asText())
        .contains("Recipe with id `999999999` not found");
    softly.assertAll();
  }
}
