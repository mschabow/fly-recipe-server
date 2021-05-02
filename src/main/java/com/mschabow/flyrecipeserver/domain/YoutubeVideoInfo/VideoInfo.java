package com.mschabow.flyrecipeserver.domain.YoutubeVideoInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mschabow.flyrecipeserver.domain.FlyRecipe;
import org.json.JSONPropertyIgnore;

import javax.persistence.*;
import java.util.Map;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoInfo {

    @Id
    private String uniqueId;
    private String title;

    @Column(length = 50000)
    private String description;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String id) {
        this.uniqueId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRecipieFound() {
        return recipieFound;
    }

    public void setRecipieFound(boolean recipieFound) {
        this.recipieFound = recipieFound;
    }

    private boolean recipieFound;

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Embedded
    private Thumbnail thumbnail;

}
