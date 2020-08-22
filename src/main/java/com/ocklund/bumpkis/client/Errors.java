package com.ocklund.bumpkis.client;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(NON_NULL)
public class Errors {
    private final List<Error> errors;

    @JsonCreator
    public Errors (@JsonProperty("errors") List<Error> errors) {
        this.errors = errors;
    }

    @JsonProperty("errors")
    public List<Error> getErrors() {
        return errors;
    }

    @JsonInclude(NON_NULL)
    public class Error {
        private final String context;
        private final String message;

        public Error(@JsonProperty("context") String context, @JsonProperty("context") String message) {
            this.context = context;
            this.message = message;
        }

        @JsonProperty("context")
        public String getContext() {
            return context;
        }

        @JsonProperty("message")
        public String getMessage() {
            return message;
        }
    }
}

