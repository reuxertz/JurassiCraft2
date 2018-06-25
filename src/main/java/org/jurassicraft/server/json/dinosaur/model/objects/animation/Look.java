package org.jurassicraft.server.json.dinosaur.model.objects.animation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.client.model.AnimatableModel;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.json.dinosaur.model.JsonDinosaurAnimator;
import org.jurassicraft.server.json.dinosaur.model.objects.AnimationCallable;
import org.jurassicraft.server.json.dinosaur.model.objects.AnimationInfoBase;

public class Look extends AnimationCallable<Look.Info> {

    public Look(JsonArray array, JsonDinosaurAnimator animator) {
        super(array, animator);
    }

    @Override
    public void performAnimations(AnimatableModel model, DinosaurEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        for (Info info : this.list) {
            model.faceTarget(rotationYaw, rotationPitch, info.divisor, info.getRenderers(model));
        }
    }

    @Override
    public Info createValue(JsonObject json, JsonDinosaurAnimator animator) {
        return new Info(json, animator);
    }

    public static class Info extends AnimationInfoBase {

        private final float divisor;

        protected Info(JsonObject json, JsonDinosaurAnimator animator) {
            super(json, animator);
            this.divisor = JsonUtils.getFloat(json, "divisor");
        }
    }
}
