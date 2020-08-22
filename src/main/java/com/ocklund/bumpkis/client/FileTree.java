package com.ocklund.bumpkis.client;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

/**
 * https://docs.atlassian.com/bitbucket-server/rest/6.9.0/bitbucket-rest.html#idp243
 */
@JsonInclude(NON_NULL)
public class FileTree {

    private List<String> values;
    private Integer size;
    private Boolean isLastPage;
    private Integer start;
    private Integer limit;
    private Integer nextPageStart;

    @JsonCreator
    public FileTree(@JsonProperty("values") List<String> values, @JsonProperty("size") Integer size,
        @JsonProperty("isLastPage") Boolean isLastPage, @JsonProperty("start") Integer start,
        @JsonProperty("limit") Integer limit, @JsonProperty("nextPageStart") Integer nextPageStart) {
        this.values = values;
        this.size = size;
        this.isLastPage = isLastPage;
        this.start = start;
        this.limit = limit;
        this.nextPageStart = nextPageStart;
    }

    @JsonProperty("values")
    public List<String> getValues() {
        return values;
    }

    @JsonProperty("size")
    public Integer getSize() {
        return size;
    }

    @JsonProperty("lastPage")
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
