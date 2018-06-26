package org.jurassicraft.server.json.dinosaur.model.objects.animation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.server.json.dinosaur.model.JsonAnimator;
import org.jurassicraft.server.json.dinosaur.model.objects.JsonAnimationModule;
import org.jurassicraft.server.json.dinosaur.model.objects.AnimationInfoBase;

public class Facing extends JsonAnimationModule<Facing.Info> {

    public Facing(JsonArray array, JsonAnimator animator) {
        super(array, animator);
    }

    @Override
    public void performAnimation(TabulaModel model, Entity entity, Info info, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        model.faceTarget(rotationYaw, rotationPitch, info.divisor, info.getRenderers(model));
    }

    @Override
    public Info createValue(JsonObject json, JsonAnimator animator) {
        return new Info(json, animator);
    }

    public static class Info extends AnimationInfoBase {

        private final float divisor;

        protected Info(JsonObject json, JsonAnimator animator) {
            super(json, animator);
            this.divisor = JsonUtils.getFloat(json, "divisor");
        }
    }
}
