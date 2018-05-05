package org.jurassicraft.server.entity.ai.util;

import net.minecraft.util.math.Vec3d;

public class InterpValue {
    
    protected static final double INTERP_AMOUNT = 0.25D; //TODO: Config value ?
    
    private double target;
    private double current;
    private double previousCurrent;
    
    private boolean initilized;
    
    public void setTarget(double target) {
        if(!initilized) {
            initilized = true;
            this.previousCurrent = target;
            this.current = target;
        }
        this.target = target;
    }
    
    public void onEntityUpdate() {
        this.previousCurrent = current;
    }
    
    public void doInterps() {
        double add;
        if(Math.abs(current - target) <= INTERP_AMOUNT) {
            add = 0;
            current = target;
        } else if(current < target) {
            add = INTERP_AMOUNT;
        } else {
            add = -INTERP_AMOUNT;
        }
        current += add;
    }
    
    public double getValueForRendering(float partialTicks) {
        return previousCurrent + (current - previousCurrent) * partialTicks;
    }
    
    public double getCurrent() {
	return current;
    }
}