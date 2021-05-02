package com.mschabow.flyrecipeserver.domain.YoutubeResult;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class YouTubeItem {


    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String generatedId;

    public void setId(YouTubeId id) {
        this.id = id;
    }

    public YouTubeId getId() {
        return id;
    }

    @Embedded
    @JsonProperty("id")
    private YouTubeId id;


    public void setGeneratedId(String generatedId) {
        this.generatedId = generatedId;
    }

    public String getGeneratedId() {
        return generatedId;
    }
}
