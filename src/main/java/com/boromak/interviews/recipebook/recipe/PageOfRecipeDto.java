package com.boromak.interviews.recipebook.recipe;

import com.boromak.interviews.recipebook.generated.model.RecipeDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Workaround for OpenAPI3 not being able to parse generics properly in it's own spec. See <a
 * href=https://github.com/springdoc/springdoc-openapi/issues/578>springdoc issue</a>.
 */
public class PageOfRecipeDto extends PageImpl<RecipeDto> {
  public PageOfRecipeDto(Page<RecipeDto> page) {
    super(page.getContent(), page.getPageable(), page.getTotalElements());
  }

  public PageOfRecipeDto(List<RecipeDto> content, Pageable pageable, long total) {
    super(content, pageable, total);
  }

  public PageOfRecipeDto(List<RecipeDto> content) {
    super(content);
  }
}
