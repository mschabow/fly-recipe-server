package com.mschabow.flyrecipeserver.appLogic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mschabow.flyrecipeserver.controller.YouTubeAPIController;
import com.mschabow.flyrecipeserver.domain.FlyRecipe;
import com.mschabow.flyrecipeserver.domain.Ingredient;
import com.mschabow.flyrecipeserver.domain.YoutubeVideoInfo.Thumbnail;
import com.mschabow.flyrecipeserver.domain.YoutubeVideoInfo.VideoInfo;
import com.mschabow.flyrecipeserver.service.FlyRecipeService;
import com.mschabow.flyrecipeserver.service.IngredientService;
import com.mschabow.flyrecipeserver.service.VideoInfoService;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class RecipeExtractor {
    YouTubeAPIController youTubeApi;
    FlyRecipeService recipeService;
    IngredientService ingredientService;
    VideoInfoService videoInfoService;

    private String testHtml = "https://www.flyfishfood.com/blogs/euro-nymph-tutorials/thread-frenchie";
    public ResponseBody testResponseBody;
    ObjectMapper objectMapper = new ObjectMapper();
    List<String> triedSearchStrings = new ArrayList<>();

    @Autowired
    public RecipeExtractor(YouTubeAPIController youTubeApi,
                           FlyRecipeService recipeService,
                           IngredientService ingredientService,
                           VideoInfoService videoInfoService) {
        this.youTubeApi = youTubeApi;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.videoInfoService = videoInfoService;
        //testResponseBody = youTubeApi.sendGetRequest(testHtml);
    }

    public ResponseBody getTestResponseBody() {
        return testResponseBody;
    }

    public List<String> extractMaterialsHTTPLink(String description) {

        List<String> httpLinks = new ArrayList<>();
        String[] tokens = description.split("flyfishfood.com/");
        if (tokens.length > 1) {
            String[] tokens2 = tokens[1].split("\n");
            if (tokens2.length > 1) {
                String searchString = tokens2[0].strip();
                if (triedSearchStrings.stream().noneMatch(p -> p.equals(searchString))) {
                    triedSearchStrings.add(searchString);
                    httpLinks.add("https://www.flyfishfood.com/" + searchString);


                }
            }

        }
        return httpLinks;

    }

    public void createRecipies() {
        //get ID list
        //foreach id, create recipe
    }


    public FlyRecipe createRecipe(String youTubeVideoId) {
        FlyRecipe recipe = null;

        // get video info from YouTubeAPI
        VideoInfo videoInfo = getYouTubeVideoInfo(youTubeVideoId);

        if (videoInfo != null) {
            recipe = createRecipeFromVideoInfo(videoInfo);
        }

        if (recipe != null) {
            List<Ingredient> ingredients = new ArrayList<>();
            //  try to find link in body
            List<String> htmlLinks = extractMaterialsHTTPLink(recipe.getVideoInfo().getDescription());
            if(!htmlLinks.isEmpty())
            {
                // try to get ingredients from links
                ingredients.addAll(getIngredientsFromFFFPage(htmlLinks));

            }
            if(ingredients.isEmpty()){
                ingredients.addAll(getIngredientsFromDescription(videoInfo));
            }
            if(!ingredients.isEmpty()){
                trySaveIngredients(ingredients);
                recipe.setIngredientList(ingredients);
                recipeService.save(recipe);
            }
        }
        return recipe;
    }

    private void trySaveIngredients(List<Ingredient> ingredients) {
        Iterable<Ingredient> savedIngredients = ingredientService.list();
        for (Ingredient ingredient : ingredients) {

            if(ingredient.getName() != null) {
                boolean ingredientFound = false;
                for (Ingredient i : savedIngredients) {
                    if (i.getName() != null && i.getName().equals(ingredient.getName())) {
                        ingredientFound = true;
                        break;
                    }
                }
                if (!ingredientFound) {
                    ingredientService.save(ingredient);
                }
            }
        }
    }

    private List<Ingredient> getIngredientsFromDescription(VideoInfo videoInfo) {
        String description = videoInfo.getDescription();
        List<Ingredient> ingredients = new ArrayList<>();

            if(description.contains("\nHook:")){
                String[] tokens = description.split("\n");
                for (String token : tokens) {
                    String[] tokens2 = token.split(": ");
                    String[] itemTest = tokens2[0].split(" ");
                    if(itemTest.length == 1) {
                        Ingredient ingredient = new Ingredient();
                        ingredient.setType(tokens2[0]);
                        ingredient.setName(tokens[1].replace("(+)", "").strip());
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
        Ingredient ingredient = null;
        List<Ingredient> ingredientList = new ArrayList<>();

        String[] tokens = htmlText.split("/products/");

        int ingredientIndex = 0;

        while (ingredientIndex < tokens.length) {
            if (tokens[ingredientIndex].contains("List.Item")) {
                try {
                    ingredient = new Ingredient();
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
        return ingredientList;
    }
}
