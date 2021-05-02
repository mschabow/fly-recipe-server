package com.mschabow.flyrecipeserver.service;

import com.mschabow.flyrecipeserver.domain.YoutubeResult.YouTubeItem;
import com.mschabow.flyrecipeserver.repository.YouTubeItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YouTubeItemService {

    private final YouTubeItemRepository resultRepository;

    @Autowired
    public YouTubeItemService(YouTubeItemRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public Iterable<YouTubeItem> list(){
        return resultRepository.findAll();
    }

    public YouTubeItem save(YouTubeItem videoInfo){
        return  resultRepository.save(videoInfo);
    }

    public Iterable<YouTubeItem> save(List<YouTubeItem> videos){
        return resultRepository.saveAll(videos);
    }

}
