package org.jurassicraft.server.json.dinosaur.model.objects.animation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.client.model.AnimatableModel;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.json.dinosaur.model.JsonDinosaurAnimator;
import org.jurassicraft.server.json.dinosaur.model.objects.AnimationCallable;
import org.jurassicraft.server.json.dinosaur.model.objects.AnimationInfoBase;

public class Bob extends AnimationCallable<Bob.Info> {

    public Bob(JsonArray array, JsonDinosaurAnimator animator) {
        super(array, animator);
    }

    @Override
    public void performAnimations(AnimatableModel model, DinosaurEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        for (Info info : this.list) {
            for (AdvancedModelRenderer renderer : info.getRenderers(model)) {
                model.bob(renderer, info.speed * this.animator.getGlobalSpeed(), info.degree * this.animator.getGlobalDegree(), false, limbSwing, limbSwingAmount);
            }
        }
    }

    @Override
    public Info createValue(JsonObject json, JsonDinosaurAnimator animator) {
        return new Info(json, animator);
    }


    public static class Info extends AnimationInfoBase {
        private final float speed;
        private final float degree;

        protected Info(JsonObject json, JsonDinosaurAnimator animator) {
            super(json, animator);
            this.speed = JsonUtils.getFloat(json, "speed");
            this.degree = JsonUtils.getFloat(json, "degree");
        }
    }

}
