package com.ocklund.bumpkis.core;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

class Utils {

    private Utils() {}

    public static String cleanUpFileTreeLine(String line) {
        return line.replace("\"", "").replace(",", "").trim();
    }

    public static List<String> cleanUpValues(List<String> values) {
        return values.stream().map(v -> v.replace("'", "").trim()).collect(toList());
    }

    public static String removeVersion(String dependency) {
        return dependency.chars().filter(ch -> ch == ':').count() == 2 ?
            dependency.substring(0, dependency.lastIndexOf(':')) : dependency;
    }

    public static String getVersion(String groupNameVersion) {
        return groupNameVersion.chars().filter(ch -> ch == ':').count() == 2 ?
            groupNameVersion.substring(groupNameVersion.lastIndexOf(':') + 1) : "NO_VERSION";
    }

    public static List<String> getValueForKeyInString(String key, String fileContents) {
        List<String> lines = Arrays.asList(fileContents.split("\\n"));
        return lines.stream().filter(line -> line.trim().startsWith(key)).map(line -> {
            String trimmedLine = line.trim();
            if (trimmedLine.contains("=")) {
                return trimmedLine.split("=")[1];
            } else {
                return trimmedLine.split(" ")[1];
            }
        }).collect(toList());
    }
}
