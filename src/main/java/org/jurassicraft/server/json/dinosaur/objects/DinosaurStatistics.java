package org.jurassicraft.server.json.dinosaur.objects;

import com.google.gson.*;
import lombok.Value;
import net.minecraft.util.JsonUtils;

import java.lang.reflect.Type;

public class DinosaurStatistics {

    public AdultBabyValue speed;
    public AdultBabyValue health;
    public AdultBabyValue strength;
    public AdultBabyValue sizeX;
    public AdultBabyValue sizeY;
    public AdultBabyValue eyeHeight;
    public AdultBabyValue scale;
    public int jumpHeight;
    public double attackSpeed;
    public int itemStorage;

    public DinosaurStatistics(AdultBabyValue speed, AdultBabyValue health, AdultBabyValue strength,
                              AdultBabyValue sizeX, AdultBabyValue sizeY, AdultBabyValue eyeHeight,
                              AdultBabyValue scale, int jumpHeight, double attackSpeed, int itemStorage) {
        this.speed = speed;
        this.health = health;
        this.strength = strength;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.eyeHeight = eyeHeight;
        this.scale = scale;
        this.jumpHeight = jumpHeight;
        this.attackSpeed = attackSpeed;
        this.itemStorage = itemStorage;

    }

    public static class JsonHandler implements JsonDeserializer<DinosaurStatistics>, JsonSerializer<DinosaurStatistics> {

        @Override
        public DinosaurStatistics deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
            }
            JsonObject json = element.getAsJsonObject();
            return new DinosaurStatistics(
                    context.deserialize(JsonUtils.getJsonObject(json, "speed"), AdultBabyValue.class),
                    context.deserialize(JsonUtils.getJsonObject(json, "health"), AdultBabyValue.class),
                    context.deserialize(JsonUtils.getJsonObject(json, "strength"), AdultBabyValue.class),
                    context.deserialize(JsonUtils.getJsonObject(json, "size_x"), AdultBabyValue.class),
                    context.deserialize(JsonUtils.getJsonObject(json, "size_y"), AdultBabyValue.class),
                    context.deserialize(JsonUtils.getJsonObject(json, "eye_height"), AdultBabyValue.class),
                    context.deserialize(JsonUtils.getJsonObject(json, "scale"), AdultBabyValue.class),
                    JsonUtils.getInt(json, "jump_height"),
                    JsonUtils.getFloat(json, "attack_speed"),
                    JsonUtils.getInt(json, "item_storage")
            );
        }

        @Override
        public JsonElement serialize(DinosaurStatistics src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            json.add("speed", context.serialize(src.speed));
            json.add("health", context.serialize(src.health));
            json.add("strength", context.serialize(src.strength));
            json.add("size_x", context.serialize(src.sizeX));
            json.add("size_y", context.serialize(src.sizeY));
            json.add("eye_height", context.serialize(src.eyeHeight));
            json.add("scale", context.serialize(src.scale));
            json.addProperty("jump_height", src.jumpHeight);
            json.addProperty("attack_speed", src.attackSpeed);
            json.addProperty("item_storage", src.itemStorage);
            return json;
        }
    }
}
