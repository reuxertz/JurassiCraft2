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
	    value.setTarget(Math.toRadians(door.hasEntity(entity) || entity.estimatedSpeed >= 0.02D /*TODO: config ?*/? 0F : door.isLeft() ? 60F : -60F));
	    model.getCube(door.getName()).rotateAngleY = (float) value.getValueForRendering(LLibrary.PROXY.getPartialTicks());
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
	
	public boolean hasEntity(CarEntity entity) {
	    return entity.getEntityInSeat(seatIndex) != null;
	}
	
	public boolean isLeft() {
	    return isLeft;
	}
	
    }

}
