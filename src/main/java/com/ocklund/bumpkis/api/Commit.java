package com.ocklund.bumpkis.api;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@JsonInclude(NON_NULL)
public class Commit {
    private final String displayId;
    private final String id;
    private final Actor author;
    private final Long authorTimestamp;
    private final Actor committer;
    private final Long committerTimestamp;
    private final String message;
    private final List<Commit> parents;

    @JsonCreator
    public Commit(
        @JsonProperty("displayId") String displayId,
        @JsonProperty("id") String id,
        @JsonProperty("author") Actor author,
        @JsonProperty("authorTimestamp") Long authorTimestamp,
        @JsonProperty("committer") Actor committer,
        @JsonProperty("committerTimestamp") Long committerTimestamp,
        @JsonProperty("message") String message,
        @JsonProperty("parents") List<Commit> parents
    ) {
        this.displayId = displayId;
        this.id = id;
        this.author = author;
        this.authorTimestamp = authorTimestamp;
        this.committer = committer;
        this.committerTimestamp = committerTimestamp;
        this.message = message;
        this.parents = parents;
    }

    public Commit(String displayId, String id) {
        this.displayId = displayId;
        this.id = id;
        this.author = null;
        this.authorTimestamp = null;
        this.committer = null;
        this.committerTimestamp = null;
        this.message = null;
        this.parents = null;
    }

    @JsonProperty("displayId")
    public String getDisplayId() {
        return displayId;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("author")
    public Actor getAuthor() {
        return author;
    }

    @JsonProperty("authorTimestamp")
    public Long getAuthorTimestamp() {
        return authorTimestamp;
    }

    @JsonProperty("committer")
    public Actor getCommitter() {
        return committer;
    }

    @JsonProperty("committerTimestamp")
    public Long getCommitterTimestamp() {
        return committerTimestamp;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("parents")
    public List<Commit> getParents() {
        return parents;
    }

    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
