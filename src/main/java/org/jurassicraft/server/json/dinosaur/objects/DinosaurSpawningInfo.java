package org.jurassicraft.server.json.dinosaur.objects;

import com.google.common.collect.Lists;
import com.google.gson.*;
import lombok.Data;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.BiomeDictionary;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

@Data
public class DinosaurSpawningInfo {

    private final int chance;
    private final BiomeDictionary.Type[] biomes;

    public static class JsonHandler implements JsonDeserializer<DinosaurSpawningInfo>, JsonSerializer<DinosaurSpawningInfo> {

        @Override
        public DinosaurSpawningInfo deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
            }
            JsonObject json = element.getAsJsonObject();
            List<BiomeDictionary.Type> biomes = Lists.newArrayList();
            for(JsonElement jsonElement : JsonUtils.getJsonArray(json, "biomes")) {
                for (BiomeDictionary.Type type : BiomeDictionary.Type.getAll()) {
                    if(type.getName().equalsIgnoreCase(JsonUtils.getString(json, "biomes"))) {
                        biomes.add(type);
                        break;
                    }
                }
            }

            return new DinosaurSpawningInfo(JsonUtils.getInt(json, "chance"), biomes.toArray(new BiomeDictionary.Type[0]));
        }

        @Override
        public JsonElement serialize(DinosaurSpawningInfo src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            json.addProperty("chance", src.getChance());
            JsonArray array = new JsonArray();
            for (BiomeDictionary.Type biome : src.biomes) {
                array.add(biome.getName().toLowerCase(Locale.ENGLISH));
            }
            json.add("biomes", array);
            return json;
        }
    }
}
