package com.ocklund.bumpkis.client;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A last-modified response.
 * See https://docs.atlassian.com/bitbucket-server/rest/5.16.0/bitbucket-rest.html#idm8297563328
 */
@JsonInclude(NON_NULL)
public class LastModified {
    private final Files files;
    private final FileUpdated latestCommit;

    @JsonCreator
    public LastModified(@JsonProperty("files") Files files,
                        @JsonProperty("latestCommit") FileUpdated latestCommit) {
        this.files = files;
        this.latestCommit = latestCommit;
    }

    @JsonProperty("files")
    public Files getFiles() {
        return files;
    }

    @JsonProperty("latestCommit")
    public FileUpdated getLatestCommit() {
        return latestCommit;
    }

    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    public class Files {
        private final FileUpdated buildGradle;

        @JsonCreator
        public Files(@JsonProperty("build.gradle") FileUpdated buildGradle) {
            this.buildGradle = buildGradle;
        }

        @JsonProperty("build.gradle")
        public FileUpdated getBuildGradle() {
            return buildGradle;
        }
    }
}
