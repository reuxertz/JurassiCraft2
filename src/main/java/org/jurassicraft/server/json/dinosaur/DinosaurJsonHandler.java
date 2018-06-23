package org.jurassicraft.server.json.dinosaur;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.Diet;
import org.jurassicraft.server.json.dinosaur.objects.*;

public class DinosaurJsonHandler {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Dinosaur.class, (JsonDeserializer)(json, type, context) -> new JsonDinosaur(context.deserialize(json, DinosaurProperties.class)))
            .registerTypeAdapter(AdultBabyValue.class, new AdultBabyValue.Deserializer())
            .registerTypeAdapter(DinosaurBreeding.class, new DinosaurBreeding.Deserializer())
            .registerTypeAdapter(DinosaurProperties.class, new DinosaurProperties.Deserializer())
            .registerTypeAdapter(DinosaurSpawningInfo.class, new DinosaurSpawningInfo.Deserializer())
            .registerTypeAdapter(DinosaurStatistics.class, new DinosaurStatistics.Deserializer())
            .registerTypeAdapter(DinosaurTraits.class, new DinosaurTraits.Deserializer())
            .registerTypeAdapter(Diet.class, new JsonDiet())
            .registerTypeAdapter(SpawnEggInfo.class, new SpawnEggInfo.Deserializer())
            .create();

}
