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

@SpringBootApplication
@EnableScheduling
public class FlyRecipeServerApplication {
  @Autowired
  RecipeExtractor recipeExtractor;

  public static void main(String[] args) {
    SpringApplication.run(FlyRecipeServerApplication.class, args);
  }

  // @Scheduled(fixedRate = 1000 * 60 * 60 * 24) // Every 24 hours
  // @Scheduled(cron = "0 0 0 * * ?") // Once Per Day at 12am
  // @Scheduled(cron = "@daily")
  private void updateFffVideoIds() {

    recipeExtractor.UpdateVideos();

  }

}
