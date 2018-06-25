package org.jurassicraft.server.json.dinosaur.model;

import com.google.common.collect.Lists;
import com.google.gson.*;
import lombok.Data;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.client.model.AnimatableModel;
import org.jurassicraft.client.model.animation.EntityAnimator;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.json.dinosaur.model.objects.Constants;
import org.jurassicraft.server.json.dinosaur.model.objects.DinosaurJsonAnimation;

import java.lang.reflect.Type;
import java.util.List;

@Data
public class JsonDinosaurAnimator extends EntityAnimator<DinosaurEntity> {


    private final float globalSpeed;
    private final float globalDegree;
    private final Constants constants;
    private final List<DinosaurJsonAnimation> animationList;

    @Override
    protected void performAnimations(AnimatableModel model, DinosaurEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        for (DinosaurJsonAnimation animation : animationList) {
            animation.performAnimations(model, entity, limbSwing, limbSwingAmount, ticks, rotationYaw, rotationPitch, scale);
        }
    }

    public static final class Deserializer implements JsonDeserializer<JsonDinosaurAnimator> {
        @Override
        public JsonDinosaurAnimator deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!element.isJsonObject()) {
                throw new JsonParseException("Expected a json object, found " + JsonUtils.toString(element));
            }
            JsonObject json = element.getAsJsonObject();
            List<DinosaurJsonAnimation> animationList = Lists.newArrayList();
            for (JsonElement jsonElement : JsonUtils.getJsonArray(json, "animations")) {
                animationList.add(context.deserialize(jsonElement, DinosaurJsonAnimation.class));
            }
            JsonDinosaurAnimator animator = new JsonDinosaurAnimator(
                    JsonUtils.getFloat(json, "global_speed"),
                    JsonUtils.getFloat(json, "global_degree"),
                    context.deserialize(JsonUtils.getJsonArray(json, "constants"), Constants.class),
                    animationList
            );
            for (DinosaurJsonAnimation dinosaurJsonAnimation : animationList) {
                dinosaurJsonAnimation.runFactories(animator);
            }
            return animator;
        }
    }
}
