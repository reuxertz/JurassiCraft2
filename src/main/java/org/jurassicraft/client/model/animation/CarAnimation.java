package org.jurassicraft.client.model.animation;

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

public class CarAnimation implements ITabulaModelAnimator<CarEntity> {
    private final List<CarAnimation.Door> doorList = Lists.newArrayList();
    public float partialTicks;
    
    public CarAnimation addDoor(Door door) {
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
