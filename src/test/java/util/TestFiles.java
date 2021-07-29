package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class TestFiles {

    public static String loadData(String filename) throws IOException {
        return loadFile("data/" + filename);
    }

    private static String loadFile(String path) throws IOException {
        ClassLoader classLoader = TestFiles.class.getClassLoader();
        InputStream resourceStream = classLoader.getResourceAsStream(path);
        if (resourceStream == null) return "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream, StandardCharsets.UTF_8));
        return reader.lines().collect(Collectors.joining("\n"));
    }

}
