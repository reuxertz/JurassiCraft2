package org.jurassicraft.server.json.dinosaur.entity.objects;

import com.google.gson.*;
import lombok.Data;
import net.minecraft.util.JsonUtils;

import java.lang.reflect.Type;

@Data
public class EntityAiBlock {
    private final int priority;
    private final String name;

    public static class Deserializer implements JsonDeserializer<EntityAiBlock> {

        @Override
        public EntityAiBlock deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
            }
            JsonObject json = element.getAsJsonObject();
            return new EntityAiBlock(JsonUtils.getInt(json, "priority"), JsonUtils.getString(json, "type"));
        }
    }
}
