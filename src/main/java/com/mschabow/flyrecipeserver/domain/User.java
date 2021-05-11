package com.mschabow.flyrecipeserver.domain;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
public class User {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList.clear();
        this.ingredientList.addAll(ingredientList);
    }

    public List<String> getFavoriteRecipes() {
        return favoriteRecipes;
    }

    public void setFavoriteRecipes(List<String> favoriteRecipes) {
        this.favoriteRecipes = favoriteRecipes;
    }

    @OneToMany
    private List<Ingredient> ingredientList = new ArrayList<>();
    @ElementCollection
    private List<String> favoriteRecipes = new ArrayList<>();

}
