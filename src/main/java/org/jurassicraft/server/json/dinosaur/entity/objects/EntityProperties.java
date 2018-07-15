package org.jurassicraft.server.json.dinosaur.entity.objects;

import com.google.common.collect.Lists;
import com.google.gson.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.SoundEvent;
import org.jurassicraft.client.model.animation.EntityAnimation;
import org.jurassicraft.server.json.JsonUtil;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Value
public class EntityProperties { 
    String type;
    @Nullable AttackTargets targets;
    @Nullable EntityJsonAi ai;
    List<EntityJsonAttributes> attributes;
    @Nullable EntityJsonSounds sounds;

    public static class Deserializer implements JsonDeserializer<EntityProperties> {

        @Override
        public EntityProperties deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = JsonUtils.getJsonObject(element, "root"); //Impossible ?
            return new EntityProperties(
                    JsonUtils.getString(json, "type"),
                    JsonUtils.hasField(json, "targets") ? context.deserialize(JsonUtils.getJsonObject(json, "targets"), AttackTargets.class) : null,
                    JsonUtils.hasField(json, "ai") ? context.deserialize(JsonUtils.getJsonObject(json, "ai"), EntityJsonAi.class) : null,
                    JsonUtils.hasField(json, "attributes") ? JsonUtil.deserializeArray(JsonUtils.getJsonArray(json, "attributes"), context, EntityJsonAttributes.class) : Lists.newArrayList(),
                    JsonUtils.hasField(json, "sounds") ? context.deserialize(JsonUtils.getJsonObject(json, "sounds"), EntityJsonSounds.class) : null
            );
        }
    }
}
