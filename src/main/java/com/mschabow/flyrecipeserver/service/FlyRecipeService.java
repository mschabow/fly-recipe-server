package com.mschabow.flyrecipeserver.service;

import com.mschabow.flyrecipeserver.domain.FlyRecipe;
import com.mschabow.flyrecipeserver.repository.FlyRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlyRecipeService {

    private final FlyRecipeRepository resultRepository;

    @Autowired
    public FlyRecipeService(FlyRecipeRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public Iterable<FlyRecipe> list() {
        return resultRepository.findAll();
    }

    public FlyRecipe save(FlyRecipe recipe){
        return resultRepository.save(recipe);
    }

    public Iterable<FlyRecipe> save(List<FlyRecipe> recipes){
        return resultRepository.saveAll(recipes);
    }
}
