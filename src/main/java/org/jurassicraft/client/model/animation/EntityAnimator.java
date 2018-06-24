package org.jurassicraft.client.model.animation;

import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
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
public abstract class EntityAnimator<E extends EntityLivingBase & Animatable> implements ITabulaModelAnimator<E> {
    protected EnumMap<GrowthStage, Map<E, JabelarAnimationHandler<E>>> animationHandlers = new EnumMap<>(GrowthStage.class);

    private JabelarAnimationHandler<E> getAnimationHelper(E entity, AnimatableModel model, boolean useInertialTweens) {
        GrowthStage growth = entity.getGrowthStage();
        Map<E, JabelarAnimationHandler<E>> growthToRender = this.animationHandlers.get(growth);

        if (growthToRender == null) {
            growthToRender = new WeakHashMap<>();
            this.animationHandlers.put(growth, growthToRender);
        }

        JabelarAnimationHandler<E> render = growthToRender.get(entity);

        if (render == null) {
            render = entity.<E>getPoseHandler().createAnimationHandler(entity, model, growth, useInertialTweens);
            growthToRender.put(entity, render);
        }

        return render;
    }

    @Override
    public final void setRotationAngles(TabulaModel model, E entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        this.getAnimationHelper(entity, (AnimatableModel) model, entity.shouldUseInertia()).performAnimations(entity, limbSwing, limbSwingAmount, ticks);
        for(int i = 0;true;i++) {
            AdvancedModelRenderer cube = model.getCube("neck" + i++);
            if(cube == null) {
                cube = model.getCube("throat" + i++);
            }
            float j = 1 - (i * 0.00001F);
            if(cube != null ) {
                cube.scaleX *= j;
                cube.scaleY *= j;
                cube.scaleZ *= j;
            } else {
                break;
            }
        }
        this.performAnimations((AnimatableModel) model, entity, limbSwing, limbSwingAmount, ticks, rotationYaw, rotationPitch, scale);
    }

    protected void performAnimations(AnimatableModel parModel, E entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
    }
}
