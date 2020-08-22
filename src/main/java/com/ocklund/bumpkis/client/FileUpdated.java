package com.ocklund.bumpkis.client;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.ocklund.bumpkis.api.Actor;
import com.ocklund.bumpkis.api.Ref;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

/**
 * A file update response.
 * See https://docs.atlassian.com/bitbucket-server/rest/5.0.1/bitbucket-rest.html#idm45993793705776
 */
@JsonInclude(NON_NULL)
public class FileUpdated {
    private final String id;
    private final String displayId;
    private final Actor author;
    private final Long authorTimestamp;
    private final Actor committer;
    private final Long committerTimestamp;
    private final String message;
    private final List<Ref> parents;

    @JsonCreator
    public FileUpdated(@JsonProperty("id") String id,
        @JsonProperty("displayId") String displayId,
        @JsonProperty("author") Actor author,
        @JsonProperty("authorTimestamp") Long authorTimestamp,
        @JsonProperty("committer") Actor committer,
        @JsonProperty("committerTimestamp") Long committerTimestamp,
        @JsonProperty("message") String message,
        @JsonProperty("parents") List<Ref> parents) {
        this.id = id;
        this.displayId = displayId;
        this.author = author;
        this.authorTimestamp = authorTimestamp;
        this.committer = committer;
        this.committerTimestamp = committerTimestamp;
        this.message = message;
        this.parents = parents;
    }

    public FileUpdated(String id) {
        this.id = id;
        this.displayId = null;
        this.author = null;
        this.authorTimestamp = 0L;
        this.committer = null;
        this.committerTimestamp = 0L;
        this.message = null;
        this.parents = null;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("displayId")
    public String getDisplayId() {
        return displayId;
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
    public List<Ref> getParents() {
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
