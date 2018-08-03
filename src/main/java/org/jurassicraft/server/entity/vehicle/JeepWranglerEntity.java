package org.jurassicraft.server.entity.vehicle;

import org.jurassicraft.server.item.ItemHandler;

import net.minecraft.world.World;

public class JeepWranglerEntity extends CarEntity {
    public JeepWranglerEntity(World world) {
        super(world);
        this.speedModifier = 0f;
    }

    @Override
    public void dropItems() {
        this.dropItem(ItemHandler.JEEP_WRANGLER, 1);
    }

    @Override
    protected Seat[] createSeats() {
        Seat frontLeft = new Seat( 0.563F, 0.45F, 0.0F, 0.5F, 0.25F);
        Seat frontRight = new Seat( -0.563F, 0.45F, 0.0F, 0.5F, 0.25F);
        Seat backLeft = new Seat( 0.5F, 0.7F, -2.2F, 0.4F, 0.25F);
        Seat backRight = new Seat( -0.5F, 0.7F, -2.2F, 0.4F, 0.25F);
        return new Seat[] { frontLeft, frontRight, backLeft, backRight };
    }
    
    @Override
    protected WheelData createWheels() {
	return new WheelData(1.2, 1.5, -1.2, -2.65);
    }
}
