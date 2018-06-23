package org.jurassicraft.server.json.dinosaur.objects;

import com.google.gson.*;
import lombok.Data;
import net.minecraft.util.JsonUtils;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;

@Data
public class AdultBabyValue {
    private final float adult;
    private final float baby;
    public static class Deserializer implements JsonDeserializer<AdultBabyValue> {

        @Override
        public AdultBabyValue deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
            }
            JsonObject json = element.getAsJsonObject();
            return new AdultBabyValue(JsonUtils.getFloat(json, "baby"), JsonUtils.getFloat(json, "adult"));
        }
    }

    public void apply(BiConsumer<Integer, Integer> consumer) {

    }

}
