package net.programmer.igoodie.goodies.format;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import net.programmer.igoodie.goodies.exception.GoodieParseException;
import net.programmer.igoodie.goodies.runtime.GoodieArray;
import net.programmer.igoodie.goodies.runtime.GoodieElement;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.runtime.GoodiePrimitive;
import net.programmer.igoodie.goodies.util.TypeUtilities;

import java.util.*;

public class TomlGoodieFormat extends GoodieFormat<Toml, GoodieObject> {

    @Override
    public GoodieObject writeToGoodie(Toml externalFormat) {
        return convertObject(externalFormat.toMap());
    }

    public static GoodieElement convert(Object tomlElement) {
        if (tomlElement instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> tomlObject = (Map<String, Object>) tomlElement;
            return convertObject(tomlObject);
        }
        if (tomlElement instanceof Collection)
            return convertArray(((Collection<?>) tomlElement));
        if (tomlElement instanceof Date)
            return GoodiePrimitive.from(((Date) tomlElement).toInstant().toString());
        if (TypeUtilities.isPrimitive(tomlElement.getClass()))
            return GoodiePrimitive.from(tomlElement);

        return null; // <-- No corresponding Goodie type exists
    }

    public static GoodieObject convertObject(Map<String, Object> tomlObject) {
        GoodieObject goodieObject = new GoodieObject();
        tomlObject.forEach((propertyName, value) -> {
            GoodieElement goodieElement = convert(value);
            goodieObject.put(propertyName, goodieElement);
        });
        return goodieObject;
    }

    public static GoodieArray convertArray(Collection<?> tomlList) {
        GoodieArray goodieArray = new GoodieArray();
        for (Object tomlElement : tomlList) {
            GoodieElement goodieElement = convert(tomlElement);
            goodieArray.add(goodieElement);
        }
        return goodieArray;
    }

    /* ------------------------------------------ */

    @Override
    public Toml readFromGoodie(GoodieObject goodie) {
        TomlWriter tomlWriter = new TomlWriter();
        Map<String, Object> objectMap = convertObject(goodie);
        return readFromString(tomlWriter.write(objectMap));
    }

    public static Object convert(GoodieElement goodieElement) {
        if (goodieElement.isObject())
            return convertObject(goodieElement.asObject());
        if (goodieElement.isArray())
            return convertArray(goodieElement.asArray());
        if (goodieElement.isPrimitive())
            return convertPrimitive(goodieElement.asPrimitive());

        return null; // <-- No corresponding Toml type exists
    }

    public static Map<String, Object> convertObject(GoodieObject goodieObject) {
        Map<String, Object> tomlObject = new HashMap<>();
        for (String propertyName : goodieObject.keySet()) {
            Object tomlElement = convert(goodieObject.get(propertyName));
            tomlObject.put(propertyName, tomlElement);
        }
        return tomlObject;
    }

    public static List<Object> convertArray(GoodieArray goodieArray) {
        List<Object> tomlArray = new ArrayList<>();
        for (GoodieElement goodieElement : goodieArray) {
            Object tomlElement = convert(goodieElement);
            tomlArray.add(tomlElement);
        }
        return tomlArray;
    }

    public static Object convertPrimitive(GoodiePrimitive goodiePrimitive) {
        if (goodiePrimitive.isNumber())
            return goodiePrimitive.getNumber();
        if (goodiePrimitive.isString())
            return goodiePrimitive.getString();
        if (goodiePrimitive.isCharacter())
            return goodiePrimitive.getCharacter();
        if (goodiePrimitive.isBoolean())
            return goodiePrimitive.getBoolean();
        return goodiePrimitive.getString();
    }

    /* ------------------------------------------ */

    @Override
    public String writeToString(Toml externalFormat, boolean pretty) {
        TomlWriter.Builder builder = new TomlWriter.Builder();
        if (pretty) {
            builder = builder
                    .indentTablesBy(2)
                    .padArrayDelimitersBy(1);
        }
        TomlWriter writer = builder.build();
        return writer.write(externalFormat.toMap());
    }

    @Override
    public Toml readFromString(String text) throws GoodieParseException {
        try {
            return new Toml().read(text);

        } catch (Exception e) {
            throw new GoodieParseException("Error while trying to parse TOML text", e);
        }
    }

}
