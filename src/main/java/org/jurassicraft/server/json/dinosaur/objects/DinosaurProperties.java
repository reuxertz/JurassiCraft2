package org.jurassicraft.server.json.dinosaur.objects;

import com.google.gson.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.period.TimePeriod;

import java.lang.reflect.Type;
import java.util.Locale;

@Value
public class DinosaurProperties {

    String name;
    TimePeriod timePeriod;
    String headCubeName;
    @Deprecated
    String dinosaurAnimatorClassName;
    String dinosaurModelLocation;
    float shadowSize;

    SpawnEggInfo maleSpawnEgg;
    SpawnEggInfo femaleSpawnEgg;

    DinosaurStatistics statistics;
    DinosaurTraits traits;
    DinosaurSpawningInfo spawningInfo;
    DinosaurBreeding breeding;

    String[] bones;
    String[][] skeletonRecipe;

    //TODO: model

    public static class JsonHandler implements JsonDeserializer<DinosaurProperties>, JsonSerializer<DinosaurProperties> {

        @Override
        @SuppressWarnings("unchecked")
        public DinosaurProperties deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
            }
            JsonObject json = element.getAsJsonObject();
            String entityClass = JsonUtils.getString(json, "entity");
            Class clazz;
            try {
                clazz = Class.forName(JsonUtils.getString(json, "entity"));
            } catch (ClassNotFoundException e) {
                throw new JsonParseException("Unable to find entity class, " + entityClass);
            }
            if(!DinosaurEntity.class.isAssignableFrom(clazz)) {
                throw new JsonParseException("Entity class, " + entityClass + " is not a child class of Entity");
            }

            JsonObject spawnEggInfo = JsonUtils.getJsonObject(json, "spawn_egg");

            JsonArray array = JsonUtils.getJsonArray(json, "skeleton_recipe");
            if(array.size() > 5) {
                throw new JsonParseException("Skeleton Recipe has a height larger than 5. At max it should be 5");
            }
            int index = 0;
            String[][] skeleton_recipe = null;
            int size = 0;
            for(JsonElement jsonElement : array) {
                JsonArray childArray = jsonElement.getAsJsonArray();
                if(childArray.size() > 5) {
                    throw new JsonParseException("Skeleton Recipe has a height larger than 5. At max it should be 5");
                }
                String[] childList = createStringList(childArray);
                if(skeleton_recipe == null) {
                    size = childList.length;
                    skeleton_recipe = new String[array.size()][size];
                } else if(size != childList.length) {
                    throw new JsonParseException("All widths of the recipe must be the same width");
                }
                skeleton_recipe[index++] = childList;
            }

            return new DinosaurProperties(
                    JsonUtils.getString(json, "name"),
                    TimePeriod.valueOf(JsonUtils.getString(json, "time_period").toUpperCase(Locale.ENGLISH)),
                    JsonUtils.getString(json, "head_cube_name"),
                    JsonUtils.isString(json, "dinosaur_animator_class") ? JsonUtils.getString(json, "dinosaur_animator_class") : null,
                    JsonUtils.isString(json, "model_location") ? JsonUtils.getString(json, "model_location") : null,
                    JsonUtils.getFloat(json, "shadow_size"),
                    context.deserialize(JsonUtils.getJsonArray(spawnEggInfo, "male"), SpawnEggInfo.class),
                    context.deserialize(JsonUtils.getJsonArray(spawnEggInfo, "female"), SpawnEggInfo.class),
                    context.deserialize(JsonUtils.getJsonObject(json, "statistics"), DinosaurStatistics.class),
                    context.deserialize(JsonUtils.getJsonObject(json, "traits"), DinosaurTraits.class),
                    context.deserialize(JsonUtils.getJsonObject(json, "spawning"), DinosaurSpawningInfo.class),
                    context.deserialize(JsonUtils.getJsonObject(json, "breeding"), DinosaurBreeding.class),
                    createStringList(JsonUtils.getJsonArray(json, "bones")),
                    skeleton_recipe
            );
        }

        @Override
        public JsonElement serialize(DinosaurProperties src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            JsonObject spawnEgg = new JsonObject();
            spawnEgg.add("male", context.serialize(src.getMaleSpawnEgg()));
            spawnEgg.add("female", context.serialize(src.getFemaleSpawnEgg()));
            json.addProperty("name", src.getName());
            json.addProperty("time_period", src.getTimePeriod().toString().toLowerCase(Locale.ENGLISH));
            json.addProperty("head_cube_name", src.getHeadCubeName());
            if(src.getDinosaurModelLocation() != null && !src.getDinosaurModelLocation().isEmpty()) {
                json.addProperty("model_location", src.getDinosaurModelLocation());
            } else {
                json.addProperty("dinosaur_animator_class", src.getDinosaurAnimatorClassName());
            }
            json.addProperty("shadow_size", src.getShadowSize());
            json.add("spawn_egg", spawnEgg);
            json.add("statistics", context.serialize(src.getStatistics()));
            json.add("traits", context.serialize(src.getTraits()));
            json.add("spawning", context.serialize(src.getSpawningInfo()));
            json.add("breeding", context.serialize(src.getBreeding()));

            JsonArray bones = new JsonArray();
            for (String bone : src.getBones()) {
                bones.add(bone);
            }
            json.add("bones", bones);


            JsonArray skeletonRecipe = new JsonArray();
            for (String[] strings : src.getSkeletonRecipe()) {
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
            for(JsonElement jsonElement : array) {
                if(jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString()) {
                    list[i++] = jsonElement.getAsString();
                } else {
                    throw new JsonParseException("Expected String, found " + JsonUtils.toString(jsonElement));
                }
            }
            return list;
        }
    }

}
