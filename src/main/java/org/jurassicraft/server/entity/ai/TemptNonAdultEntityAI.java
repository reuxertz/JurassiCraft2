package org.jurassicraft.server.entity.ai;

import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.food.FoodHelper;
import org.jurassicraft.server.food.FoodType;

import net.minecraft.entity.ai.EntityAITempt;

public class TemptNonAdultEntityAI extends EntityAITempt {
    private DinosaurEntity dinosaur;

    public TemptNonAdultEntityAI(DinosaurEntity dinosaur, double speed) {
        super(dinosaur, speed, !dinosaur.getDinosaur().getDiet().canEat(dinosaur, FoodType.MEAT), FoodHelper.getEdibleFoodItems(dinosaur, dinosaur.getDinosaur().getDiet()));
        this.dinosaur = dinosaur;
        this.setMutexBits(Mutex.METABOLISM | Mutex.MOVEMENT);
    }

    @Override
    public boolean shouldExecute() {
        return super.shouldExecute() && !this.dinosaur.isBusy() && this.dinosaur.getAgePercentage() < 50;
    }
}
