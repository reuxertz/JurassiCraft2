package org.jurassicraft.server.json.dinosaur.model.objects;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import org.jurassicraft.client.model.AnimatableModel;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.json.dinosaur.model.JsonDinosaurAnimator;

import java.util.List;

public abstract class AnimationCallable<V> {

    protected final List<V> list = Lists.newArrayList();
    protected final JsonDinosaurAnimator animator;

    protected AnimationCallable(JsonArray array, JsonDinosaurAnimator animator) {
        this.animator = animator;
        String name = this.getClass().toString();
        for (JsonElement jsonElement : array) {
            list.add(this.createValue(JsonUtils.getJsonObject(jsonElement, name), animator));
        }
    }

    public abstract void performAnimations(AnimatableModel model, DinosaurEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale);

    public abstract V createValue(JsonObject json, JsonDinosaurAnimator animator);
}
