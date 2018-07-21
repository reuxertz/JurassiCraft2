package org.jurassicraft.server.json.dinosaur.model;

import com.google.common.collect.Lists;
import com.google.gson.*;
import lombok.Data;
import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.json.dinosaur.model.objects.Constants;
import org.jurassicraft.server.json.dinosaur.model.objects.JsonAnimationType;

import java.lang.reflect.Type;
import java.util.List;

/**
 * The Json handler for
 * @author Wyn Price
 */
@Data
public class JsonAnimator implements ITabulaModelAnimator<Entity> {

    public final float globalSpeed;
    public final float globalDegree;
    public final Constants constants;
    public final List<JsonAnimationType> animationList;

    public JsonAnimator(float globalSpeed, float globalDegree, Constants constants, List animationList) {
        this.globalSpeed = globalSpeed;
        this.globalDegree = globalDegree;
        this.constants = constants;
        this.animationList = animationList;
    }

    @Override
    public void setRotationAngles(TabulaModel model, Entity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        for (JsonAnimationType animation : animationList) {
            animation.performAnimations(model, entity, limbSwing, limbSwingAmount, ticks, rotationYaw, rotationPitch, scale);
        }
    }

    public static final class Deserializer implements JsonDeserializer<JsonAnimator> {
        @Override
        public JsonAnimator deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected a json object, found " + JsonUtils.toString(element));
            }
            JsonObject json = element.getAsJsonObject();
            List<JsonAnimationType> animationList = Lists.newArrayList();
            for (JsonElement jsonElement : JsonUtils.getJsonArray(json, "animations")) {
                animationList.add(context.deserialize(jsonElement, JsonAnimationType.class));
            }
            JsonAnimator animator = new JsonAnimator(
                    JsonUtils.getFloat(json, "global_speed"),
                    JsonUtils.getFloat(json, "global_degree"),
                    context.deserialize(JsonUtils.getJsonArray(json, "constants"), Constants.class),
                    animationList
            );
            for (JsonAnimationType dinosaurJsonAnimation : animationList) {
                dinosaurJsonAnimation.runFactories(animator);
            }
            return animator;
        }
    }
}
