package com.mschabow.flyrecipeserver.domain;


import javax.persistence.*;
import java.util.UUID;

@Entity
public class Ingredient {

    private String type;
    private String name;
    private String link;
    @Id
    @GeneratedValue
    private UUID id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
