package com.boromak.interviews.recipebook.recipe;

import com.boromak.interviews.recipebook.generated.model.RecipeDto;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/** Recipe service handling all CRUD and search operations. */
@Service
@AllArgsConstructor
public class RecipeService {
  private final RecipeRepository recipeRepository;

  private final RecipeMapper recipeMapper;

  /**
   * Create a new recipe.
   *
   * @param recipeDto the recipe to create
   * @return the created recipe
   */
  public RecipeDto create(RecipeDto recipeDto) {
    recipeDto.setId(null);
    return recipeMapper.toDto(recipeRepository.save(recipeMapper.toEntity(recipeDto)));
  }

  /**
   * Update an existing recipe.
   *
   * @param recipeId the id of the recipe to update
   * @param recipeDto the recipe to update the id with
   * @return the updated recipe
   */
  public RecipeDto update(Long recipeId, RecipeDto recipeDto) {
    if (recipeRepository.findById(recipeId).isEmpty()) {
      throw new EntityNotFoundException(String.format("Recipe with id `%s` not found", recipeId));
    }
    recipeDto.setId(recipeId);
    return recipeMapper.toDto(recipeRepository.save(recipeMapper.toEntity(recipeDto)));
  }

  /**
   * Deletes a specific recipe by id.
   *
   * @param recipeId the id of the recipe to delete
   */
  public void delete(Long recipeId) {
    if (recipeRepository.findById(recipeId).isEmpty()) {
      throw new EntityNotFoundException(String.format("Recipe with id `%s` not found", recipeId));
    }
    recipeRepository.deleteById(recipeId);
  }

  /**
   * Get a specific recipe by id.
   *
   * @param recipeId the id of the recipe to get
   * @return the recipe
   */
  public RecipeDto getById(Long recipeId) {
    return recipeRepository
        .findById(recipeId)
        .map(recipeMapper::toDto)
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    String.format("Recipe with id `%s` not found", recipeId)));
  }

  /**
   * Searches for recipes with the provided parameters. If null is provided for a parameter, the
   * filter is not applied.
   *
   * @param vegetarian filter on vegetarian recipes
   * @param minNumberOfServings filter on minimum number of servings
   * @param includeIngredients filter on recipes containing these ingredients
   * @param excludeIngredients filter on recipes not containing these ingredients
   * @param instructions filter on recipes containing these instructions
   * @param pageable pagination parameters
   * @return the page of recipes
   */
  public Page<RecipeDto> search(
      Boolean vegetarian,
      Double minNumberOfServings,
      List<String> includeIngredients,
      List<String> excludeIngredients,
      String instructions,
      Pageable pageable) {
    var specification =
        RecipeSpecificationBuilder.searchRecipes(
            vegetarian, minNumberOfServings, includeIngredients, excludeIngredients, instructions);
    //noinspection unchecked
    return recipeRepository
        .findAll(specification, pageable)
        .map(recipe -> recipeMapper.toDto((Recipe) recipe));
  }
}
