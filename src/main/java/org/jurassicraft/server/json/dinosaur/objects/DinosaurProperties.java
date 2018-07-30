package org.jurassicraft.server.json.dinosaur.objects;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.Sound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import org.jurassicraft.client.model.animation.EntityAnimation;
import org.jurassicraft.server.json.JsonUtil;
import org.jurassicraft.server.json.dinosaur.DinosaurJsonHandler;
import org.jurassicraft.server.json.dinosaur.entity.objects.EntityJsonSounds;
import org.jurassicraft.server.period.TimePeriod;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;

public class DinosaurProperties {

	public String name;
	public TimePeriod timePeriod;
	public String headCubeName;

	public String dinosaurAnimatorClassName;
	public String dinosaurModelLocation;
	public float shadowSize;
	public boolean possibleToLeashUntamed;

	public SpawnEggInfo maleSpawnEgg;
	public SpawnEggInfo femaleSpawnEgg;

	public DinosaurStatistics statistics;
	public DinosaurTraits traits;
	public DinosaurSpawningInfo spawningInfo;
	public DinosaurBreeding breeding;
	public EntityJsonSounds sounds;

	public DinosaurAnimation animation;

	public String[] bones;
	public String[][] skeletonRecipe;

	public DinosaurProperties(String name, TimePeriod timePeriod, String headCubeName, String dinosaurAnimatorClassName,
							  String dinosaurModelLocation, float shadowSize, boolean possibleToLeashUntamed, SpawnEggInfo maleSpawnEgg,
							  SpawnEggInfo femaleSpawnEgg, DinosaurStatistics statistics, DinosaurTraits traits,
							  DinosaurSpawningInfo spawningInfo, DinosaurBreeding breeding, EntityJsonSounds sounds, String[] bones, String[][] skeletonRecipe,
							  DinosaurAnimation animation) {
		this.name = name;
		this.timePeriod = timePeriod;
		this.headCubeName = headCubeName;
		this.dinosaurAnimatorClassName = dinosaurAnimatorClassName;
		this.dinosaurModelLocation = dinosaurModelLocation;
		this.shadowSize = shadowSize;
		this.possibleToLeashUntamed = possibleToLeashUntamed;
		this.maleSpawnEgg = maleSpawnEgg;
		this.femaleSpawnEgg = femaleSpawnEgg;
		this.statistics = statistics;
		this.traits = traits;
		this.spawningInfo = spawningInfo;
		this.breeding = breeding;
		this.sounds = sounds;
		this.bones = bones;
		this.skeletonRecipe = skeletonRecipe;
		this.animation = animation;
	}


	//TODO: model

	public static class JsonHandler implements JsonDeserializer<DinosaurProperties>, JsonSerializer<DinosaurProperties> {

        @Override
		@SuppressWarnings("unchecked")
		public DinosaurProperties deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			if (!element.isJsonObject()) {
				throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
			}
			JsonObject json = element.getAsJsonObject();
			JsonObject spawnEggInfo = JsonUtils.getJsonObject(json, "spawn_egg");

			JsonArray array = JsonUtils.getJsonArray(json, "skeleton_recipe");
			if (array.size() > 5) {
				throw new JsonParseException("Skeleton Recipe has a height larger than 5. At max it should be 5");
			}
			int index = 0;
			String[][] skeleton_recipe = null;
			int size = 0;
			for (JsonElement jsonElement : array) {
				JsonArray childArray = jsonElement.getAsJsonArray();
				if (childArray.size() > 5) {
					throw new JsonParseException("Skeleton Recipe has a height larger than 5. At max it should be 5");
				}
				String[] childList = createStringList(childArray);
				if (skeleton_recipe == null) {
					size = childList.length;
					skeleton_recipe = new String[array.size()][size];
				} else if (size != childList.length) {
					throw new JsonParseException("All widths of the recipe must be the same width");
				}
				skeleton_recipe[index++] = childList;
			}


                return new DinosaurProperties(
                        JsonUtils.getString(json, "name"),
                        TimePeriod.valueOf(JsonUtils.getString(json, "time_period").toUpperCase(Locale.ENGLISH)),
                        JsonUtils.getString(json, "head_cube_name"),
                        JsonUtils.isString(json, "dinosaur_animator_class") ? JsonUtils.getString(json, "dinosaur_animator_class") : null,
                        JsonUtils.isString(json,"model_location") ? JsonUtils.getString(json, "model_location") : null,
                        JsonUtils.getFloat(json, "shadow_size"),
                        JsonUtils.getBoolean(json, "possible_to_leash_untamed"),
                        context.deserialize(JsonUtils.getJsonArray(spawnEggInfo, "male"), SpawnEggInfo.class),
                        context.deserialize(JsonUtils.getJsonArray(spawnEggInfo, "female"), SpawnEggInfo.class),
                        context.deserialize(JsonUtils.getJsonObject(json, "statistics"), DinosaurStatistics.class),
                        context.deserialize(JsonUtils.getJsonObject(json, "traits"), DinosaurTraits.class),
                        context.deserialize(JsonUtils.getJsonObject(json, "spawning"), DinosaurSpawningInfo.class), 
    			        context.deserialize(JsonUtils.getJsonObject(json, "breeding"), DinosaurBreeding.class), context.deserialize(JsonUtils.getJsonObject(json, "sounds"), EntityJsonSounds.class), createStringList(JsonUtils.getJsonArray(json, "bones")), 
    			        skeleton_recipe,
                        DinosaurAnimation.parse(JsonUtils.getJsonObject(json, "animation"))
                );
          
        }

		@Override
		public JsonElement serialize(DinosaurProperties src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			JsonObject spawnEgg = new JsonObject();
			spawnEgg.add("male", context.serialize(src.maleSpawnEgg));
			spawnEgg.add("female", context.serialize(src.femaleSpawnEgg));
			json.addProperty("name", src.name);
			json.addProperty("time_period", src.timePeriod.toString().toLowerCase(Locale.ENGLISH));
			json.addProperty("head_cube_name", src.headCubeName);
			if (src.dinosaurModelLocation != null && !src.dinosaurModelLocation.isEmpty()) {
				json.addProperty("model_location", src.dinosaurModelLocation);
			} else {
				json.addProperty("dinosaur_animator_class", src.dinosaurAnimatorClassName);
			}
			json.addProperty("shadow_size", src.shadowSize);
			json.add("spawn_egg", spawnEgg);
			json.add("statistics", context.serialize(src.statistics));
			json.add("traits", context.serialize(src.traits));
			json.add("spawning", context.serialize(src.spawningInfo));
			json.add("breeding", context.serialize(src.breeding));


			JsonArray bones = new JsonArray();
			for (String bone : src.bones) {
				bones.add(bone);
			}
			json.add("bones", bones);


			JsonArray skeletonRecipe = new JsonArray();
			for (String[] strings : src.skeletonRecipe) {
				JsonArray innerArray = new JsonArray();
				for (String string : strings) {
					innerArray.add(string);
				}
				skeletonRecipe.add(innerArray);
			}
			json.add("skeleton_recipe", skeletonRecipe);
			return json;
		}

		private String[] createStringList(JsonArray array) {
			String[] list = new String[array.size()];
			int i = 0;
			for (JsonElement jsonElement : array) {
				if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString()) {
					list[i++] = jsonElement.getAsString();
				} else {
					throw new JsonParseException("Expected String, found " + JsonUtils.toString(jsonElement));
				}
			}
			return list;
		}
	}

}
