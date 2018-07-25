package org.jurassicraft.server.json.dinosaur.model.objects.animation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraft.entity.Entity;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.json.dinosaur.model.JsonAnimator;
import org.jurassicraft.server.json.dinosaur.model.objects.JsonAnimationModule;
import org.jurassicraft.server.json.dinosaur.model.objects.AnimationInfoBase;

public class BufferSwing extends JsonAnimationModule<AnimationInfoBase> {

    public BufferSwing(JsonArray array, JsonAnimator animator) {
        super(array, animator);
    }

    @Override
    public void performAnimation(TabulaModel model, Entity entity, AnimationInfoBase info, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        if(entity instanceof DinosaurEntity) {
            ((DinosaurEntity)entity).tailBuffer.applyChainSwingBuffer(info.getRenderers(model));
        }
    }

    @Override
    public AnimationInfoBase createValue(JsonObject json, JsonAnimator animator) {
        return new AnimationInfoBase(json, animator);
    }
}
