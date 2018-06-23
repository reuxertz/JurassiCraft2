package org.jurassicraft.server.json.dinosaur.objects;

import com.google.common.collect.Lists;
import com.google.gson.*;
import lombok.Data;
import net.minecraft.util.JsonUtils;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.lang.reflect.Type;
import java.util.List;

@Data
public class DinosaurSpawningInfo {

    private final int chance;
    private final Biome[] biomes;

    public static class Deserializer implements JsonDeserializer<DinosaurSpawningInfo> {
        @Override
        public DinosaurSpawningInfo deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
            }
            JsonObject json = element.getAsJsonObject();
            List<Biome> biomes = Lists.newArrayList();
            for(JsonElement jsonElement : JsonUtils.getJsonArray(json, "biomes")) {
                for (BiomeDictionary.Type type : BiomeDictionary.Type.getAll()) {
                    if(type.getName().equalsIgnoreCase(JsonUtils.getString(json, "biomes"))) {
                        biomes.addAll(BiomeDictionary.getBiomes(type));
                        break;
                    }
                }
            }

            return new DinosaurSpawningInfo(JsonUtils.getInt(json, "chance"), biomes.toArray(new Biome[0]));
        }
    }
}
