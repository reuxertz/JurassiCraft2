package org.jurassicraft.client.model.animation.entity.vehicle;

import org.jurassicraft.client.model.animation.CarAnimation;
import org.jurassicraft.server.entity.ai.util.InterpValue;
import org.jurassicraft.server.entity.vehicle.CarEntity;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class JeepWranglerAnimator extends CarAnimation {
    
    private final InterpValue steerAmount = new InterpValue(0.1D);
    
    @Override
    public void setRotationAngles(TabulaModel model, CarEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        super.setRotationAngles(model, entity, limbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, scale);
	
	AdvancedModelRenderer wheelHolderFront = model.getCube("wheel holder front");
        AdvancedModelRenderer wheelHolderBack = model.getCube("wheel holder back");

        float wheelRotation = entity.prevWheelRotateAmount + (entity.wheelRotateAmount - entity.prevWheelRotateAmount) * partialTicks;
        float wheelRotationAmount = entity.wheelRotation - entity.wheelRotateAmount * (1.0F - partialTicks);

        if (entity.backward()) {
            wheelRotationAmount = -wheelRotationAmount;
        }
        
        wheelHolderFront.rotateAngleX = wheelRotationAmount * 0.5F;
        wheelHolderBack.rotateAngleX = wheelRotationAmount * 0.5F;

        steerAmount.setTarget(Math.toRadians(entity.left() ? 40.0F : entity.right() ? -40.0F : 0.0F) * wheelRotation);
        
        float steerAmount = (float) this.steerAmount.getValueForRendering(partialTicks);
        
        model.getCube("steering wheel main").rotateAngleZ = steerAmount;
        wheelHolderFront.rotateAngleY = -steerAmount * 0.15F;
    }
}
