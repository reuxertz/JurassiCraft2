package org.jurassicraft.server.json.dinosaur.objects;

import com.google.gson.*;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.entity.Diet;
import org.jurassicraft.server.food.FoodType;

import java.lang.reflect.Type;
import java.util.Locale;

public class JsonDiet implements JsonDeserializer<Diet> {
    @Override
    public Diet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(!json.isJsonArray()) {
            throw new JsonSyntaxException("Expected a Json array, found " + JsonUtils.toString(json));
        }
        Diet diet = new Diet();
        for(JsonElement element : json.getAsJsonArray()) {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
            }
            JsonObject object = element.getAsJsonObject();
            Diet.DietModule module = new Diet.DietModule(FoodType.valueOf(JsonUtils.getString(object, "type").toUpperCase(Locale.ENGLISH)));
            if(object.has("conditions")) {
                for(JsonElement conditionElement : JsonUtils.getJsonArray(object, "conditions")) {
                    if(!conditionElement.isJsonPrimitive() || !conditionElement.getAsJsonPrimitive().isString()) {
                        throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(conditionElement));
                    }
                    module.withCondition(DietConditionType.valueOf(conditionElement.getAsString().toUpperCase(Locale.ENGLISH)).getFunction());
                }
            }
            diet.withModule(module);
        }
        return diet;
    }
}
