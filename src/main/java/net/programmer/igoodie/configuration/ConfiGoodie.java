package net.programmer.igoodie.configuration;

import net.programmer.igoodie.configuration.validation.GoodieValidator;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.serialization.GoodieObjectifier;
import net.programmer.igoodie.util.FileUtils;

import java.io.File;
import java.util.function.Consumer;

public abstract class ConfiGoodie<F extends GoodieFormat<?, GoodieObject>> {

    public abstract F getFormat();

    public <T extends ConfiGoodie<F>> T readConfig(File file) {
        return readConfig(file, fixedGoodie -> {
            // TODO: Override File
        });
    }

    public <T extends ConfiGoodie<F>> T readConfig(File file, Consumer<GoodieObject> onFixed) {
        return readConfig(FileUtils.readString(file), onFixed);
    }

    public <T extends ConfiGoodie<F>> T readConfig(String externalFormat) {
        return readConfig(externalFormat, fixedGoodie -> {});
    }

    @SuppressWarnings("unchecked")
    public <T extends ConfiGoodie<F>> T readConfig(String json, Consumer<GoodieObject> onFixed) {
        F format = getFormat();
        GoodieValidator goodieValidator = new GoodieValidator();
        GoodieObjectifier goodieObjectifier = new GoodieObjectifier();

        GoodieObject goodieObject = format.readGoodieFromString(json);

        goodieValidator.validateAndFix(this, goodieObject);

        if (goodieValidator.changesMade()) {
            onFixed.accept(goodieObject);
        }

        goodieObjectifier.fillFields(this, goodieObject);

        return (T) this;
    }

}
