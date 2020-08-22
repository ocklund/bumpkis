package com.ocklund.bumpkis.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Project {
    private final String key;
    private final int id;
    private final String name;
    private final String description;
    private final boolean projectPublic;
    private final String type;
    private final Links links;

    @JsonCreator
    public Project(@JsonProperty("key") String key,
                   @JsonProperty("id") int id,
                   @JsonProperty("name") String name,
                   @JsonProperty("description") String description,
                   @JsonProperty("public") boolean projectPublic,
                   @JsonProperty("type") String type,
                   @JsonProperty("links") Links links) {
        this.key = key;
        this.id = id;
        this.name = name;
        this.description = description;
        this.projectPublic = projectPublic;
        this.type = type;
        this.links = links;
    }

    public Project(String key) {
        this.key = key;
        this.id = 0;
        this.name = null;
        this.description = null;
        this.projectPublic = false;
        this.type = null;
        this.links = null;
    }

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("public")
    public boolean isProjectPublic() {
        return projectPublic;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("links")
    public Links getLinks() {
        return links;
    }

    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
