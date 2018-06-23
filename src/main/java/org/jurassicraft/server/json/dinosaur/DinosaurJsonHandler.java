package org.jurassicraft.server.json.dinosaur;

import com.google.gson.*;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.Diet;
import org.jurassicraft.server.json.dinosaur.objects.*;

import java.lang.reflect.Type;

public class DinosaurJsonHandler {

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Dinosaur.class, new JsonHandler())
            .registerTypeAdapter(AdultBabyValue.class, new AdultBabyValue.JsonHandler())
            .registerTypeAdapter(DinosaurBreeding.class, new DinosaurBreeding.JsonHandler())
            .registerTypeAdapter(DinosaurProperties.class, new DinosaurProperties.JsonHandler())
            .registerTypeAdapter(DinosaurSpawningInfo.class, new DinosaurSpawningInfo.JsonHandler())
            .registerTypeAdapter(DinosaurStatistics.class, new DinosaurStatistics.JsonHandler())
            .registerTypeAdapter(DinosaurTraits.class, new DinosaurTraits.JsonHandler())
            .registerTypeAdapter(Diet.class, new JsonDiet())
            .registerTypeAdapter(SpawnEggInfo.class, new SpawnEggInfo.JsonHandler())
            .create();

    public static class JsonHandler implements JsonDeserializer<Dinosaur>, JsonSerializer<Dinosaur> {

        @Override
        public Dinosaur deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new JsonDinosaur(context.deserialize(json, DinosaurProperties.class));
        }

        @Override
        public JsonElement serialize(Dinosaur dino, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(new DinosaurProperties(
                    dino.getName(),
                    dino.getDinosaurClass(),
                    dino.getPeriod(),
                    new SpawnEggInfo(dino.getEggPrimaryColorMale(), dino.getEggSecondaryColorMale()),
                    new SpawnEggInfo(dino.getEggPrimaryColorFemale(), dino.getEggSecondaryColorFemale()),
                    new DinosaurStatistics(
                            new AdultBabyValue(dino.getBabySpeed(),     dino.getAdultSpeed()),
                            new AdultBabyValue(dino.getBabyHealth(),    dino.getAdultHealth()),
                            new AdultBabyValue(dino.getBabyStrength(),  dino.getAdultStrength()),
                            new AdultBabyValue(dino.getBabySizeX(),     dino.getAdultSizeX()),
                            new AdultBabyValue(dino.getBabySizeY(),     dino.getAdultSizeY()),
                            new AdultBabyValue(dino.getBabyEyeHeight(), dino.getAdultEyeHeight()),
                            new AdultBabyValue(dino.getScaleInfant(),   dino.getScaleAdult()),
                            dino.getJumpHeight(),
                            dino.getAttackSpeed(),
                            dino.getStorage()
                    ),
                    new DinosaurTraits(
                            dino.getDinosaurType(),
                            dino.getDiet(),
                            dino.getSleepTime(),
                            dino.isImprintable(),
                            dino.shouldDefendOwner(),
                            dino.getMaximumAge(),
                            dino.getMaxHerdSize(),
                            dino.getAttackBias(),
                            dino.canClimb()
                    ),
                    new DinosaurSpawningInfo(dino.getSpawnChance(), dino.getBiomeTypes()),
                    new DinosaurBreeding(
                            dino.getBirthType(),
                            dino.getMinClutch(),
                            dino.getMaxClutch(),
                            dino.getBreedCooldown(),
                            dino.shouldBreedAroundOffspring(), dino.shouldDefendOffspring()
                    ),
                    dino.getBones(),
                    dino.getRecipe()
            ));
        }
    }
}
