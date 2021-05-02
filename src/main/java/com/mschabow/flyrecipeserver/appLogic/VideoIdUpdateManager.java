package com.mschabow.flyrecipeserver.appLogic;

import com.mschabow.flyrecipeserver.controller.YouTubeAPIController;
import com.mschabow.flyrecipeserver.domain.YoutubeResult.SearchResults;
import com.mschabow.flyrecipeserver.domain.YoutubeVideoInfo.VideoIds;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mschabow.flyrecipeserver.domain.YoutubeResult.YouTubeItem;
import com.mschabow.flyrecipeserver.service.YouTubeItemService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class VideoIdUpdateManager {

    YouTubeItemService youTubeItemService;
    YouTubeAPIController youTubeAPIController;

    List<YouTubeItem> previousResults = new ArrayList<>();
    List<YouTubeItem> newResults = new ArrayList<>();
    VideoIds videoIds = new VideoIds();
    ObjectMapper objectMapper = new ObjectMapper();
    static final String FLY_FISH_FOOD_CHANNEL_ID = "&channelId=UCWxBa8MprBJr1vuv3uw8ghA"; //Fly Fish Food YouTube Channel

    @Autowired
    public VideoIdUpdateManager(YouTubeItemService youTubeItemService, YouTubeAPIController youTubeAPIController) {
        this.youTubeItemService = youTubeItemService;
        this.youTubeAPIController = youTubeAPIController;
        youTubeItemService.list().forEach(youTubeItem -> previousResults.add(youTubeItem));
    }


    public void getYouTubeItemsFlyFishFood(String nextPageRequest) {

        ResponseBody apiResults = youTubeAPIController.sendRequest(FLY_FISH_FOOD_CHANNEL_ID, "&part=snippet", "&q=material", "&fields=pageInfo, nextPageToken, items(id/videoId)", "&maxResults=1000", nextPageRequest);

        try {
            SearchResults results = objectMapper.readValue(apiResults.string(), SearchResults.class);
            saveNewResults(results);
        } catch (IOException e1) {
            e1.printStackTrace();
        }


    }

    private void saveNewResults(SearchResults results) {
        for (YouTubeItem item : results.getItems()) {
            if(item.getId() != null){
                newResults.add(item);
            }
        }
        youTubeItemService.save(newResults);

        if (results.getNextPageToken() != null && !results.getNextPageToken().equals("")) {
            String nextPageRequest = "&pageToken=" + results.getNextPageToken();
            getYouTubeItemsFlyFishFood(nextPageRequest);
        }


    }

    public void getVideoListFromFile(File file) {

        try {
            SearchResults results = objectMapper.readValue(file, SearchResults.class);
            saveNewResults(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
