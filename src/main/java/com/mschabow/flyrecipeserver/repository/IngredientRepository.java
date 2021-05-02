package com.mschabow.flyrecipeserver.repository;

import com.mschabow.flyrecipeserver.domain.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, String> {



}
