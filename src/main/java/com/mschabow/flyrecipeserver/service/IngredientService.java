package com.mschabow.flyrecipeserver.service;

import com.mschabow.flyrecipeserver.domain.Ingredient;
import com.mschabow.flyrecipeserver.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    private IngredientRepository repository;

    @Autowired
    public IngredientService(IngredientRepository repository) {
        this.repository = repository;
    }

    public Iterable<Ingredient> list(){
        return repository.findAll();
    }

    public Ingredient save(Ingredient ingredient){
        return repository.save(ingredient);
    }

    public Iterable<Ingredient> save(List<Ingredient> ingredients){
        return repository.saveAll(ingredients);
    }
}
