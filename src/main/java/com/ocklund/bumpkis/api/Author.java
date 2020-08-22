package com.ocklund.bumpkis.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Author {
    private final Actor user;
    private final String role;
    private final boolean approved;
    private final String status;

    @JsonCreator
    public Author(@JsonProperty("user") Actor user,
                 @JsonProperty("role") String role,
                 @JsonProperty("approved") boolean approved,
                 @JsonProperty("status") String status) {
        this.user = user;
        this.role = role;
        this.approved = approved;
        this.status = status;
    }

    @JsonProperty("user")
    public Actor getUser() {
        return user;
    }

    @JsonProperty("role")
    public String getRole() {
        return role;
    }

    @JsonProperty("approved")
    public boolean isApproved() {
        return approved;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
