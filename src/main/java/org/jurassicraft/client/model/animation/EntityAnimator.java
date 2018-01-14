package org.jurassicraft.client.model.animation;

import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.model.AnimatableModel;
import org.jurassicraft.server.api.Animatable;
import org.jurassicraft.server.entity.GrowthStage;

import java.util.EnumMap;
import java.util.Map;
import java.util.WeakHashMap;

@SideOnly(Side.CLIENT)
public abstract class EntityAnimator<ENTITY extends EntityLivingBase & Animatable> implements ITabulaModelAnimator<ENTITY> {
    protected EnumMap<GrowthStage, Map<ENTITY, JabelarAnimationHandler<ENTITY>>> animationHandlers = new EnumMap<>(GrowthStage.class);

    private JabelarAnimationHandler<ENTITY> getAnimationHelper(ENTITY entity, AnimatableModel model, boolean useInertialTweens) {
        GrowthStage growth = entity.getGrowthStage();
        Map<ENTITY, JabelarAnimationHandler<ENTITY>> growthToRender = this.animationHandlers.get(growth);

        if (growthToRender == null) {
            growthToRender = new WeakHashMap<>();
            this.animationHandlers.put(growth, growthToRender);
        }

        JabelarAnimationHandler<ENTITY> render = growthToRender.get(entity);

        if (render == null) {
            render = entity.<ENTITY>getPoseHandler().createAnimationHandler(entity, model, growth, useInertialTweens);
            growthToRender.put(entity, render);
        }

        return render;
    }

    @Override
    public final void setRotationAngles(TabulaModel model, ENTITY entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        this.getAnimationHelper(entity, (AnimatableModel) model, entity.shouldUseInertia()).performAnimations(entity, limbSwing, limbSwingAmount, ticks);

        this.performAnimations((AnimatableModel) model, entity, limbSwing, limbSwingAmount, ticks, rotationYaw, rotationPitch, scale);
    }

    protected void performAnimations(AnimatableModel parModel, ENTITY entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
    }
}
