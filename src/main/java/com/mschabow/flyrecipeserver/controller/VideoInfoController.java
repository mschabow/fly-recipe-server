package com.mschabow.flyrecipeserver.controller;


import com.mschabow.flyrecipeserver.domain.YoutubeVideoInfo.VideoInfo;
import com.mschabow.flyrecipeserver.service.VideoInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class VideoInfoController {
    VideoInfoService service;

    @Autowired
    public VideoInfoController(VideoInfoService service) {
        this.service = service;
    }

    @GetMapping("/videoInfo/")
    public Iterable<VideoInfo> list(){
        return service.list();
    }
}
