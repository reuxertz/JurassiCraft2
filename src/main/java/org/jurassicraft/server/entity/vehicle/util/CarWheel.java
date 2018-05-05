package org.jurassicraft.server.entity.vehicle.util;

import org.jurassicraft.server.entity.ai.util.InterpValue;

import net.minecraft.util.math.Vec3d;

public class CarWheel extends InterpValue {
    
    private Vec3d currentWheelPos = Vec3d.ZERO;

    public void setCurrentWheelPos(Vec3d currentWheelPos) {
        this.currentWheelPos = currentWheelPos;
    }
    
    public Vec3d getCurrentWheelPos() {
        return currentWheelPos;
    }
}
