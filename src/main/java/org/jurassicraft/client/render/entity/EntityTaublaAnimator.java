package org.jurassicraft.client.render.entity;

import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.jurassicraft.client.model.AnimatableModel;
import org.jurassicraft.client.model.animation.EntityAnimator;

public class EntityTaublaAnimator extends EntityAnimator {

    private final ITabulaModelAnimator<Entity> animator;

    public EntityTaublaAnimator(ITabulaModelAnimator<Entity> animator) {
        this.animator = animator;
    }

    @Override
    protected void performAnimations(AnimatableModel model, EntityLivingBase entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        this.animator.setRotationAngles(model, entity, limbSwing, limbSwingAmount, ticks, rotationYaw, rotationPitch, scale);
    }
}
