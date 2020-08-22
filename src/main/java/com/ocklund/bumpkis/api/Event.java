package com.ocklund.bumpkis.api;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@JsonInclude(NON_NULL)
public class Event {

    private final String eventKey;
    private final String date;
    private final Actor actor;
    private final PullRequest pullRequest;
    private final Repository repository;
    private final List<Change> changes;

    @JsonCreator
    public Event(@JsonProperty("eventKey") String eventKey,
                 @JsonProperty("date") String date,
                 @JsonProperty("actor") Actor actor,
                 @JsonProperty("pullRequest") PullRequest pullRequest,
                 @JsonProperty("repository") Repository repository,
                 @JsonProperty("changes") List<Change> changes) {
        this.eventKey = eventKey;
        this.date = date;
        this.actor = actor;
        this.pullRequest = pullRequest;
        this.repository = repository;
        this.changes = changes;
    }

    @JsonProperty("eventKey")
    public String getEventKey() {
        return eventKey;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("actor")
    public Actor getActor() {
        return actor;
    }

    @JsonProperty("pullRequest")
    public PullRequest getPullRequest() {
        return pullRequest;
    }

    @JsonProperty("repository")
    public Repository getRepository() {
        return repository;
    }

    @JsonProperty("changes")
    public List<Change> getChanges() {
        return changes;
    }

    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
