package com.ocklund.bumpkis.api;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;

public class Links {
    private final List<Map<String, String>> clone;
    private final List<Map<String, String>> self;

    @JsonCreator
    public Links(@JsonProperty("clone") List<Map<String, String>> clone,
                 @JsonProperty("self") List<Map<String, String>> self) {
        this.clone = clone;
        this.self = self;
    }

    @JsonProperty("clone")
    @JsonInclude(NON_NULL)
    public List<Map<String, String>> getClone() {
        return clone;
    }

    @JsonProperty("self")
    public List<Map<String, String>> getSelf() {
        return self;
    }

    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}

/*
    "links":{
      "clone":[
        {
          "href":"http://localhost:7990/bitbucket/scm/project_1/rep_1.git",
          "name":"http"
        },
        {
          "href":"ssh://git@localhost:7999/project_1/rep_1.git",
          "name":"ssh"
        }
      ],
      "self":[
        {
          "href":"http://localhost:7990/bitbucket/projects/PROJECT_1/repos/rep_1/browse"
        }
      ]
    }
  },

 */
