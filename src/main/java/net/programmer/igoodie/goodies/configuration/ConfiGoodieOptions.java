package net.programmer.igoodie.goodies.configuration;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ConfiGoodieOptions {

    protected Runnable externalConfigFetcher;
    protected GoodieFixedConsumer onFixed;
    protected String externalConfigLocation;
    protected String externalConfigText;
    protected boolean trimExcessiveFields;

    private ConfiGoodieOptions() {}

    @FunctionalInterface
    public interface GoodieFixedConsumer {
        void accept(ConfiGoodieOptions options, GoodieObject fixedGoodie, ConfiGoodie<?> confiGoodie);
    }

    public ConfiGoodieOptions onFixed(GoodieFixedConsumer onFixed) {
        this.onFixed = onFixed;
        return this;
    }

    public ConfiGoodieOptions trimExcessiveFields() {
        this.trimExcessiveFields = true;
        return this;
    }

    /* ---------------------- */

    public static ConfiGoodieOptions fromURL(URL url) {
        ConfiGoodieOptions options = new ConfiGoodieOptions();

        options.externalConfigFetcher = () -> {
            StringBuilder externalText = new StringBuilder();

            options.externalConfigLocation = url.toString();

            try (InputStream inputStream = url.openStream()) {
                Scanner scanner = new Scanner(inputStream);
                while (scanner.hasNext()) {
                    externalText.append(scanner.nextLine()).append("\n");
                }
                options.externalConfigText = externalText.toString();

            } catch (IOException e) {
                options.externalConfigText = "";
            }
        };

        return options;
    }

    public static ConfiGoodieOptions fromFile(File externalConfigFile) {
        ConfiGoodieOptions options = new ConfiGoodieOptions();

        options.externalConfigFetcher = () -> {
            options.externalConfigLocation = externalConfigFile.toURI().toString();
            FileUtils.createFileIfAbsent(externalConfigFile);

            options.externalConfigText = FileUtils.readString(externalConfigFile);
        };

        return options;
    }

    public static ConfiGoodieOptions fromText(String externalConfigText) {
        ConfiGoodieOptions options = new ConfiGoodieOptions();

        options.externalConfigFetcher = () -> {
            options.externalConfigText = externalConfigText;
        };

        return options;
    }

    /* ---------------------- */

    public static class FixHandlers {

        @FunctionalInterface
        public interface FileNameSupplier {
            String accept(File invalidConfigFile, GoodieObject fixedGoodie);
        }

        public static GoodieFixedConsumer renameInvalidConfig(FileNameSupplier renamer) {
            return (options, fixedGoodie, confiGoodie) -> {
                if (!options.externalConfigLocation.startsWith("file:/"))
                    return; // This handler is not meant for non-tile locations

                URI currentFileLocation = URI.create(options.externalConfigLocation);
                File currentFile = new File(currentFileLocation);

                File relocateFile = new File(renamer.accept(currentFile, fixedGoodie));

                // Move current one to relocated location
                FileUtils.createFileIfAbsent(relocateFile);
                FileUtils.moveFile(currentFile, relocateFile);

                // Overwrite fixed file
                FileUtils.writeString(currentFile, confiGoodie.getFormat().writeToString(fixedGoodie, true),
                        StandardCharsets.UTF_8);
            };
        }

    }

}
