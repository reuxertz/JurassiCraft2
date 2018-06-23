package org.jurassicraft.server.json.dinosaur.objects;

import com.google.gson.*;
import lombok.Data;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.Diet;
import org.jurassicraft.server.entity.SleepTime;
import org.jurassicraft.server.json.JsonUtil;

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
    private final double flockSpeed;

    public static class JsonHandler implements JsonDeserializer<DinosaurTraits>, JsonSerializer<DinosaurTraits> {

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
                    JsonUtils.getBoolean(json, "can_climb"),
                    JsonUtils.getFloat(json, "flock_speed")
            );
        }

        @Override
        public JsonElement serialize(DinosaurTraits src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            json.addProperty("type", src.getType().toString().toLowerCase(Locale.ENGLISH));
            json.add("diet", context.serialize(src.getDiet()));
            json.addProperty("sleep_type", src.getSleepType().toString().toLowerCase(Locale.ENGLISH));
            json.addProperty("imprintable", src.isImprintable());
            json.addProperty("defend_owner", src.isDefendOwner());
            json.addProperty("maximum_age", (src.getMaxAge() * 8) / 24000); //Convert from ticks to days
            json.addProperty("max_herd_size", src.getMaxHerdSize());
            json.addProperty("attack_bias", src.getAttackBias());
            json.addProperty("can_climb", src.isCanClimb());
            json.addProperty("flock_speed", (float)src.getFlockSpeed());
            return json;
        }
    }
}
