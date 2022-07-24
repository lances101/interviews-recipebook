package com.boromak.interviews.recipebook.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * Recipe entity, represents a favorite recipe. It has a name, a list of ingredients and an
 * instruction.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder(toBuilder = true)
public class Recipe {

  /**
   * Maximum length of the recipe instructions text. Reasoning behind number:
   *
   * <p>An A4 page is usually around 1800 characters. We round that up to 2048. 3 pages should be
   * enough for a recipe. Therefore, we take 6144.
   */
  public static final int MAX_RECIPE_TEXT_SIZE = 6144;

  /** The unique identifier of the recipe. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** The name of the recipe. Cannot be null. */
  @NotNull
  @Column(nullable = false)
  private String name;

  /** The list of ingredients of the recipe as a list of strings. Cannot be null. */
  @NotNull
  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> ingredients = new ArrayList<>();

  /** Number of servings for the recipe. */
  @NotNull
  @Column(nullable = false)
  private Double numberOfServings;

  /** A flag indicating whether the recipe is vegetarian. */
  @NotNull
  @Column(nullable = false)
  private Boolean vegetarian;

  /** A string containing the list of instructions. */
  @NotNull
  @Column(nullable = false, length = MAX_RECIPE_TEXT_SIZE)
  private String instructions;

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Recipe recipe = (Recipe) o;
    return id != null && Objects.equals(id, recipe.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
