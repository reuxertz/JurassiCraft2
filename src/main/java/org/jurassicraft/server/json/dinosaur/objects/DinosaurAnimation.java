package org.jurassicraft.server.json.dinosaur.objects;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;

public class DinosaurAnimation {
	public DinosaurLegs legs;

	public static DinosaurAnimation parse(JsonObject json) {
		DinosaurAnimation animation = new DinosaurAnimation();
		animation.legs = DinosaurLegs.parse(JsonUtils.getJsonObject(json, "legs"));
		return animation;
	}
}
