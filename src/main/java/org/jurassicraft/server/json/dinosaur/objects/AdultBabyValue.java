package org.jurassicraft.server.json.dinosaur.objects;

import com.google.gson.*;
import lombok.Data;
import net.minecraft.util.JsonUtils;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;

@Data
public class AdultBabyValue {
    private final double baby;
    private final double adult;
    public static class JsonHandler implements JsonDeserializer<AdultBabyValue>, JsonSerializer<AdultBabyValue> {

        @Override
        public AdultBabyValue deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
            }
            JsonObject json = element.getAsJsonObject();
            return new AdultBabyValue(Math.round(JsonUtils.getFloat(json, "baby") * 100F) / 100F, Math.round(JsonUtils.getFloat(json, "adult") * 100F) / 100F);
        }

        @Override
        public JsonElement serialize(AdultBabyValue src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            json.addProperty("adult", (float)src.adult);
            json.addProperty("baby", (float)src.baby);
            return json;
        }
    }

    public void apply(BiConsumer<Float, Float> consumer) {
        consumer.accept((float)baby, (float)adult);
    }

}
