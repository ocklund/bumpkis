package com.example.baz;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BazLibraryTest {

    @Test
    void shouldReturnTrue() {
        BazLibrary target = new BazLibrary();
        assertTrue(target.someLibraryMethod(), "should return 'true'");
    }
}
