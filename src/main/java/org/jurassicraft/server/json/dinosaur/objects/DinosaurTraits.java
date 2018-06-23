package org.jurassicraft.server.json.dinosaur.objects;

import com.google.gson.*;
import lombok.Data;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.Diet;
import org.jurassicraft.server.entity.SleepTime;

import java.lang.reflect.Type;
import java.util.Locale;

@Data
public class DinosaurTraits {

    private final Dinosaur.DinosaurType type;
    private final Diet diet;
    private final SleepTime sleepType;
    private final boolean imprintable;
    private final boolean defendOwner;
    private final int maxAge;
    private final int maxHerdSize;
    private final double attackBias;
    private final boolean canClimb;

    public static class Deserializer implements JsonDeserializer<DinosaurTraits> {

        @Override
        public DinosaurTraits deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
            }
            JsonObject json = element.getAsJsonObject();
            return new DinosaurTraits(
                    Dinosaur.DinosaurType.valueOf(JsonUtils.getString(json, "type").toUpperCase(Locale.ENGLISH)),
                    context.deserialize(JsonUtils.getJsonArray(json, "diet"), Diet.class),
                    SleepTime.valueOf(JsonUtils.getString(json, "sleep_type").toUpperCase(Locale.ENGLISH)),
                    JsonUtils.getBoolean(json, "imprintable"),
                    JsonUtils.getBoolean(json, "defend_owner"),
                    JsonUtils.getInt(json, "maximum_age"),
                    JsonUtils.getInt(json, "max_herd_size"),
                    JsonUtils.getFloat(json, "attack_bias"),
                    JsonUtils.getBoolean(json, "can_climb")
            );
        }
    }
}
