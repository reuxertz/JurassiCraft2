package org.jurassicraft.client.render.entity;

import java.util.stream.IntStream;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jurassicraft.server.entity.ai.util.MathUtils;
import org.jurassicraft.server.entity.vehicle.CarEntity;

@SideOnly(Side.CLIENT)
public abstract class CarRenderer<E extends CarEntity> extends Render<E> {
    private static final ResourceLocation[] DESTROY_STAGES = IntStream.range(0, 10)
        .mapToObj(n -> new ResourceLocation(String.format("textures/blocks/destroy_stage_%d.png", n)))
        .toArray(ResourceLocation[]::new);

    protected CarRenderer(RenderManager renderManager) {
        super(renderManager);
    }
    
    public static void doCarRotations(CarEntity entity, float partialTicks) {
	double backWheel = entity.backValue.getValueForRendering(partialTicks);
        double frontWheel = entity.frontValue.getValueForRendering(partialTicks);
        double leftWheel = entity.leftValue.getValueForRendering(partialTicks);
        double rightWheel = entity.rightValue.getValueForRendering(partialTicks);
        GlStateManager.translate(0, -0.5, 1.4);
        float localRotationPitch = (float) MathUtils.cosineFromPoints(new Vec3d(frontWheel, 0, -2.5f), new Vec3d(backWheel, 0, -2.5f), new Vec3d(backWheel, 0, 2f));//No need for cosine as is a right angled triangle. I'm to lazy to work out the right maths. //TODO: SOHCAHTOA this
        GlStateManager.rotate(frontWheel < backWheel ? -localRotationPitch : localRotationPitch, 1, 0, 0);
        GlStateManager.translate(0, 0.5, -1.4);
        float localRotationRoll = (float) MathUtils.cosineFromPoints(new Vec3d(rightWheel, 0, -1.3f), new Vec3d(leftWheel, 0, -1.3f), new Vec3d(leftWheel, 0, 1.3f));//TODO: same as above
        GlStateManager.rotate(leftWheel < rightWheel ? localRotationRoll : -localRotationRoll, 0, 0, 1);
    }
}
