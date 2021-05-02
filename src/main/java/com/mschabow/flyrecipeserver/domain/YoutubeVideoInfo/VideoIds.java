package com.mschabow.flyrecipeserver.domain.YoutubeVideoInfo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class VideoIds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ElementCollection
    public List<String> VideoIds = new ArrayList<>();

}
