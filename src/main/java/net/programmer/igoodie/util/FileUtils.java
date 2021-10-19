package net.programmer.igoodie.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Stream;

public class FileUtils {

    public static String readString(File file) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));

        } catch (IOException e) {
            return null;
        }

        return contentBuilder.toString();
    }


}
