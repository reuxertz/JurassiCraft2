package org.jurassicraft.server.json.dinosaur.entity.objects;

import com.google.gson.*;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.json.JsonUtil;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.List;


public class EntityJsonAi {
    @Nullable EntityAiBlockNoPriority attack;
    List<EntityAiBlock> additionalTasks;

    public EntityJsonAi(EntityAiBlockNoPriority attack, List list) {
        super();
        this.attack = attack;
        this.additionalTasks = list;

    }

    public static class Deserializer implements JsonDeserializer<EntityJsonAi> {

        @Override
        public EntityJsonAi deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = JsonUtils.getJsonObject(element, "ai");
            return new EntityJsonAi(
                    JsonUtils.hasField(json, "attack") ? context.deserialize(JsonUtils.getJsonObject(json, "attack"), EntityAiBlockNoPriority.class) : null,
                    JsonUtils.hasField(json, "additional_tasks") ? JsonUtil.deserializeArray(JsonUtils.getJsonArray(json, "additional_tasks"), context, EntityAiBlock.class) : null
            );
        }
    }

}
