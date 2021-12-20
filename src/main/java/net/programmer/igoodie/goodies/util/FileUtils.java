package net.programmer.igoodie.goodies.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.stream.Stream;

public class FileUtils {

    public static boolean createFileIfAbsent(File file) {
        if (file.exists()) return false;

        File parentDir = file.getParentFile();
        parentDir.mkdirs();

        try {return file.createNewFile();} catch (IOException e) {
            return false;
        }
    }

    public static boolean moveFile(File source, File target) {
        return moveFile(source.toPath(), target.toPath());
    }

    public static boolean moveFile(Path source, Path target) {
        try {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public static boolean isEmpty(File file) {
        String content = readString(file);
        return content == null || content.isEmpty();
    }

    public static String readString(File file) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));

        } catch (IOException e) {
            return null;
        }

        return contentBuilder.toString();
    }

    public static boolean writeString(File file, String string, Charset charset) {
        try {
            Files.write(file.toPath(), Arrays.asList(string.split("\\r?\\n")), charset);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
