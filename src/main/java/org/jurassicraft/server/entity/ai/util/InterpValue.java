package org.jurassicraft.server.entity.ai.util;

public class InterpValue {
	
	private static final double INTERP_AMOUNT = 0.2D;
	
	private double target;
	private double current;
	private double previousCurrent;
	
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
}
