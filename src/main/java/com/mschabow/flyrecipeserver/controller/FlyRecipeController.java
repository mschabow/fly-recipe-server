package com.mschabow.flyrecipeserver.controller;

import com.mschabow.flyrecipeserver.domain.FlyRecipe;
import com.mschabow.flyrecipeserver.service.FlyRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/api/v1/recipes")
public class FlyRecipeController {
    private FlyRecipeService service;
    @Autowired
    public FlyRecipeController(FlyRecipeService service) {
        this.service = service;
    }

    public class RecipeResponse{
        Iterable<FlyRecipe> recipes;

        public Iterable<FlyRecipe> getRecipes() {
            return recipes;
        }

        public void setRecipes(Iterable<FlyRecipe> recipes) {
            this.recipes = recipes;
        }
    }


    @GetMapping("/all/")
    public RecipeResponse list(){
        RecipeResponse response = new RecipeResponse();
        response.recipes = service.list();
        return response;
    }

    
    @GetMapping("/complete/")
    @CrossOrigin
    public RecipeResponse listRecipeFound() {
        List<FlyRecipe> completeRecipies = new ArrayList<>();
        for (FlyRecipe recipe:service.list()) {
            if(recipe.isHasIngredients()){
                completeRecipies.add(recipe);
            }
        }

        RecipeResponse response = new RecipeResponse();
        response.recipes = completeRecipies;
        return response;
    }

    @GetMapping("/total-count/")
    public int count(){
        return ((Collection<FlyRecipe>) list().recipes).size();
    }

    @GetMapping("/complete-count/")
    public int countComplete(){
        return ((Collection<FlyRecipe>) listRecipeFound().recipes).size();
    }
}
