package com.ocklund.bumpkis.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Change {
    private final Ref ref;
    private final String refId;
    private final String fromHash;
    private final String toHash;
    private final String type;

    @JsonCreator
    public Change(@JsonProperty("ref") Ref ref,
                  @JsonProperty("refId") String refId,
                  @JsonProperty("fromHash") String fromHash,
                  @JsonProperty("toHash") String toHash,
                  @JsonProperty("type") String type) {
        this.ref = ref;
        this.refId = refId;
        this.fromHash = fromHash;
        this.toHash = toHash;
        this.type = type;
    }

    @JsonProperty("ref")
    public Ref getRef() {
        return ref;
    }

    @JsonProperty("refId")
    public String getRefId() {
        return refId;
    }

    @JsonProperty("fromHash")
    public String getFromHash() {
        return fromHash;
    }

    @JsonProperty("toHash")
    public String getToHash() {
        return toHash;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
