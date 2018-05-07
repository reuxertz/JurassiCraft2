package org.jurassicraft.client.render.entity;

import java.util.stream.IntStream;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector4d;

import org.jurassicraft.server.entity.ai.util.MathUtils;
import org.jurassicraft.server.entity.vehicle.CarEntity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class CarRenderer<E extends CarEntity> extends Render<E> {
    private static final ResourceLocation[] DESTROY_STAGES = IntStream.range(0, 10)
        .mapToObj(n -> new ResourceLocation(String.format("textures/blocks/destroy_stage_%d.png", n)))
        .toArray(ResourceLocation[]::new);

    protected CarRenderer(RenderManager renderManager) {
        super(renderManager);
    }
    
    public static void doCarRotations(CarEntity entity, float partialTicks) {
	double backValue = entity.backValue.getValueForRendering(partialTicks);
        double frontValue = entity.frontValue.getValueForRendering(partialTicks);
        double leftValue = entity.leftValue.getValueForRendering(partialTicks);
        double rightValue = entity.rightValue.getValueForRendering(partialTicks);
        
        Vector4d vec = entity.getCarDimensions();
        Vector2d rot = entity.getBackWheelRotationPoint();
        	
        GlStateManager.translate(0, rot.x, rot.y);
        float localRotationPitch = (float) MathUtils.cosineFromPoints(new Vec3d(frontValue, 0, vec.w), new Vec3d(backValue, 0, vec.w), new Vec3d(backValue, 0, vec.y));//No need for cosine as is a right angled triangle. I'm to lazy to work out the right maths. //TODO: SOHCAHTOA this
        GlStateManager.rotate(frontValue < backValue ? -localRotationPitch : localRotationPitch, 1, 0, 0);
        GlStateManager.translate(0, -rot.x, -rot.y);
        float localRotationRoll = (float) MathUtils.cosineFromPoints(new Vec3d(rightValue, 0, vec.z), new Vec3d(leftValue, 0, vec.z), new Vec3d(leftValue, 0, vec.x));//TODO: same as above
        GlStateManager.rotate(leftValue < rightValue ? localRotationRoll : -localRotationRoll, 0, 0, 1);
    }
}
