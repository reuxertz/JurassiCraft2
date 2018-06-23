package org.jurassicraft.server.json.dinosaur.objects;

import com.google.gson.*;
import lombok.Data;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.dinosaur.Dinosaur;

import java.lang.reflect.Type;

@Data
public class DinosaurBreeding {

    private final Dinosaur.BirthType birthType;
    private final int minClutch;
    private final int maxClutch;
    private final int breedingCooldown;
    private final boolean breedNearOffsprring;
    private final boolean defendOffspring;

    public static class Deserializer implements JsonDeserializer<DinosaurBreeding> {
        @Override
        public DinosaurBreeding deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
            }
            JsonObject json = element.getAsJsonObject();
            return new DinosaurBreeding(
                    Dinosaur.BirthType.valueOf(JsonUtils.getString(json, "birth_type")),
                    JsonUtils.getInt(json, "min_clutch"),
                    JsonUtils.getInt(json, "max_clutch"),
                    JsonUtils.getInt(json, "breeding_cooldown"),
                    JsonUtils.getBoolean(json, "breed_near_offspring"),
                    JsonUtils.getBoolean(json, "defend_offspring")
            );
        }
    }
}
