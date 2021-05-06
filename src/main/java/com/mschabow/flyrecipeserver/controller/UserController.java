package com.mschabow.flyrecipeserver.controller;

import com.mschabow.flyrecipeserver.domain.Ingredient;
import com.mschabow.flyrecipeserver.domain.User;
import com.mschabow.flyrecipeserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/api/v1/users")
public class UserController {
    UserService userService;

    class FavoriteRecipes{
      private Iterable<String> favoriteRecipes = new ArrayList<>();

        public Iterable<String> getFavoriteRecipes() {
            return favoriteRecipes;
        }

        public void setFavoriteRecipes(Iterable<String> favoriteRecipes) {
            this.favoriteRecipes = favoriteRecipes;
        }
    }

    class OwnedIngredients{
        private Iterable<Ingredient> ownedIngredients = new ArrayList<>();

        public Iterable<Ingredient> getOwnedIngredients() {
            return ownedIngredients;
        }

        public void setOwnedIngredients(Iterable<Ingredient> ownedIngredients) {
            this.ownedIngredients = ownedIngredients;
        }
    }

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin
    @PutMapping("/{id}/add-user")
    public ResponseEntity<User> addUser(@PathVariable String id){
        User user = new User();
        if(!userExists(id)){
            
            user.setId(id);
            userService.save(user);
        }
        
        return ResponseEntity.ok(userService.findById(id).get());
    }

    
    public boolean userExists(String id){
        return userService.existsById(id);
    }

    @CrossOrigin
    @GetMapping("/{id}/get-favorite-recipes")
    public FavoriteRecipes getFavoriteRecipes(@PathVariable String id){
        FavoriteRecipes recipes = new FavoriteRecipes();
        if(userService.existsById(id)){
            Optional<User> user = userService.findById(id);
            recipes.setFavoriteRecipes(user.get().getFavoriteRecipes());
        }
        return recipes;
    }

    @CrossOrigin
    @GetMapping("/{id}/get-ingredients")
    public OwnedIngredients getIngredeients(@PathVariable String id){
        OwnedIngredients ingredients = new OwnedIngredients();
        if(userService.existsById(id)){
            Optional<User> user = userService.findById(id);
            ingredients.setOwnedIngredients(user.get().getIngredientList());
        }
        return ingredients;
    }

    @CrossOrigin
    @PatchMapping("/{id}/update-ingredients")
    public ResponseEntity<?> updateIngredients(@RequestBody List<Ingredient> ingredients, @PathVariable String id){
        ResponseEntity<?> response;

        if(userService.existsById(id)){
            User user = userService.findById(id).get();
            user.setIngredientList(ingredients);
            userService.save(user);
            response = ResponseEntity.ok("user ingredients updated");

        }
        else response = (ResponseEntity<?>) ResponseEntity.unprocessableEntity();

        return response;
    }

    @CrossOrigin
    @PatchMapping("/{id}/update-favorites")
    public ResponseEntity<?> updateFavorites(@RequestBody List<String> favoriteIds, @PathVariable String id){
        ResponseEntity<?> response;

        if(userService.existsById(id)){
            User user = userService.findById(id).get();
            user.setFavoriteRecipes(favoriteIds);
            userService.save(user);
            response = ResponseEntity.ok("user favorites updated");

        }
        else response = (ResponseEntity<?>) ResponseEntity.unprocessableEntity();

        return response;
    }


}
