package com.mschabow.flyrecipeserver.domain.YoutubeResult;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;


@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResults {

    public String getNextPageToken() {
        return nextPageToken;
    }

    public List<YouTubeItem> getItems() {
        return items;
    }


    public UUID getResultsId() {
        return resultsId;
    }

    private String nextPageToken;

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public void setItems(List<YouTubeItem> items) {
        this.items = items;
    }

    public void setResultsId(UUID resultsId) {
        this.resultsId = resultsId;
    }

    @OneToMany
    @JsonProperty("items")
    private List<YouTubeItem> items;

    @Id
    @GeneratedValue
    private UUID resultsId;


}

