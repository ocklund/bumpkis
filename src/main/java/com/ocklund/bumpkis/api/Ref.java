package com.ocklund.bumpkis.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Ref {
    private final String id;
    private final String displayId;
    private final String type;
    private final String latestCommit;
    private final Repository repository;

    @JsonCreator
    public Ref(@JsonProperty("id") String id,
               @JsonProperty("displayId") String displayId,
               @JsonProperty("type") String type,
               @JsonProperty("latestCommit") String latestCommit,
               @JsonProperty("repository") Repository repository) {
        this.id = id;
        this.displayId = displayId;
        this.type = type;
        this.latestCommit = latestCommit;
        this.repository = repository;
    }

    public Ref(String id, Repository repository) {
        this.id = id;
        this.displayId = null;
        this.type = null;
        this.latestCommit = null;
        this.repository = repository;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("displayId")
    public String getDisplayId() {
        return displayId;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("latestCommit")
    public String getLatestCommit() {
        return latestCommit;
    }

    @JsonProperty("repository")
    public Repository getRepository() {
        return repository;
    }

    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
