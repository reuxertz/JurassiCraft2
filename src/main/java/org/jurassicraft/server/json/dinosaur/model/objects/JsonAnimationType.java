package org.jurassicraft.server.json.dinosaur.model.objects;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.json.dinosaur.model.JsonAnimator;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * The type of modules holds a list of {@link JsonAnimationModule}.
 * In the example given in {@link JsonAnimationModule}, this would be "limb_swing" and "look"
 *
 * @author Wyn Price
 */
public class JsonAnimationType {

	private final Map<JsonArray, BiFunction<JsonArray, JsonAnimator, JsonAnimationModule>> animationFactoryMap;
	private final List<JsonAnimationModule> modules = Lists.newArrayList();

	public JsonAnimationType(Map<JsonArray, BiFunction<JsonArray, JsonAnimator, JsonAnimationModule>> animationFactoryMap) {
		super();
		this.animationFactoryMap = animationFactoryMap;
	}

	public void runFactories(JsonAnimator animator) {
		modules.clear();
		animationFactoryMap.forEach((array, factory) -> modules.add(factory.apply(array, animator)));
	}

	public void performAnimations(TabulaModel model, Entity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
		for (JsonAnimationModule module : this.modules) {
			module.performAnimations(model, entity, limbSwing, limbSwingAmount, ticks, rotationYaw, rotationPitch, scale);
		}
	}

	public static class Deserializer implements JsonDeserializer<JsonAnimationType> {
		@Override
		public JsonAnimationType deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			if (!element.isJsonObject()) {
				throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
			}
			JsonObject json = element.getAsJsonObject();
			String type = JsonUtils.getString(json, "type");
			if (!JsonAnimationRegistry.factoryMap.containsKey(type)) {
				throw new JsonParseException("Illegal type: " + type);
			}
			Map<JsonArray, BiFunction<JsonArray, JsonAnimator, JsonAnimationModule>> animationFactoryMap = Maps.newHashMap();
			for (Map.Entry<String, BiFunction<JsonArray, JsonAnimator, JsonAnimationModule>> entry : JsonAnimationRegistry.factoryMap.get(type).entrySet()) {
				if (JsonUtils.isJsonArray(json, entry.getKey())) {
					animationFactoryMap.put(JsonUtils.getJsonArray(json, entry.getKey()), entry.getValue());
				}
			}
			return new JsonAnimationType(animationFactoryMap);
		}
	}

}
