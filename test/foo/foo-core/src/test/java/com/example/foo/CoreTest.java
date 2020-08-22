package com.example.foo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CoreTest {

    @Test
    void shouldBeCore() {
        Core target = new Core();
        assertTrue(target.isCore());
    }
}