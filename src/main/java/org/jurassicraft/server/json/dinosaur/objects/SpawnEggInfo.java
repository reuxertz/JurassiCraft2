package org.jurassicraft.server.json.dinosaur.objects;

import com.google.gson.*;
import lombok.Data;
import net.minecraft.util.JsonUtils;

import java.lang.reflect.Type;

@Data
public class SpawnEggInfo {
    private final int primary;
    private final int secondary;

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
            json.add("0x" + Integer.toHexString(src.primary));
            json.add("0x" + Integer.toHexString(src.secondary));
            return json;
        }

        private int getInt(JsonElement json) throws JsonParseException {
            if(json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
                String num = json.getAsString();
                if(num.startsWith("0x")) { //If the user wants the integer to start with 0x, then let them. The mock up json did so
                    num = num.substring(2, num.length());
                }
                if(num.length() == 6) {
                    throw  new JsonParseException("Expected a string length of 6, found " + num.length());
                }
                return Integer.parseInt(num, 16);
            }
            throw new JsonSyntaxException("Expected a string, found " + JsonUtils.toString(json));
        }
    }
}
