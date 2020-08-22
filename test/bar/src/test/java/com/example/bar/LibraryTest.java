package com.example.bar;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LibraryTest {

    @Test
    void shouldReturnTrue() {
        Library target = new Library();
        assertTrue(target.someLibraryMethod(), "should return 'true'");
    }
}
