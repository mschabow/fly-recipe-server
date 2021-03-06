package com.mschabow.flyrecipeserver.applogic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mschabow.flyrecipeserver.controller.YouTubeAPIController;
import com.mschabow.flyrecipeserver.domain.FlyRecipe;
import com.mschabow.flyrecipeserver.domain.Ingredient;
import com.mschabow.flyrecipeserver.domain.YoutubeVideoInfo.Thumbnail;
import com.mschabow.flyrecipeserver.domain.YoutubeVideoInfo.VideoInfo;
import com.mschabow.flyrecipeserver.service.FlyRecipeService;
import com.mschabow.flyrecipeserver.service.VideoInfoService;
import com.mschabow.flyrecipeserver.service.YouTubeItemService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class RecipeExtractor {
  VideoIdUpdateManager updateService;
  YouTubeItemService youTubeItemService;
  YouTubeAPIController youTubeApi;
  FlyRecipeService recipeService;
  VideoInfoService videoInfoService;

  ObjectMapper objectMapper = new ObjectMapper();
  Map<String, String> triedLinks = new HashMap<>();

  @Autowired
  public RecipeExtractor(VideoIdUpdateManager updateService, YouTubeItemService youTubeItemService,
      YouTubeAPIController youTubeApi, FlyRecipeService recipeService, VideoInfoService videoInfoService) {
    this.updateService = updateService;
    this.youTubeItemService = youTubeItemService;
    this.youTubeApi = youTubeApi;
    this.recipeService = recipeService;
    this.videoInfoService = videoInfoService;
  }

  public void UpdateVideos() {
    // VideoIdUpdateManager.getVideoListFromFile(file);
    // updateService.getYouTubeItemsFlyFishFood("");
    System.out.println("Starting Update....");
    // recipeService.reset();

    Map<String, String> items = new HashMap<>();

    youTubeItemService.list().forEach(i -> {
      if (i.getId() != null) {
        String id = i.getId().getVideoId();
        if (!items.containsKey(id)) {
          items.put(id, id);
        }
      }
    });

    Map<String, Boolean> currentRecipes = new HashMap<>();

    recipeService.list().forEach(r -> currentRecipes.put(r.getVideoId(), r.isHasIngredients()));

    for (String id : items.values()) {
      if (id != null) {
        if (currentRecipes.get(id) == null || currentRecipes.get(id) == false)
          createRecipe(id);
      }
    }
    System.out.println("Update Complete");
  }

  public List<String> extractMaterialsHTTPLink(String description) {
    Map<String, String> httpLinks = new HashMap<>();

    String[] tokens = description.split("flyfishfood.com/");
    if (tokens.length > 1) {
      String[] tokens2 = tokens[1].split("\n");
      if (tokens2.length > 1) {
        String searchString = tokens2[0].strip();
        if (triedLinks.get(searchString) == null) {
          triedLinks.put(searchString, "https://www.flyfishfood.com/" + searchString);
          httpLinks.put(searchString, "https://www.flyfishfood.com/" + searchString);
        }
      }

    }
    return httpLinks.values().stream().toList();
  }

  public void createRecipe(String youTubeVideoId) {
    FlyRecipe recipe = null;
    if (youTubeVideoId.equals("RHUUWEp8amU")) {
      System.out.println("found recipe");
    }

    // get video info from YouTubeAPI
    VideoInfo videoInfo = getYouTubeVideoInfo(youTubeVideoId);

    if (videoInfo != null) {
      recipe = createRecipeFromVideoInfo(videoInfo);
    }

    if (recipe != null) {
      List<Ingredient> ingredients = new ArrayList<>();
      // try to find link in body
      List<String> htmlLinks = extractMaterialsHTTPLink(recipe.getVideoInfo().getDescription());
      if (!htmlLinks.isEmpty()) {
        // try to get ingredients from links
        ingredients.addAll(getIngredientsFromFFFPage(htmlLinks));
      }
      if (ingredients.isEmpty()) {
        ingredients.addAll(getIngredientsFromDescription(videoInfo));
      }
      if (!ingredients.isEmpty()) {
        System.out.println("Saving Recipe Complete Recipe" + recipe.getName());
        recipe.setIngredientList(ingredients);
        recipeService.save(recipe);
      } else {
        System.out.println("No Recipe Found For: " + recipe.getVideoId() + "          " + recipe.getName());
      }
    }
  }

  private List<Ingredient> getIngredientsFromDescription(VideoInfo videoInfo) {
    String description = videoInfo.getDescription();
    List<Ingredient> ingredients = new ArrayList<>();

    if (description.contains("\nHook:")) {
      String[] tokens = description.split("\n");
      for (String token : tokens) {
        String[] tokens2 = token.split(": ");
        String[] itemTest = tokens2[0].split(" ");
        if (itemTest.length == 1 && tokens2.length > 1) {
          Ingredient ingredient = new Ingredient();
          ingredient.setType(tokens2[0]);
          ingredient.setName(tokens2[1].replace("(+)", "").strip());
          ingredients.add(ingredient);
        }
      }
    }
    return ingredients;
  }

  private FlyRecipe createRecipeFromVideoInfo(VideoInfo videoInfo) {
    FlyRecipe recipe = new FlyRecipe();
    recipe.setName(videoInfo.getTitle());
    recipe.setVideoId(videoInfo.getUniqueId());
    recipe.setVideoInfo(videoInfo);
    recipeService.save(recipe);
    return recipe;
  }

  private VideoInfo getYouTubeVideoInfo(String youTubeVideoId) {
    VideoInfo videoInfo = null;
    String videoRequest = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id=" + youTubeVideoId;
    String bodyText = null;
    try {
      bodyText = youTubeApi.sendGetRequest(videoRequest + youTubeApi.KEY).string();
      JSONObject json = new JSONObject(bodyText);
      JSONArray itemsArray = json.getJSONArray("items");
      JSONObject itemJson = itemsArray.getJSONObject(0).getJSONObject("snippet");
      if (itemJson != null) {
        videoInfo = new VideoInfo();
        videoInfo.setDescription(itemJson.getString("description"));
        videoInfo.setTitle(itemJson.getString("title"));
        JSONObject thumbnailsJson = itemJson.getJSONObject("thumbnails");
        JSONObject thumbnailJson = thumbnailsJson.getJSONObject("default");
        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setUrl(thumbnailJson.getString("url"));
        thumbnail.setWidth(thumbnailJson.getInt("width"));
        thumbnail.setHeight(thumbnailJson.getInt("height"));
        videoInfo.setThumbnail(thumbnail);
        videoInfo.setUniqueId(youTubeVideoId);
        videoInfoService.save(videoInfo);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return videoInfo;
  }

  private List<Ingredient> getIngredientsFromFFFPage(List<String> htmlLinks) {
    List<Ingredient> ingredients = new ArrayList<>();
    for (String htmlLink : htmlLinks) {
      String htmlResponse = "";
      try {
        htmlResponse = youTubeApi.sendGetRequest(htmlLink).string();
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (!htmlResponse.equals("")) {
        ingredients = extractIngredientsFromBody(htmlResponse);

      }
    }
    return ingredients;
  }

  public List<Ingredient> extractIngredientsFromBody(String htmlText) {
    List<Ingredient> ingredientList = new ArrayList<>();

    String[] tokens = htmlText.split("/products/");

    int ingredientIndex = 0;

    if (tokens.length > 1) {
      while (ingredientIndex < tokens.length) {
        if (tokens[ingredientIndex].contains("List.Item")) {
          try {
            Ingredient ingredient = new Ingredient();
            String ingredientString = tokens[ingredientIndex];
            String[] tokens1 = ingredientString.split("\"");
            String link = tokens1[0].strip();
            ingredient.setLink("/products/" + link);

            String[] tokens2 = ingredientString.split("<b><u>");
            if (tokens2.length > 1) {
              String type = tokens2[1].split("</u></b>:")[0];
              ingredient.setType(type);
            }

            String[] tokens3 = ingredientString.split("</u></b>:");
            if (tokens3.length > 1) {
              String name = tokens3[1].split("</span>")[0];
              ingredient.setName(name);
            }
            ingredientList.add(ingredient);
          } catch (Exception e) {
            e.printStackTrace();
          }

        }
        ingredientIndex += 1;

      }
    } else {
      tokens = htmlText.split("\n\n");
      if (tokens.length > 1) {
        while (ingredientIndex < tokens.length) {
          if (tokens[ingredientIndex].contains("Hook:")) {
            String[] tokens2 = tokens[ingredientIndex].split("\n");
            Arrays.stream(tokens2).toList().forEach(token -> {
              String[] tokens3 = token.split(":");
              String type = tokens3[0].strip();
              String name = tokens3[1].strip();
              Ingredient ingredient = new Ingredient();
              ingredient.setType(type);
              ingredient.setName(name);
              ingredientList.add(ingredient);
            });
          }
          ingredientIndex += 1;
        }
      }

    }

    return ingredientList;
  }
}
