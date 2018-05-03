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
		current += Math.abs(current - target) <= INTERP_AMOUNT ? 0 : current < target ? INTERP_AMOUNT : -INTERP_AMOUNT;
	}
	
	public double getValueForRendering(float partialTicks) {
		return previousCurrent + (current - previousCurrent) * partialTicks;
	}
}
