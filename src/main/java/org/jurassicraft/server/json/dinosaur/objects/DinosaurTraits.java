package org.jurassicraft.server.json.dinosaur.objects;

import com.google.gson.*;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.Diet;
import org.jurassicraft.server.entity.SleepTime;

import java.lang.reflect.Type;
import java.util.Locale;

public class DinosaurTraits {

    public Dinosaur.DinosaurHomeType homeType;
    public Dinosaur.DinosaurBehaviourType type;
    public Diet diet;
    public SleepTime sleepType;
    public boolean imprintable;
    public boolean defendOwner;
    public int maxAge;
    public int maxHerdSize;
    public double attackBias;
    public boolean canClimb;
    public double flockSpeed;

    public DinosaurTraits(Dinosaur.DinosaurHomeType homeType, Dinosaur.DinosaurBehaviourType type,
                          Diet diet, SleepTime sleepType, boolean imprintable, boolean defendOwner,
                          int maxAge, int maxHerdSize, double attackBias, boolean canClimb, double flockSpeed) {
        this.homeType = homeType;
        this.type = type;
        this.diet = diet;
        this.sleepType = sleepType;
        this.imprintable = imprintable;
        this.defendOwner = defendOwner;
        this.maxAge = maxAge;
        this.maxHerdSize = maxHerdSize;
        this.attackBias = attackBias;
        this.canClimb = canClimb;
        this.flockSpeed = flockSpeed;

    }

    public static class JsonHandler implements JsonDeserializer<DinosaurTraits>, JsonSerializer<DinosaurTraits> {

        @Override
        public DinosaurTraits deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
            }
            JsonObject json = element.getAsJsonObject();
            return new DinosaurTraits(
                    JsonUtils.isString(json, "home_type") ? Dinosaur.DinosaurHomeType.valueOf(JsonUtils.getString(json, "home_type").toUpperCase(Locale.ENGLISH)) : Dinosaur.DinosaurHomeType.LAND,
                    Dinosaur.DinosaurBehaviourType.valueOf(JsonUtils.getString(json, "type").toUpperCase(Locale.ENGLISH)),
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
            json.addProperty("type", src.type.toString().toLowerCase(Locale.ENGLISH));
            json.add("diet", context.serialize(src.diet));
            json.addProperty("sleep_type", src.sleepType.toString().toLowerCase(Locale.ENGLISH));
            json.addProperty("imprintable", src.imprintable);
            json.addProperty("defend_owner", src.defendOwner);
            json.addProperty("maximum_age", (src.maxAge * 8) / 24000); //Convert from ticks to days
            json.addProperty("max_herd_size", src.maxHerdSize);
            json.addProperty("attack_bias", src.attackBias);
            json.addProperty("can_climb", src.canClimb);
            json.addProperty("flock_speed", (float)src.flockSpeed);
            return json;
        }
    }
}
