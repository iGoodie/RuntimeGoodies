package net.programmer.igoodie.goodies.format;

import com.google.gson.*;
import net.programmer.igoodie.goodies.exception.GoodieParseException;
import net.programmer.igoodie.goodies.runtime.*;

public class GsonGoodieFormat extends GoodieFormat<JsonObject, GoodieObject> {

    @Override
    public GoodieObject writeToGoodie(JsonObject externalFormat) {
        return convertObject(externalFormat);
    }

    public static GoodieElement convert(JsonElement jsonElement) {
        if (jsonElement.isJsonObject())
            return convertObject(jsonElement.getAsJsonObject());
        if (jsonElement.isJsonArray())
            return convertArray(jsonElement.getAsJsonArray());
        if (jsonElement.isJsonPrimitive())
            return convertPrimitive(jsonElement.getAsJsonPrimitive());
        if (jsonElement.isJsonNull()) {
            return GoodieNull.INSTANCE;
        }

        return null; // <-- No corresponding Goodie type exists
    }

    public static GoodieObject convertObject(JsonObject jsonObject) {
        GoodieObject goodieObject = new GoodieObject();
        for (String propertyName : jsonObject.keySet()) {
            GoodieElement goodieElement = convert(jsonObject.get(propertyName));
            goodieObject.put(propertyName, goodieElement);
        }
        return goodieObject;
    }

    public static GoodieArray convertArray(JsonArray jsonArray) {
        GoodieArray goodieArray = new GoodieArray();
        for (JsonElement jsonElement : jsonArray) {
            GoodieElement goodieElement = convert(jsonElement);
            goodieArray.add(goodieElement);
        }
        return goodieArray;
    }

    public static GoodiePrimitive convertPrimitive(JsonPrimitive jsonPrimitive) {
        return GoodiePrimitive.from(
                jsonPrimitive.isBoolean() ? jsonPrimitive.getAsBoolean()
                        : jsonPrimitive.isString() ? jsonPrimitive.getAsString()
                        : jsonPrimitive.isNumber() ? jsonPrimitive.getAsNumber()
                        : jsonPrimitive.getAsString()
        );
    }

    /* ------------------------------------------ */

    @Override
    public JsonObject readFromGoodie(GoodieObject goodie) {
        return convertObject(goodie);
    }

    public static JsonElement convert(GoodieElement goodieElement) {
        if (goodieElement.isObject())
            return convertObject((GoodieObject) goodieElement);
        if (goodieElement.isArray())
            return convertArray((GoodieArray) goodieElement);
        if (goodieElement.isPrimitive())
            return convertPrimitive((GoodiePrimitive) goodieElement);
        if (goodieElement.isNull())
            return JsonNull.INSTANCE;

        return null; // <-- No corresponding Gson type exists
    }

    public static JsonObject convertObject(GoodieObject goodieObject) {
        JsonObject jsonObject = new JsonObject();
        for (String propertyName : goodieObject.keySet()) {
            JsonElement jsonElement = convert(goodieObject.get(propertyName));
            jsonObject.add(propertyName, jsonElement);
        }
        return jsonObject;
    }

    public static JsonArray convertArray(GoodieArray goodieArray) {
        JsonArray jsonArray = new JsonArray();
        for (GoodieElement goodieElement : goodieArray) {
            JsonElement jsonElement = convert(goodieElement);
            jsonArray.add(jsonElement);
        }
        return jsonArray;
    }

    public static JsonPrimitive convertPrimitive(GoodiePrimitive goodiePrimitive) {
        if (goodiePrimitive.isNumber())
            return new JsonPrimitive(goodiePrimitive.getNumber());
        if (goodiePrimitive.isString())
            return new JsonPrimitive(goodiePrimitive.getString());
        if (goodiePrimitive.isCharacter())
            return new JsonPrimitive(goodiePrimitive.getCharacter());
        if (goodiePrimitive.isBoolean())
            return new JsonPrimitive(goodiePrimitive.getBoolean());
        return new JsonPrimitive(goodiePrimitive.getString());
    }

    /* ------------------------------------------ */

    @Override
    public String writeToString(JsonObject externalFormat, boolean pretty) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (pretty) gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.serializeNulls().create();
        return gson.toJson(externalFormat);
    }

    @Override
    public JsonObject readFromString(String text) throws GoodieParseException {
        try {
            JsonElement jsonElement = JsonParser.parseString(text);

            if (jsonElement.isJsonNull())
                return new JsonObject();

            if (!jsonElement.isJsonObject())
                throw new GoodieParseException("Expected a JSON object, instead found -> " + jsonElement.getClass());

            return jsonElement.getAsJsonObject();

        } catch (JsonParseException e) {
            throw new GoodieParseException("Error while trying to parse JSON text", e);
        }
    }

}