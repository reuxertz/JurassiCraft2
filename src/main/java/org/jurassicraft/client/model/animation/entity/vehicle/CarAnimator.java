package org.jurassicraft.client.model.animation.entity.vehicle;

import java.util.List;

import org.jurassicraft.client.event.ClientEventHandler;
import org.jurassicraft.server.entity.ai.util.InterpValue;
import org.jurassicraft.server.entity.vehicle.CarEntity;

import com.google.common.collect.Lists;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CarAnimator implements ITabulaModelAnimator<CarEntity> {
    private final List<CarAnimator.Door> doorList = Lists.newArrayList();
    public float partialTicks;
    private final InterpValue steerAmount = new InterpValue(0.1D);

    
    public CarAnimator addDoor(Door door) {
	this.doorList.add(door);
	return this;
    }
    
    @Override
    public void setRotationAngles(TabulaModel model, CarEntity entity, float limbSwing, float limbSwingAmount,
	    float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
	doorList.forEach(door -> {
	    InterpValue value = door.getInterpValue();
	    CarEntity.Seat seat = door.getSeat(entity);
	    CarEntity.Seat closestSeat = seat;
	    EntityPlayer player = Minecraft.getMinecraft().player;
	    Vec3d playerPos = player.getPositionVector();
	    for(Door door1 : this.doorList) {
		if(door1.getSeat(entity).getPos().distanceTo(playerPos) < closestSeat.getPos().distanceTo(playerPos)) {
		    closestSeat = door1.getSeat(entity);
		}
	    }
	    value.setTarget(Math.toRadians(seat.getOccupant() != null || closestSeat != seat || closestSeat.getPos().distanceTo(playerPos) > 4D ? 0F : door.isLeft() ? 60F : -60F));
	    model.getCube(door.getName()).rotateAngleY = (float) value.getValueForRendering(partialTicks);
	});
	
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
    
    public static class Door {
	private final InterpValue interpValue = new InterpValue(0.1D);
	private final String name;
	private final int seatIndex;
	private final boolean isLeft;
	
	public Door(String name, int seatIndex, boolean isLeft) {
	    this.name = name;
	    this.seatIndex = seatIndex;
	    this.isLeft = isLeft;
	}
	
	public InterpValue getInterpValue() {
	    return interpValue;
	}
	
	public String getName() {
	    return name;
	}
	
	public CarEntity.Seat getSeat(CarEntity entity) {
	    return entity.getSeat(seatIndex);
	}
	
	public boolean isLeft() {
	    return isLeft;
	}
	
    }

}