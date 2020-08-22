package com.ocklund.bumpkis.client;

import static com.ocklund.bumpkis.TestUtils.readFile;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class FileUpdatedTest {

    @Test
    public void shouldDeserialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = readFile("fixtures/file-updated.json");
        FileUpdated fileUpdated = mapper.readValue(jsonString, FileUpdated.class);

        assertThat(fileUpdated.getId(), is("b3e33e4e095ed8d8194665b3fc5a54cb3bcc8922"));
    }
}