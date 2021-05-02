package com.mschabow.flyrecipeserver.domain;

import com.mschabow.flyrecipeserver.domain.YoutubeVideoInfo.VideoInfo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FlyRecipe {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id
    private String videoId;
    @OneToMany(cascade=CascadeType.ALL)
    private List<Ingredient> ingredientList = new ArrayList<>();
    @OneToOne
    private VideoInfo videoInfo;
    private boolean hasIngredients;


    public boolean isHasIngredients() {
        return hasIngredients;
    }

    public void setHasIngredients(boolean hasIngredients) {
        this.hasIngredients = hasIngredients;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
        this.videoInfo.setRecipieFound(true);
        hasIngredients = true;
        videoInfo.setRecipieFound(true);
    }

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(VideoInfo videoInfo) {
        this.videoInfo = videoInfo;
    }
}
