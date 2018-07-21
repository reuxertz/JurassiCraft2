package org.jurassicraft.server.json.dinosaur.objects;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.entity.Dinosaur;

import java.util.LinkedHashMap;
import java.util.Map;

public class DinosaurLegs {
	public Dinosaur.LegType type;
	public Map<String, Float> params;

	public DinosaurLegs() {
		this.params = new LinkedHashMap<>();
	}

	public static DinosaurLegs parse(JsonObject json) {
		DinosaurLegs legs = new DinosaurLegs();
		legs.type = Dinosaur.LegType.valueOf(JsonUtils.getString(json, "type"));
		for (Map.Entry<String, JsonElement> element : JsonUtils.getJsonObject(json, "params").entrySet()) {
			legs.params.put(element.getKey(), element.getValue().getAsFloat());
		}
		return legs;
	}
}
