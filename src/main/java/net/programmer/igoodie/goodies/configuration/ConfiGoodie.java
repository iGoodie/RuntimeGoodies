package net.programmer.igoodie.goodies.configuration;

import net.programmer.igoodie.goodies.configuration.validation.FixReason;
import net.programmer.igoodie.goodies.configuration.validation.GoodieValidator;
import net.programmer.igoodie.goodies.exception.GoodieParseException;
import net.programmer.igoodie.goodies.format.GoodieFormat;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.serialization.ConfiGoodieDeserializer;
import net.programmer.igoodie.goodies.serialization.ConfiGoodieSerializer;
import net.programmer.igoodie.goodies.serialization.Serializable;
import net.programmer.igoodie.goodies.util.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public abstract class ConfiGoodie<F extends GoodieFormat<?, GoodieObject>> implements Serializable<GoodieObject> {

    public enum State {
        PRISTINE,
        LOADING,
        READY
    }

    private State state = State.PRISTINE;
    private GoodieObject lastAppliedGoodieObject;
    private GoodieValidator validator;
    private List<FixReason> generalFixesDone;

    public GoodieObject getLastAppliedGoodieObject() {
        return lastAppliedGoodieObject;
    }

    public Collection<FixReason> getFixesDone() {
        List<FixReason> fixesDone = new LinkedList<>();
        fixesDone.addAll(generalFixesDone);
        fixesDone.addAll(validator.getFixesDone());
        return fixesDone;
    }

    public State getState() {
        return state;
    }

    public abstract F getFormat();

    /* -- Config Readers ------------------------------- */

    private GoodieObject readGoodieObject(ConfiGoodieOptions options) {
        try {
            F goodieFormat = getFormat();
            return goodieFormat.readGoodieFromString(options.externalConfigText);

        } catch (GoodieParseException exception) {
            generalFixesDone.add(new FixReason("$", FixReason.Action.RESET_TO_DEFAULT_SCHEME, exception.getMessage()));
            return new GoodieObject(); // Discard malformed config data
        }
    }

    public <T extends ConfiGoodie<F>> T readConfig(File file) {
        return readConfig(ConfiGoodieOptions.fromFile(file));
    }

    public <T extends ConfiGoodie<F>> T readConfig(String externalText) {
        return readConfig(ConfiGoodieOptions.fromText(externalText));
    }

    public <T extends ConfiGoodie<F>> T readConfig(ConfiGoodieOptions options) throws GoodieParseException {
        options.externalConfigFetcher.run();

        if (options.externalConfigText == null) {
            throw new IllegalArgumentException("Passed options do not contain any config data...");
        }

        state = State.LOADING;

        generalFixesDone = new ArrayList<>();

        // Read goodie object from the external config format
        lastAppliedGoodieObject = readGoodieObject(options);

        // Validate and fix loaded goodie object
        validator = new GoodieValidator(this, lastAppliedGoodieObject);
        validator.validateAndFixFields();

        // Deserialize underlying goodie object into fields
        deserialize(lastAppliedGoodieObject);

        // If changes are made, handle the modified goodie object
        if (!getFixesDone().isEmpty() && options.onFixed != null) {
            options.onFixed.accept(options, lastAppliedGoodieObject, this);
        }

        state = State.READY;

        @SuppressWarnings("unchecked")
        T thisConfig = (T) this;
        return thisConfig;
    }

    /* -- File Savers ------------------------------- */

    public void saveToFile(File file) {
        GoodieObject goodieObject = serialize();
        saveToFile(file, goodieObject);
    }

    private void saveToFile(File file, GoodieObject goodieObject) {
        F goodieFormat = getFormat();
        String serializedGoodie = goodieFormat.writeToString(goodieObject, true);
        FileUtils.writeString(file, serializedGoodie, StandardCharsets.UTF_8);
    }

    public boolean isDirty() {
        if (state == State.PRISTINE) return false;
        GoodieObject currentGoodie = serialize();
        return !currentGoodie.equals(lastAppliedGoodieObject);
    }

    /* -- Implementation Helpers ------------------------------- */

    /**
     * Used while implementing your own ConfiGoodie. <br/>
     * When the default value is hard to write as a one-liner, use this protected method to generate <br/>
     * <br/>
     * Example Usage:
     * <pre> {@code
     * @Goodie
     * List<String> myListStuff = defaultValue(() -> {
     *   List<String> value = new LinkedList<>();
     *   value.add("A");
     *   value.add("B");
     *   value.add("C");
     *   return value;
     * })
     * }</pre>
     */
    protected final <T> T defaultValue(Supplier<T> supplier) {
        return supplier.get();
    }

    /* -- Serializers ------------------------------- */

    @Override
    public GoodieObject serialize() {
        return new ConfiGoodieSerializer().serializeFrom(this);
    }

    @Override
    public void deserialize(GoodieObject serialized) {
        new ConfiGoodieDeserializer().deserializeInto(this, serialized);
    }

    /* -- Instancing Helpers ------------------------------- */

    public static <F extends GoodieFormat<?, GoodieObject>, C extends ConfiGoodie<F>> ConfiGoodieHolder<C> readConfig(Class<C> configClass, File file) {
        return readConfig(configClass, ConfiGoodieOptions.fromFile(file));
    }

    public static <F extends GoodieFormat<?, GoodieObject>, C extends ConfiGoodie<F>> ConfiGoodieHolder<C> readConfig(Class<C> configClass, String externalText) {
        return readConfig(configClass, ConfiGoodieOptions.fromText(externalText));
    }

    public static <F extends GoodieFormat<?, GoodieObject>, C extends ConfiGoodie<F>> ConfiGoodieHolder<C> readConfig(Class<C> configClass, ConfiGoodieOptions options) {
        ConfiGoodieHolder<C> holder = new ConfiGoodieHolder<>(configClass);
        holder.get().readConfig(options);
        return holder;
    }

}
