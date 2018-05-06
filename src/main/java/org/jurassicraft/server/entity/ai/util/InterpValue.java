package org.jurassicraft.server.entity.ai.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.INBTSerializable;

public class InterpValue implements INBTSerializable<NBTTagCompound> {
    
    protected static final double INTERP_AMOUNT = 0.25D; //TODO: Config value ?
    
    private double target;
    private double current;
    private double previousCurrent;
    
    private boolean initilized;
    
    public void setTarget(double target) {
        if(!initilized) {
            initilized = true;
            reset(target);
        } else {
            this.target = target;
        }
    }
    
    public void reset(double target) {
	this.previousCurrent = target;
        this.current = target;
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

    @Override
    public NBTTagCompound serializeNBT() {
	NBTTagCompound tag = new NBTTagCompound();
	tag.setDouble("target", target);
	tag.setDouble("current", current);
	return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
	this.target = nbt.getDouble("target");
	this.current = nbt.getDouble("current");
	this.previousCurrent = current;
    }
}