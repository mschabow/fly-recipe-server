package com.mschabow.flyrecipeserver.repository;

import com.mschabow.flyrecipeserver.domain.YoutubeVideoInfo.VideoInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoInfoRepository extends CrudRepository<VideoInfo, String> {



}
