package org.jurassicraft.server.json.dinosaur.model.objects;

import com.google.gson.*;
import lombok.Data;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.json.dinosaur.model.JsonDinosaurAnimator;

import java.lang.reflect.Type;

@Data
public class JsonDinosaurModel {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(JsonDinosaurModel.class, new Deserializer())
            .registerTypeAdapter(JsonDinosaurAnimator.class, new JsonDinosaurAnimator.Deserializer())
            .registerTypeAdapter(Constants.class, new Constants.Deserializer())
            .registerTypeAdapter(DinosaurJsonAnimation.class, new DinosaurJsonAnimation.Deserializer())
            .create();

    private final String headCuboid;
    private final JsonDinosaurAnimator animator;
    private final float shadowSize;

    public static class Deserializer implements JsonDeserializer<JsonDinosaurModel> {
        @Override
        public JsonDinosaurModel deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
            }
            JsonObject json = element.getAsJsonObject();
            return new JsonDinosaurModel(
                    JsonUtils.getString(json, "head_cuboid"),
                    context.deserialize(JsonUtils.getJsonObject(json, "animator"), JsonDinosaurAnimator.class),
                    JsonUtils.getFloat(json, "shadow_size"));
        }
    }

}
