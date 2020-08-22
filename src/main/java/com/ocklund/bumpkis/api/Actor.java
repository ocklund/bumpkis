package com.ocklund.bumpkis.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Actor {
    private final String name;
    private final String emailAddress;
    private final int id;
    private final String displayName;
    private final boolean active;
    private final String slug;
    private final String type;
    private final Links links;

    @JsonCreator
    public Actor(@JsonProperty("name") String name,
                 @JsonProperty("emailAddress") String emailAddress,
                 @JsonProperty("id") int id,
                 @JsonProperty("displayName") String displayName,
                 @JsonProperty("active") boolean active,
                 @JsonProperty("slug") String slug,
                 @JsonProperty("type") String type,
                 @JsonProperty("links") Links links) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.id = id;
        this.displayName = displayName;
        this.active = active;
        this.slug = slug;
        this.type = type;
        this.links = links;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("emailAddress")
    public String getEmailAddress() {
        return emailAddress;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("active")
    public boolean isActive() {
        return active;
    }

    @JsonProperty("slug")
    public String getSlug() {
        return slug;
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
