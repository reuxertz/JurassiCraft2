package org.jurassicraft.server.json.dinosaur.objects;

import com.google.gson.*;
import lombok.Data;
import net.minecraft.util.JsonUtils;

import java.lang.reflect.Type;

@Data
public class DinosaurStatistics {

    private final AdultBabyValue speed;
    private final AdultBabyValue health;
    private final AdultBabyValue strength;
    private final AdultBabyValue sizeX;
    private final AdultBabyValue sizeY;
    private final AdultBabyValue eyeHeight;
    private final AdultBabyValue scale;
    private final int jumpHeight;
    private final float attackSpeed;
    private final int itemStorage;

    public static class Deserializer implements JsonDeserializer<DinosaurStatistics> {

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
    }

}
