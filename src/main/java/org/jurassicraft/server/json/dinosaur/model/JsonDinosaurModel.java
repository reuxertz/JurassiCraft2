package org.jurassicraft.server.json.dinosaur.model;

import com.google.gson.*;
import lombok.Data;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.json.dinosaur.JsonDinosaur;

import java.lang.reflect.Type;

@Data
public class JsonDinosaurModel {

    public final String headCuboid;
    public final JsonAnimator animator;
    public final float shadowSize;

    public JsonDinosaurModel(String headCuboid, JsonAnimator animator, float shadowSize) {
        this.headCuboid = headCuboid;
        this.animator = animator;
        this.shadowSize = shadowSize;
    }

    public static class Deserializer implements JsonDeserializer<JsonDinosaurModel> {
        @Override
        public JsonDinosaurModel deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected Json Object, found " + JsonUtils.toString(element));
            }
            JsonObject json = element.getAsJsonObject();
            return new JsonDinosaurModel(
                    JsonUtils.getString(json, "head_cuboid"),
                    context.deserialize(JsonUtils.getJsonObject(json, "animator"), JsonAnimator.class),
                    JsonUtils.getFloat(json, "shadow_size"));
        }
    }

}
