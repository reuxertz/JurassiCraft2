package org.jurassicraft.server.json.dinosaur.objects;

import com.google.gson.*;
import net.minecraft.util.JsonUtils;

import java.lang.reflect.Type;
import java.util.function.Consumer;

public class AdultBabyValue {
    double baby;
    double adult;

    public AdultBabyValue(double baby, double adult) {
        this.baby = baby;
        this.adult = adult;
    }

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


    public void apply(Consumer<Float> babyCons, Consumer<Float> adultCons) {
        babyCons.accept((float)this.baby);
        adultCons.accept((float)this.adult);
    }

    public double get(String bOrA){
        if(bOrA == "baby"){
            return this.baby;
        }else{
            return this.adult;
        }
    }

    public void set(String bOrA, double toSet){
        if(bOrA == "baby"){
            this.baby = toSet;
        }else{
            this.adult = toSet;
        }
    }

}
