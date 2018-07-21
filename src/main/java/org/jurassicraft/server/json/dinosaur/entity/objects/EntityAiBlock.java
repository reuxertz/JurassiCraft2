package org.jurassicraft.server.json.dinosaur.entity.objects;

import com.google.gson.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.entity.dinosaur.DinosaurEntity;
import org.jurassicraft.server.json.dinosaur.entity.EntityDinosaurJsonHandler;

import java.lang.reflect.Type;
import java.util.function.Function;

public class EntityAiBlock {
    private final int priority;
    private final Function<DinosaurEntity, EntityAIBase> entityAiFunc;

    public EntityAiBlock(int priority, Function aiFunc) {
        super();
        this.priority = priority;
        this.entityAiFunc = aiFunc;
    }

    public static class Deserializer implements JsonDeserializer<EntityAiBlock> {
        @Override
        public EntityAiBlock deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
            }
            JsonObject json = element.getAsJsonObject();
            return new EntityAiBlock(JsonUtils.getInt(json, "priority"), EntityDinosaurJsonHandler.TASK_MAP.get(JsonUtils.getString(json, "type")).apply(json));
        }
    }

}
