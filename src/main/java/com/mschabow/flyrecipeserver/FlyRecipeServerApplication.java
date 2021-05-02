package com.mschabow.flyrecipeserver;

import com.mschabow.flyrecipeserver.appLogic.RecipeExtractor;
import com.mschabow.flyrecipeserver.appLogic.VideoIdUpdateManager;
import com.mschabow.flyrecipeserver.domain.YoutubeResult.YouTubeItem;
import com.mschabow.flyrecipeserver.service.YouTubeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.IOException;
import java.util.List;


@SpringBootApplication
@EnableScheduling
public class FlyRecipeServerApplication {

  @Autowired
  VideoIdUpdateManager updateService;
  @Autowired
  YouTubeItemService youTubeItemService;
  @Autowired
  RecipeExtractor recipeExtractor;


  public static void main(String[] args) {
    SpringApplication.run(FlyRecipeServerApplication.class, args);
  }


  //@Scheduled(fixedRate = 1000*60*24) // Every 24 hours
  //@Scheduled(cron = "0 0 0 * * ?") // Once Per Day at 12am
  private void updateFffVideoIds(){
    //VideoIdUpdateManager.getVideoListFromFile(file);
    //updateService.getYouTubeItemsFlyFishFood("");
    Iterable<YouTubeItem> items = youTubeItemService.list();
    for (YouTubeItem item : items) {
      if (item.getId() != null){
        recipeExtractor.createRecipe(item.getId().getVideoId());
      }

    }

  }



}
