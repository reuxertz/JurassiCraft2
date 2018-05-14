package org.jurassicraft.client.sound;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.entity.vehicle.CarEntity;

@SideOnly(Side.CLIENT)
public class CarSound extends MovingSound {
    private CarEntity entity;

    public CarSound(CarEntity entity) {
        super(SoundHandler.CAR_MOVE, SoundCategory.BLOCKS);
        this.entity = entity;
    }

    @Override
    public boolean canRepeat() {
        return true;
    }

    @Override
    public float getVolume() {
	return this.entity.getSoundVolume();
    }

    @Override
    public float getPitch() {
        return Math.min(1.0F, this.getVolume()) * 0.5F + 0.5F;
    }

    @Override
    public void update() {
        if (this.entity.isDead) {
            this.donePlaying = true;
        } else {
            this.xPosF = (float) this.entity.posX;
            this.yPosF = (float) this.entity.posY;
            this.zPosF = (float) this.entity.posZ;
        }
    }
}
