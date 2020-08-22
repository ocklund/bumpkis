package com.ocklund.bumpkis.api;

import static com.ocklund.bumpkis.TestUtils.readFile;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class PullRequestTest {

    @Test
    public void shouldDeserialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = readFile("fixtures/pull-request.json");
        PullRequest pullRequest = mapper.readValue(jsonString, PullRequest.class);

        assertThat(pullRequest.getId(), is(5));
    }
}