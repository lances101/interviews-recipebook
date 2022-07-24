package com.boromak.interviews.recipebook.recipe;

import java.util.List;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

/** Recipe specification builder for search queries. */
public class RecipeSpecificationBuilder {

  private RecipeSpecificationBuilder() {}

  /**
   * Builds a specification for a search query.
   *
   * @param vegetarian true if the recipe is vegetarian, false otherwise
   * @param minNumberOfServings the minimum number of servings for the recipe
   * @param includeIngredients the ingredients to include in the recipe
   * @param excludeIngredients the ingredients to exclude from the recipe
   * @param instructions the instructions for the recipe
   * @return a specification for the search query
   */
  @SuppressWarnings("DuplicatedCode")
  public static Specification<Recipe> searchRecipes(
      Boolean vegetarian,
      Double minNumberOfServings,
      List<String> includeIngredients,
      List<String> excludeIngredients,
      String instructions) {
    return (root, query, cb) -> {
      Predicate predicate = cb.conjunction();

      if (vegetarian != null) {
        predicate = cb.and(predicate, cb.equal(root.get("vegetarian"), vegetarian));
      }
      if (minNumberOfServings != null) {
        predicate =
            cb.and(
                predicate,
                cb.greaterThanOrEqualTo(root.get("numberOfServings"), minNumberOfServings));
      }

      if (includeIngredients != null && !includeIngredients.isEmpty()) {
        for (String ingredient : includeIngredients) {
          if (ingredient.isEmpty()) {
            continue;
          }
          Subquery<String> subquery = query.subquery(String.class);
          Root<Recipe> subRoot = subquery.correlate(root);
          Join<Recipe, String> join = subRoot.join("ingredients");
          subquery.select(subRoot.get("id")).where(cb.like(join, "%" + ingredient + "%"));
          predicate = cb.and(predicate, root.get("id").in(subquery));
        }
      }

      if (excludeIngredients != null && !excludeIngredients.isEmpty()) {
        for (String ingredient : excludeIngredients) {
          if (ingredient.isEmpty()) {
            continue;
          }
          Subquery<String> subquery = query.subquery(String.class);
          Root<Recipe> subRoot = subquery.correlate(root);
          Join<Recipe, String> join = subRoot.join("ingredients");
          subquery.select(subRoot.get("id")).where(cb.like(join, "%" + ingredient + "%"));
          predicate = cb.and(predicate, root.get("id").in(subquery).not());
        }
      }

      if (instructions != null && !instructions.isEmpty()) {
        predicate = cb.and(predicate, cb.like(root.get("instructions"), "%" + instructions + "%"));
      }

      return predicate;
    };
  }
}
