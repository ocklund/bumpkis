package com.ocklund.bumpkis.api;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@JsonInclude(NON_NULL)
public class PullRequest {
    private final int id;
    private final int version;
    private final String title;
    private final String description;
    private final String state;
    private final boolean open;
    private final boolean closed;
    private final long createdDate;
    private final long updatedDate;
    private final long closedDate;
    private final Ref fromRef;
    private final Ref toRef;
    private final boolean locked;
    private final Author author;
    private final List<Actor> reviewers;
    private final List<Actor> participants;
    private final Properties properties;
    private final Links links;


    @JsonCreator
    public PullRequest(@JsonProperty("id") int id,
                   @JsonProperty("version") int version,
                   @JsonProperty("title") String title,
                   @JsonProperty("description") String description,
                   @JsonProperty("state") String state,
                   @JsonProperty("open") boolean open,
                   @JsonProperty("closed") boolean closed,
                   @JsonProperty("createdDate") long createdDate,
                   @JsonProperty("updatedDate") long updatedDate,
                   @JsonProperty("closedDate") long closedDate,
                   @JsonProperty("fromRef") Ref fromRef,
                   @JsonProperty("toRef") Ref toRef,
                   @JsonProperty("locked") boolean locked,
                   @JsonProperty("author") Author author,
                   @JsonProperty("reviewers") List<Actor> reviewers,
                   @JsonProperty("participants") List<Actor> participants,
                   @JsonProperty("properties") Properties properties,
                   @JsonProperty("links") Links links) {
        this.id = id;
        this.version = version;
        this.title = title;
        this.description = description;
        this.state = state;
        this.open = open;
        this.closed = closed;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.closedDate = closedDate;
        this.fromRef = fromRef;
        this.toRef = toRef;
        this.locked = locked;
        this.author = author;
        this.reviewers = reviewers;
        this.participants = participants;
        this.properties = properties;
        this.links = links;
    }

    public PullRequest(String title, String description, String state, boolean open, boolean closed, Ref fromRef, Ref toRef, boolean locked, List<Actor> reviewers) {
        this.id = 0;
        this.version = 0;
        this.title = title;
        this.description = description;
        this.state = state;
        this.open = open;
        this.closed = closed;
        this.createdDate = 0;
        this.updatedDate = 0;
        this.closedDate = 0;
        this.fromRef = fromRef;
        this.toRef = toRef;
        this.locked = locked;
        this.author = null;
        this.reviewers = reviewers;
        this.participants = null;
        this.properties = null;
        this.links = null;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("version")
    public int getVersion() {
        return version;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("open")
    public boolean isOpen() {
        return open;
    }

    @JsonProperty("closed")
    public boolean isClosed() {
        return closed;
    }

    @JsonProperty("createdDate")
    public long getCreatedDate() {
        return createdDate;
    }

    @JsonProperty("updatedDate")
    public long getUpdatedDate() {
        return updatedDate;
    }

    @JsonProperty("closedDate")
    public long getClosedDate() {
        return closedDate;
    }

    @JsonProperty("fromRef")
    public Ref getFromRef() {
        return fromRef;
    }

    @JsonProperty("toRef")
    public Ref getToRef() {
        return toRef;
    }

    @JsonProperty("locked")
    public boolean isLocked() {
        return locked;
    }

    @JsonProperty("author")
    public Author getAuthor() {
        return author;
    }

    @JsonProperty("reviewers")
    public List<Actor> getReviewers() {
        return reviewers;
    }

    @JsonProperty("participants")
    public List<Actor> getParticipants() {
        return participants;
    }

    @JsonProperty("properties")
    public Properties getProperties() {
        return properties;
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
