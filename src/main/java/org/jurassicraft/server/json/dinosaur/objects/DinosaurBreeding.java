package org.jurassicraft.server.json.dinosaur.objects;

import com.google.gson.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.dinosaur.Dinosaur;

import java.lang.reflect.Type;
import java.util.Locale;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DinosaurBreeding {

   Dinosaur.BirthType birthType;
   int minClutch;
   int maxClutch;
   int breedingCooldown;
   boolean breedNearOffsprring;
   boolean defendOffspring;

    public static class JsonHandler implements JsonDeserializer<DinosaurBreeding>, JsonSerializer<DinosaurBreeding> {
        @Override
        public DinosaurBreeding deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
            }
            JsonObject json = element.getAsJsonObject();
            return new DinosaurBreeding(
                    Dinosaur.BirthType.valueOf(JsonUtils.getString(json, "birth_type").toUpperCase(Locale.ENGLISH)),
                    JsonUtils.getInt(json, "min_clutch"),
                    JsonUtils.getInt(json, "max_clutch"),
                    JsonUtils.getInt(json, "breeding_cooldown"),
                    JsonUtils.getBoolean(json, "breed_near_offspring"),
                    JsonUtils.getBoolean(json, "defend_offspring")
            );
        }

        @Override
        public JsonElement serialize(DinosaurBreeding src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            json.addProperty("birth_type", src.getBirthType().toString().toLowerCase(Locale.ENGLISH));
            json.addProperty("min_clutch", src.minClutch);
            json.addProperty("max_clutch", src.maxClutch);
            json.addProperty("breeding_cooldown", src.breedingCooldown);
            json.addProperty("breed_near_offspring", src.breedNearOffsprring);
            json.addProperty("defend_offspring", src.defendOffspring);
            return json;
        }
    }
}
