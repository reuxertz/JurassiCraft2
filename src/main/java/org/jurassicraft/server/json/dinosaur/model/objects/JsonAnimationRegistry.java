package org.jurassicraft.server.json.dinosaur.model.objects;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import org.jurassicraft.server.json.dinosaur.model.JsonAnimator;
import org.jurassicraft.server.json.dinosaur.model.objects.animation.*;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * The registry to register different animation modules. To register/remove/replace a module,
 * call this class at {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent}
 * @author Wyn Price
 */
public class JsonAnimationRegistry {

    public static final Map<String, Map<String, BiFunction<JsonArray, JsonAnimator, JsonAnimationModule>>> factoryMap = Maps.newHashMap();

    public static Map<String, BiFunction<JsonArray, JsonAnimator, JsonAnimationModule>> createWithDefault(String string, BiFunction<JsonArray, JsonAnimator, JsonAnimationModule> factory) {
        Map<String, BiFunction<JsonArray, JsonAnimator, JsonAnimationModule>> map = Maps.newHashMap();
        map.put(string, factory);
        return map;
    }

    static {

        Map<String, BiFunction<JsonArray, JsonAnimator, JsonAnimationModule>> limbSwing = factoryMap.computeIfAbsent("limb_swing", s -> Maps.newHashMap());
        limbSwing.put("bob", Bob::new);
        limbSwing.put("chain_wave", ChainWave.LimbSwing::new);
        limbSwing.put("chain_swing", ChainSwing::new);

        factoryMap.put("idle_tick", createWithDefault("chain_wave", ChainWave.IdleTick::new));
        factoryMap.put("look", createWithDefault("facing", Facing::new));
        factoryMap.put("tail_buffer", createWithDefault("buffer_swing", BufferSwing::new));
        factoryMap.put("leg_articulation", createWithDefault("quadruped", ArticulateQuadruped::new));

    }

}
