package com.mschabow.flyrecipeserver.controller;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;

@Service
public class YouTubeAPIController {
    private YouTubeAPIController(){

    }

    private static final String REQUEST_URI =
            "https://youtube.googleapis.com/youtube/v3/search?";

    public static final String KEY = "&key=AIzaSyBJHbOgbIdc-nVA2W3HLVJy-J7cHoPumb8";//"&key=AIzaSyBJHbOgbIdc-nVA2W3HLVJy-J7cHoPumb8"; // AIzaSyB3IBYqW1rh_z-HlwnTDaPPRC0b7IyqkaA"

    public ResponseBody sendRequest(String channelId, String part, String query, String fields, String maxResults, String pageRequest) {
        String msg = MessageFormat.format("{0}{1}{2}{3}{4}{5}{6}{7}", REQUEST_URI, KEY, channelId, part, query, fields, maxResults, pageRequest);
        return sendGetRequest(msg);
    }

    public ResponseBody sendGetRequest(String requestMessage){
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(requestMessage)
                .addHeader("Accept-Encoding", "json")
                .method("GET", null)
                .build();
        ResponseBody body = null;
        try {
            body = client.newCall(request).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;

    }
}
