package org.jurassicraft.server.entity.vehicle.util;

import javax.vecmath.Vector2d;

import net.minecraft.util.math.Vec3d;

public class CarWheel {
    
    private static final double INTERP_AMOUNT = 0.2D; //TODO: Config value ?
        
    private double targetY;
    private double currentY;
    private double previousCurrentY;
    
    private boolean initilizedY;
    
    private Vec3d currentWheelPos = Vec3d.ZERO;

    
    public void setTargetY(double target) {
        if(!initilizedY) {
            initilizedY = true;
            this.previousCurrentY = target;
            this.currentY = target;
        }
        this.targetY = target;
    }
    
    public void onUpdate() {
        this.previousCurrentY = currentY;
    }
    
    public void doInterps() {
        double add;
        if(Math.abs(currentY - targetY) <= INTERP_AMOUNT) {
            add = 0;
            currentY = targetY;
        } else if(currentY < targetY) {
            add = INTERP_AMOUNT;
        } else {
            add = -INTERP_AMOUNT;
        }
        currentY += add;
    }
    
    public double getValueForRendering(float partialTicks) {
        return previousCurrentY + (currentY - previousCurrentY) * partialTicks;
    }
    
    public void setCurrentWheelPos(Vec3d currentWheelPos) {
        this.currentWheelPos = currentWheelPos;
    }
    
    public Vec3d getCurrentWheelPos() {
        return currentWheelPos;
    }
}
