package net.programmer.igoodie.configuration;

import net.programmer.igoodie.configuration.validation.GoodieValidator;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.query.GoodieQuery;
import net.programmer.igoodie.serialization.GoodieObjectifier;
import net.programmer.igoodie.util.FileUtils;
import net.programmer.igoodie.util.ReflectionUtilities;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public abstract class ConfiGoodie<F extends GoodieFormat<?, GoodieObject>> {

    public abstract F getFormat();

    public <T extends ConfiGoodie<F>> T readConfig(File file) {
        return readConfig(new ConfiGoodieOptions().useFile(file));
    }

    public <T extends ConfiGoodie<F>> T readConfig(String externalText) {
        return readConfig(new ConfiGoodieOptions().useText(externalText));
    }

    @SuppressWarnings("unchecked")
    public <T extends ConfiGoodie<F>> T readConfig(ConfiGoodieOptions options) {
        if (options.externalConfigText == null) {
            throw new IllegalArgumentException("Passed options do not contain any config data...");
        }

        F goodieFormat = getFormat();
        GoodieValidator goodieValidator = new GoodieValidator();
        GoodieObjectifier goodieObjectifier = new GoodieObjectifier();

        // Read goodie object from the external config format
        GoodieObject goodieObject = goodieFormat.readGoodieFromString(options.externalConfigText);

        // Validate and fix loaded goodie object
        goodieValidator.validateAndFix(this, goodieObject);

        // Fill the fields in this config object with (possibly) fixed goodie object
        goodieObjectifier.fillFields(this, goodieObject, (object, field, goodiePath, e) -> {
            // Reset value in Goodie Object
            GoodieQuery.set(goodieObject, goodiePath, null);

            // Replace reset value with default
            goodieValidator.fixMissingValue(object, field, goodieObject, goodiePath);

            // Put back the fixed/corrected value
            GoodieElement fixedValue = GoodieQuery.query(goodieObject, goodiePath);
            Object objectifiedValue = goodieObjectifier.generate(field, fixedValue);
            ReflectionUtilities.setValue(object, field, objectifiedValue);
        });

        // If changes are made, handle the modified goodie object
        if (goodieValidator.changesMade()) {
            if (options.onFixed != null) {
                options.onFixed.accept(goodieObject);

            } else if (options.externalConfigFile != null && options.renameInvalidConfig != null) {
                String serializedGoodie = goodieFormat.writeToString(goodieObject, true);
                if (!FileUtils.isEmpty(options.externalConfigFile)) {
                    String movePath = options.renameInvalidConfig.accept(options.externalConfigFile, goodieObject);
                    FileUtils.moveFile(options.externalConfigFile, new File(movePath));
                    FileUtils.createFileIfAbsent(options.externalConfigFile);
                }
                FileUtils.writeString(options.externalConfigFile, serializedGoodie, StandardCharsets.UTF_8);
            }
        }

        return (T) this;
    }

    protected  <T> T defaultValue(Supplier<T> supplier) {
        return supplier.get();
    }

}
