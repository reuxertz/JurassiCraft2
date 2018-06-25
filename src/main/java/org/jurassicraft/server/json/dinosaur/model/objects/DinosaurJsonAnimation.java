package org.jurassicraft.server.json.dinosaur.model.objects;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;
import lombok.Data;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.client.model.AnimatableModel;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.json.dinosaur.model.JsonDinosaurAnimator;
import org.jurassicraft.server.json.dinosaur.model.objects.animation.Bob;
import org.jurassicraft.server.json.dinosaur.model.objects.animation.BufferSwing;
import org.jurassicraft.server.json.dinosaur.model.objects.animation.Look;
import org.jurassicraft.server.json.dinosaur.model.objects.animation.chainwave.ChainWaveIdleTick;
import org.jurassicraft.server.json.dinosaur.model.objects.animation.chainwave.ChainWaveLimbSwing;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Data
public class DinosaurJsonAnimation {

    public static Map<String, Map<String, AnimationFactory>> factoryMap = Maps.newHashMap();

    private static Map<String, AnimationFactory> createWithDefault(String string, AnimationFactory factory) {
        Map<String, AnimationFactory> map = Maps.newHashMap();
        map.put(string, factory);
        return map;
    }

    static {

        Map<String, AnimationFactory> limbSwing = factoryMap.computeIfAbsent("limb_swing", s -> Maps.newHashMap());
        limbSwing.put("bob", Bob::new);
        limbSwing.put("chain_wave", ChainWaveLimbSwing::new);
        limbSwing.put("chain_swing", ChainWaveLimbSwing::new);

        factoryMap.put("idle_tick", createWithDefault("chain_wave", ChainWaveIdleTick::new));
        factoryMap.put("look", createWithDefault("look", Look::new));
        factoryMap.put("tail_buffer", createWithDefault("buffer_swing", BufferSwing::new));

    }

    private final Map<JsonArray, AnimationFactory> animationFactoryMap;
    private final List<AnimationCallable> callables = Lists.newArrayList();

    public void runFactories(JsonDinosaurAnimator animator) {
        callables.clear();
        animationFactoryMap.forEach((array, factory) -> callables.add(factory.createCallback(array, animator)));
    }

    public void performAnimations(AnimatableModel model, DinosaurEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        for (AnimationCallable callable : this.callables) {
            callable.performAnimations(model, entity, limbSwing, limbSwingAmount, ticks, rotationYaw, rotationPitch, scale);
        }
    }

    public static class Deserializer implements JsonDeserializer<DinosaurJsonAnimation> {
        @Override
        public DinosaurJsonAnimation deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
            }
            JsonObject json = element.getAsJsonObject();
            String type = JsonUtils.getString(json, "type");
            if(!factoryMap.containsKey(type)) {
                throw new JsonParseException("Illegal type: " + type);
            }
            Map<JsonArray, AnimationFactory> animationFactoryMap = Maps.newHashMap();
            factoryMap.get(type).forEach((s, factory) -> animationFactoryMap.put(JsonUtils.getJsonArray(json, s), factory));
            return new DinosaurJsonAnimation(animationFactoryMap);
        }
    }

}
