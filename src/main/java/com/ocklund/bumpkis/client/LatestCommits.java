package com.ocklund.bumpkis.client;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.ocklund.bumpkis.api.Commit;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@JsonInclude(NON_NULL)
public class LatestCommits {
    private final List<Commit> values;
    private final Integer size;
    private final Boolean isLastPage;
    private final Integer start;
    private final Integer limit;
    private final Integer nextPageStart;

    @JsonCreator
    public LatestCommits(
        @JsonProperty("values") List<Commit> values,
        @JsonProperty("size") Integer size,
        @JsonProperty("isLastPage") Boolean isLastPage,
        @JsonProperty("start") Integer start,
        @JsonProperty("limit") Integer limit,
        @JsonProperty("nextPageStart") Integer nextPageStart
    ) {
        this.values = values;
        this.size = size;
        this.isLastPage = isLastPage;
        this.start = start;
        this.limit = limit;
        this.nextPageStart = nextPageStart;
    }

    public LatestCommits(List<Commit> values) {
        this.values = values;
        this.size = null;
        this.isLastPage = null;
        this.start = null;
        this.limit = null;
        this.nextPageStart = null;
    }

    @JsonProperty("values")
    public List<Commit> getValues() {
        return values;
    }

    @JsonProperty("size")
    public Integer getSize() {
        return size;
    }

    @JsonProperty("isLastPage")
    public Boolean getLastPage() {
        return isLastPage;
    }

    @JsonProperty("start")
    public Integer getStart() {
        return start;
    }

    @JsonProperty("limit")
    public Integer getLimit() {
        return limit;
    }

    @JsonProperty("nextPageStart")
    public Integer getNextPageStart() {
        return nextPageStart;
    }

    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
