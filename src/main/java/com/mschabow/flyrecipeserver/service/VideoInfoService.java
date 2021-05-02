package com.mschabow.flyrecipeserver.service;

import com.mschabow.flyrecipeserver.domain.YoutubeVideoInfo.VideoInfo;
import com.mschabow.flyrecipeserver.repository.VideoInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoInfoService {

    private VideoInfoRepository videoInfoRepository;

    public VideoInfoService(VideoInfoRepository videoInfoRepository) {
        this.videoInfoRepository = videoInfoRepository;
    }

    public Iterable<VideoInfo> list(){
        return videoInfoRepository.findAll();
    }

    public VideoInfo save(VideoInfo videoInfo){
        return  videoInfoRepository.save(videoInfo);
    }

    public Iterable<VideoInfo> save(List<VideoInfo> videos){
        return videoInfoRepository.saveAll(videos);
    }


}
