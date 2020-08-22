package com.ocklund.bumpkis.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Properties {
    private final Commit mergeCommit;

    @JsonCreator
    public Properties(@JsonProperty("mergeCommit") Commit mergeCommit) {
        this.mergeCommit = mergeCommit;
    }

    @JsonProperty("mergeCommit")
    public Commit getMergeCommit() {
        return mergeCommit;
    }

    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
