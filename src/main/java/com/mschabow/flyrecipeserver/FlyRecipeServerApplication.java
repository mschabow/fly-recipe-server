package com.mschabow.flyrecipeserver;

import com.mschabow.flyrecipeserver.applogic.RecipeExtractor;
import com.mschabow.flyrecipeserver.applogic.VideoIdUpdateManager;
import com.mschabow.flyrecipeserver.service.FlyRecipeService;
import com.mschabow.flyrecipeserver.service.YouTubeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;


@SpringBootApplication
@EnableScheduling
public class FlyRecipeServerApplication {

  @Autowired
  VideoIdUpdateManager updateService;
  @Autowired
  YouTubeItemService youTubeItemService;
  @Autowired
  RecipeExtractor recipeExtractor;
  @Autowired
  FlyRecipeService flyRecipeService;


  public static void main(String[] args) {
    SpringApplication.run(FlyRecipeServerApplication.class, args);
  }


  //@Scheduled(fixedRate = 1000*60*24) // Every 24 hours
  //@Scheduled(cron = "0 0 0 * * ?") // Once Per Day at 12am
  private void updateFffVideoIds(){
    //VideoIdUpdateManager.getVideoListFromFile(file);
    //updateService.getYouTubeItemsFlyFishFood("");
    flyRecipeService.reset();

    Map<String, String> items = new HashMap<>();

    youTubeItemService.list().forEach(i -> {
      if (i.getId() != null) {
        String id = i.getId().getVideoId();
        if (!items.containsKey(id)) {
          items.put(id, id);
        }
      }
    });


    for (String id : items.values()) {
      if (id != null){
        recipeExtractor.createRecipe(id);
      }

    }

  }



}
