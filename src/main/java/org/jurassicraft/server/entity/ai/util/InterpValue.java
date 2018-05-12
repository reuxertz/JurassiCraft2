package org.jurassicraft.server.entity.ai.util;

import java.util.List;

import org.jurassicraft.JurassiCraft;

import com.google.common.collect.Lists;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid=JurassiCraft.MODID)
public class InterpValue implements INBTSerializable<NBTTagCompound> {
    
    private static final List<InterpValue> INSTANCES = Lists.newArrayList();
    
    private final double amount;
    
    private double target;
    private double current;
    private double previousCurrent;
    
    private boolean initilized;
    
    public InterpValue(double amount) {
	this.amount = amount;
	INSTANCES.add(this);
    }
    
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
    
    private void tickInterp() {
        this.previousCurrent = current;
        if(Math.abs(current - target) <= amount) {
            current = target;
        } else if(current < target) {
            current += amount;
        } else {
            current -= amount;
        }
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
    
    @SubscribeEvent
    public static void onTick(TickEvent event) {
	Side side = FMLCommonHandler.instance().getSide();
	if((event instanceof ClientTickEvent && side.isClient()) || (event instanceof ServerTickEvent && side.isServer())) {
	    INSTANCES.forEach(InterpValue::tickInterp);
	} 
    }
}