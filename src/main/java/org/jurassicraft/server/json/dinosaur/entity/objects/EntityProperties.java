package org.jurassicraft.server.json.dinosaur.entity.objects;

import com.google.common.collect.Lists;
import com.google.gson.*;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.json.JsonUtil;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.List;

public class EntityProperties {

    String type;
    @Nullable AttackTargets targets;
    @Nullable EntityJsonAi ai;
    List<EntityJsonAttributes> attributes;
    @Nullable EntityJsonSounds sounds;


    //TODO Look into Constructor
    public EntityProperties(String string, JsonObject object, JsonObject object2, List list, JsonObject object3) {
        super();
    }

    public static class Deserializer implements JsonDeserializer<EntityProperties> {

        @Override
        public EntityProperties deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = JsonUtils.getJsonObject(element, "root"); //Impossible ?
            return new EntityProperties(
                    JsonUtils.getString(json, "type"),
                    JsonUtils.hasField(json, "targets") ? context.deserialize(JsonUtils.getJsonObject(json, "targets"), AttackTargets.class) : null,
                    JsonUtils.hasField(json, "ai") ?  context.deserialize(JsonUtils.getJsonObject(json, "ai"), EntityJsonAi.class) : null,
                    JsonUtils.hasField(json, "attributes") ? JsonUtil.deserializeArray(JsonUtils.getJsonArray(json, "attributes"), context, EntityJsonAttributes.class) : Lists.newArrayList(),
                    JsonUtils.hasField(json, "sounds") ? context.deserialize(JsonUtils.getJsonObject(json, "sounds"), EntityJsonSounds.class) : null
            );
        }
    }
}
