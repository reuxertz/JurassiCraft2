package org.jurassicraft.server.json.dinosaur.model.objects.animation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jurassicraft.client.model.AnimatableModel;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.json.dinosaur.model.JsonDinosaurAnimator;
import org.jurassicraft.server.json.dinosaur.model.objects.AnimationCallable;
import org.jurassicraft.server.json.dinosaur.model.objects.AnimationInfoBase;

public class BufferSwing extends AnimationCallable<AnimationInfoBase> {

    public BufferSwing(JsonArray array, JsonDinosaurAnimator animator) {
        super(array, animator);
    }

    @Override
    public void performAnimations(AnimatableModel model, DinosaurEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        for (AnimationInfoBase info : this.list) {
            entity.tailBuffer.applyChainSwingBuffer(info.getRenderers(model));
        }
    }

    @Override
    public AnimationInfoBase createValue(JsonObject json, JsonDinosaurAnimator animator) {
        return new AnimationInfoBase(json, animator);
    }
}
