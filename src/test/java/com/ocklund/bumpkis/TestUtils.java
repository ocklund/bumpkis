package com.ocklund.bumpkis;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TestUtils {

    public static String readFile(String fileName) {
        InputStream inputStream = TestUtils.class.getClassLoader().getResourceAsStream(fileName);
        String s = null;
        if (inputStream != null) {
            try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
                s = scanner.useDelimiter("\\A").next();
            }
        }
        return s;
    }
}
