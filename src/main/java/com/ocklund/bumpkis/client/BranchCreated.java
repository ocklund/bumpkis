package com.ocklund.bumpkis.client;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(NON_NULL)
public class BranchCreated {
    private final String id;
    private final String displayId;
    private final String type;
    private final String latestCommit;
    private final String latestChangeset;
    private final boolean isDefault;

    @JsonCreator
    public BranchCreated(@JsonProperty("id") String id,
        @JsonProperty("displayId") String displayId,
        @JsonProperty("type") String type,
        @JsonProperty("latestCommit") String latestCommit,
        @JsonProperty("latestChangeset") String latestChangeset,
        @JsonProperty("isDefault") boolean isDefault) {
        this.id = id;
        this.displayId = displayId;
        this.type = type;
        this.latestCommit = latestCommit;
        this.latestChangeset = latestChangeset;
        this.isDefault = isDefault;
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

    @JsonProperty("latestChangeset")
    public String getLatestChangeset() {
        return latestChangeset;
    }

    @JsonProperty("isDefault")
    public boolean isDefault() {
        return isDefault;
    }

    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
