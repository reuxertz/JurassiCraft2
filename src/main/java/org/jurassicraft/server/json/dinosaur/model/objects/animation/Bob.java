package org.jurassicraft.server.json.dinosaur.model.objects.animation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.json.dinosaur.model.JsonAnimator;
import org.jurassicraft.server.json.dinosaur.model.objects.JsonAnimationModule;
import org.jurassicraft.server.json.dinosaur.model.objects.AnimationInfoBase;

public class Bob extends JsonAnimationModule<Bob.Info> {

    public Bob(JsonArray array, JsonAnimator animator) {
        super(array, animator);
    }

    @Override
    public void performAnimation(TabulaModel model, Entity entity, Info info, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        for (AdvancedModelRenderer renderer : info.getRenderers(model)) {
            model.bob(renderer, info.speed * this.animator.getGlobalSpeed(), info.degree * this.animator.getGlobalDegree(), false, limbSwing, limbSwingAmount);
        }
    }

    @Override
    public Info createValue(JsonObject json, JsonAnimator animator) {
        return new Info(json, animator);
    }


    public static class Info extends AnimationInfoBase {
        private final float speed;
        private final float degree;

        protected Info(JsonObject json, JsonAnimator animator) {
            super(json, animator);
            this.speed = JsonUtils.getFloat(json, "speed");
            this.degree = JsonUtils.getFloat(json, "degree");
        }
    }

}
