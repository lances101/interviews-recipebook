package com.boromak.interviews.recipebook.recipe;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/** Recipe repository handling all CRUD and search operations. */
@Repository
public interface RecipeRepository
    extends PagingAndSortingRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {}
