package org.jurassicraft.server.entity.ai;

import com.google.common.collect.Sets;
import net.minecraft.entity.ai.EntityAITempt;
import org.jurassicraft.server.entity.DinosaurEntity;

public class TemptNonAdultEntityAI extends EntityAITempt {
    private DinosaurEntity dinosaur;

    public TemptNonAdultEntityAI(DinosaurEntity dinosaur, double speed) {
        super(dinosaur, speed, true, Sets.newHashSet());//FoodHelper.getEdibleFoodItems(dinosaur, dinosaur.getDinosaur().getDiet())
        this.dinosaur = dinosaur;
        this.setMutexBits(Mutex.METABOLISM | Mutex.MOVEMENT);
    }

    @Override
    public boolean shouldExecute() {
        return super.shouldExecute() && !this.dinosaur.isBusy() && this.dinosaur.getAgePercentage() < 50;
    }
}
