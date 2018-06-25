package org.jurassicraft.server.json.dinosaur.model.objects.animation.chainwave;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.client.model.AnimatableModel;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.json.dinosaur.model.JsonDinosaurAnimator;
import org.jurassicraft.server.json.dinosaur.model.objects.AnimationCallable;
import org.jurassicraft.server.json.dinosaur.model.objects.AnimationInfoBase;

public class ChainWaveIdleTick extends AnimationCallable<ChainWaveIdleTick.Info> {

    public ChainWaveIdleTick(JsonArray array, JsonDinosaurAnimator animator) {
        super(array, animator);
    }

    @Override
    public void performAnimations(AnimatableModel model, DinosaurEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        for (Info info : this.list) {
            model.chainWave(info.getRenderers(model), info.speed, info.degree, info.rootOffset, ticks, 0.25F);
        }
    }

    @Override
    public Info createValue(JsonObject json, JsonDinosaurAnimator animator) {
        return new Info(json, animator);
    }

    public static class Info extends AnimationInfoBase {
        private final float speed;
        private final float degree;
        private final float rootOffset;

        protected Info(JsonObject json, JsonDinosaurAnimator animator) {
            super(json, animator);
            this.speed = JsonUtils.getFloat(json, "speed");
            this.degree = JsonUtils.getFloat(json, "degree");
            this.rootOffset = JsonUtils.getFloat(json, "root_offset");
        }
    }
}
