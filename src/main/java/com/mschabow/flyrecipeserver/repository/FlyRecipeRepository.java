package com.mschabow.flyrecipeserver.repository;

import com.mschabow.flyrecipeserver.domain.FlyRecipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlyRecipeRepository extends CrudRepository<FlyRecipe, String> {
}
