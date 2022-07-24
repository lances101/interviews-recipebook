package com.boromak.interviews.recipebook.recipe;

import com.boromak.interviews.recipebook.generated.api.RecipeApi;
import com.boromak.interviews.recipebook.generated.model.RecipeDto;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/** Recipe controller handling all CRUD and search operations. */
@AllArgsConstructor
@RestController
public class RecipeApiControllerImpl implements RecipeApi {
  private final RecipeService recipeService;

  @Override
  public ResponseEntity<RecipeDto> createRecipe(RecipeDto recipe) {
    return new ResponseEntity<>(recipeService.create(recipe), HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<RecipeDto> getRecipeById(Long recipeId) {
    return ResponseEntity.ok(recipeService.getById(recipeId));
  }

  @Override
  public ResponseEntity<Void> deleteRecipe(Long recipeId) {
    recipeService.delete(recipeId);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<PageOfRecipeDto> searchRecipes(
      Boolean vegetarian,
      Double minNumberOfServings,
      List<String> includeIngredients,
      List<String> excludeIngredients,
      String instructions,
      Pageable pageable) {
    var page =
        recipeService.search(
            vegetarian,
            minNumberOfServings,
            includeIngredients,
            excludeIngredients,
            instructions,
            pageable);
    // weird recast is required due to openapi schema limitations
    return ResponseEntity.ok(new PageOfRecipeDto(page));
  }

  @Override
  public ResponseEntity<RecipeDto> updateRecipe(Long recipeId, RecipeDto recipeDto) {
    return ResponseEntity.ok(recipeService.update(recipeId, recipeDto));
  }
}
