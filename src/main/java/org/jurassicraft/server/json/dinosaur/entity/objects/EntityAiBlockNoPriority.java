package org.jurassicraft.server.json.dinosaur.entity.objects;

import com.google.gson.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.json.dinosaur.entity.EntityDinosaurJsonHandler;

import java.lang.reflect.Type;
import java.util.function.Function;


public class EntityAiBlockNoPriority {
    private final Function<DinosaurEntity, EntityAIBase> entityAiFunc;

    public EntityAiBlockNoPriority(Function entityAiFunc) {
        super();
        this.entityAiFunc = entityAiFunc;
    }

    public static class Deserializer implements JsonDeserializer<EntityAiBlockNoPriority> {
        @Override
        public EntityAiBlockNoPriority deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
            }
            JsonObject json = element.getAsJsonObject();
            return new EntityAiBlockNoPriority(EntityDinosaurJsonHandler.TASK_MAP.get(JsonUtils.getString(json, "type")).apply(json));
        }
    }

}
