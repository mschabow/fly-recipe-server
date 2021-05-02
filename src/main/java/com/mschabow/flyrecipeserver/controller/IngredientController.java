package com.mschabow.flyrecipeserver.controller;

import com.mschabow.flyrecipeserver.domain.Ingredient;
import com.mschabow.flyrecipeserver.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/")
public class IngredientController {

    private IngredientService service;

    @Autowired
    public IngredientController(IngredientService service) {
        this.service = service;
    }

    @GetMapping("/ingredients/")
    public Iterable<Ingredient> list(){
        return service.list();
    }
}
