package com.ocklund.bumpkis.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Repository {
    private final String slug;
    private final int id;
    private final String name;
    private final String description;
    private final String scmId;
    private final String state;
    private final String statusMessage;
    private final boolean forkable;
    private final Project project;
    private final boolean repositoryPublic;
    private final Links links;

    @JsonCreator
    public Repository(@JsonProperty("slug") String slug,
                      @JsonProperty("id") int id,
                      @JsonProperty("name") String name,
                      @JsonProperty("description") String description,
                      @JsonProperty("scmId") String scmId,
                      @JsonProperty("state") String state,
                      @JsonProperty("statusMessage") String statusMessage,
                      @JsonProperty("forkable") boolean forkable,
                      @JsonProperty("project") Project project,
                      @JsonProperty("public") boolean repositoryPublic,
                      @JsonProperty("links") Links links) {
        this.slug = slug;
        this.id = id;
        this.name = name;
        this.description = description;
        this.scmId = scmId;
        this.state = state;
        this.statusMessage = statusMessage;
        this.forkable = forkable;
        this.project = project;
        this.repositoryPublic = repositoryPublic;
        this.links = links;
    }

    public Repository(String slug, String name, Project project) {
        this.slug = slug;
        this.id = 0;
        this.name = name;
        this.description = null;
        this.scmId = null;
        this.state = null;
        this.statusMessage = null;
        this.forkable = false;
        this.project = project;
        this.repositoryPublic = false;
        this.links = null;
    }

    @JsonProperty("slug")
    public String getSlug() {
        return slug;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("scmId")
    public String getScmId() {
        return scmId;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("statusMessage")
    public String getStatusMessage() {
        return statusMessage;
    }

    @JsonProperty("forkable")
    public boolean isForkable() {
        return forkable;
    }

    @JsonProperty("project")
    public Project getProject() {
        return project;
    }

    @JsonProperty("public")
    public boolean isRepositoryPublic() {
        return repositoryPublic;
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
