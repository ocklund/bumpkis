package com.ocklund.bumpkis.api;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LinksTest {

    @Test
    public void shouldAllowMissingCloneProperty() {
        String expected = """
                {"self":[{"foo":"bar"}]}""";
        Links links = new Links(null, singletonList(singletonMap("foo", "bar")));
        assertEquals(expected, links.toString());
    }
}
