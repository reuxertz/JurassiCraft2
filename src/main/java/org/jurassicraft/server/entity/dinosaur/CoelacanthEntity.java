package org.jurassicraft.server.entity.dinosaur;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.SwimmingDinosaurEntity;

public class CoelacanthEntity extends SwimmingDinosaurEntity {
    public CoelacanthEntity(World world) {
        super(world);
        this.target(EntitySquid.class);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        return null;
    }
}