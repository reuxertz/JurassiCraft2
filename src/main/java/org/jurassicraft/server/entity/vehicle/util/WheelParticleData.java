package org.jurassicraft.server.entity.vehicle.util;

import org.jurassicraft.server.entity.vehicle.CarEntity;

import net.minecraft.util.math.Vec3d;

public class WheelParticleData {
    private int age;
    
    private final Vec3d position;
    private final int maxAge = 200;
    
    private final CarEntity parentEntity;
    
    public WheelParticleData(CarEntity parentEntity, Vec3d position) {
        this.parentEntity = parentEntity;
        this.position = position;
    }
    
    public void onUpdate() {
        if (this.age++ >= this.maxAge) {
            this.parentEntity.markRemoval(this);
        }
    }
    
    public Vec3d getPosition() {
        return position;
    }
    
    public float getAlpha(float partialTicks) {
	if(age > 199) {
	    return 0f;
	}
	float f = (float) Math.pow(((double)this.age + partialTicks) / (double)this.maxAge, 2);
        float f1 = 2.0F - f * 2.0F;

        if (f1 > 1.0F)
        {
            f1 = 1.0F;
        }

        f1 = f1 * 0.3F;
        return f1;
    }
    
    public CarEntity getParentEntity() {
	return parentEntity;
    }
}
