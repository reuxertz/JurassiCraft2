package org.jurassicraft.server.json.dinosaur.entity.objects;

import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.jurassicraft.client.model.animation.EntityAnimation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Map;

public class EntityJsonSounds {
    public Map<EntityAnimation, SoundEvent> soundMap;
    public @Nullable SoundEvent breathingSound;

    public EntityJsonSounds(Map soundMap, SoundEvent event) {
        super();
        this.soundMap = soundMap;
        this.breathingSound = event;
    }
    
    public Map<EntityAnimation, SoundEvent> getSoundMap() {

    	return soundMap;

    }

    public static class Deserializer implements JsonDeserializer<EntityJsonSounds> {

        @Override
        public EntityJsonSounds deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = JsonUtils.getJsonObject(element, "sounds");
            Map<EntityAnimation, SoundEvent> soundMap = Maps.newHashMap();
            for (JsonElement animationElement : JsonUtils.getJsonArray(json, "animation")) {
                JsonObject animation = JsonUtils.getJsonObject(animationElement, "animation");
                soundMap.put(EntityAnimation.valueOf(JsonUtils.getString(animation, "animation").toUpperCase()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(JsonUtils.getString(animation, "sound"))));
            }
            return new EntityJsonSounds(soundMap, JsonUtils.hasField(json, "breathing") ? ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(JsonUtils.getString(json, "breathing"))) : null);
        }
    }
}
