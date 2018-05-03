package org.jurassicraft.server.entity.ai.util;

public class InterpValue {
	
	private static final double INTERP_AMOUNT = 0.2D;
	
	private double target;
	private double current;
	private double previousCurrent;
	
	private boolean initilized;
	
	public void setCurrent(double current) {
		this.current = current;
		this.target = current;
		this.previousCurrent = current;
	}
	
	public void setTarget(double target) {
		this.target = target;
	}
	
	public void preTick() {
		this.previousCurrent = current;
	}
	
	public void tick() {
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
	
	public boolean hasInitilized() {
		return initilized;
	}
	
	public void initilize() {
		initilized = true;
	}
	
	public double getCurrent() {
		return current;
	}
}
