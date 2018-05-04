package org.jurassicraft.server.entity.vehicle.util;

import net.minecraft.util.math.Vec3d;

public class WheelVec {

    public double x;
    public double y;
    public double z;
    
    public double targetY;
    
    public Vec3d actualPos;
    
    public WheelVec(double x, double y, double z, double targetY, Vec3d actualPos) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.targetY = targetY;
        this.actualPos = actualPos;
    }
    
    public Vec3d getPos() {
        return new Vec3d(x, y, z);
    }

}
