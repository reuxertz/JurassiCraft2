package org.jurassicraft.server.entity.vehicle.util;

import javax.vecmath.Vector2d;

import net.minecraft.util.math.Vec3d;

public class CarWheel {
    
    private final Vector2d relativeWheelPosition;        
    private Vec3d currentWheelPos = Vec3d.ZERO;
    
    public CarWheel(Vector2d relativeWheelPosition) {
	this.relativeWheelPosition = relativeWheelPosition;
    }
    
    public Vector2d getRelativeWheelPosition() {
	return relativeWheelPosition;
    }
    
    public void setCurrentWheelPos(Vec3d currentWheelPos) {
        this.currentWheelPos = currentWheelPos;
    }
    
    public Vec3d getCurrentWheelPos() {
        return currentWheelPos;
    }
}