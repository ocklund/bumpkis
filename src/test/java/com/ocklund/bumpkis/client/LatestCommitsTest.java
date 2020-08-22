package com.ocklund.bumpkis.client;

import static com.ocklund.bumpkis.TestUtils.readFile;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class LatestCommitsTest {

    @Test
    public void shouldDeserialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String latestCommitsString = readFile("fixtures/latest-commits.json");
        LatestCommits latestCommits = mapper.readValue(latestCommitsString, LatestCommits.class);

        assertThat(latestCommits.getSize(), is(1));
    }
}