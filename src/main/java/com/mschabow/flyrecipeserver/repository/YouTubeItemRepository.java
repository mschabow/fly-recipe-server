package com.mschabow.flyrecipeserver.repository;


import com.mschabow.flyrecipeserver.domain.YoutubeResult.YouTubeItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YouTubeItemRepository extends CrudRepository<YouTubeItem, String>{

}
