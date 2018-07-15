package org.jurassicraft.server.json.dinosaur.objects;

import com.google.gson.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import net.minecraft.util.JsonUtils;

import java.lang.reflect.Type;

@Value
public class SpawnEggInfo {
    int primary;
    int secondary;

    public static class JsonHandler implements JsonDeserializer<SpawnEggInfo>, JsonSerializer<SpawnEggInfo>{
        @Override
        public SpawnEggInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!json.isJsonArray()) {
                throw new JsonSyntaxException("Expected an array, found " + JsonUtils.toString(json));
            }
            JsonArray array = json.getAsJsonArray();
            if(array.size() != 2) {
                throw new JsonSyntaxException("Expected array size to be 2, found " + array.size());
            }
            return new SpawnEggInfo(getInt(array.get(0)), getInt(array.get(1)));
        }

        @Override
        public JsonElement serialize(SpawnEggInfo src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray json = new JsonArray();
            json.add("0x" + getHex(src.primary));
            json.add("0x" + getHex(src.secondary));
            return json;
        }

        private String getHex(int i) {
            StringBuilder builder = new StringBuilder(Integer.toHexString(i));
            while (builder.length() < 6) {
                builder.insert(0, "0");
            }
            return builder.toString();
        }

        private int getInt(JsonElement json) throws JsonParseException {
            if(json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
                String num = json.getAsString();
                if(num.startsWith("0x")) { //If the user wants the integer to start with 0x, then let them. The mock up json did so
                    num = num.substring(2, num.length());
                }
                if(num.length() != 6) {
                    throw  new JsonParseException("Expected a string length of 6, found " + num.length());
                }
                return Integer.parseInt(num, 16);
            }
            throw new JsonSyntaxException("Expected a string, found " + JsonUtils.toString(json));
        }
    }
}
