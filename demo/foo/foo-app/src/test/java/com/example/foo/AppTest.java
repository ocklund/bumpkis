package com.example.foo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void appHasAGreeting() {
        App target = new App();
        assertNotNull(target.getGreeting(), "app should have a greeting");
    }
}
