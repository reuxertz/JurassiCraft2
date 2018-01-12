package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import org.jurassicraft.server.entity.DinosaurEntity;

public class DinosaurAttackMeleeEntityAI extends EntityAIAttackMelee {
    protected DinosaurEntity dinosaur;

    public DinosaurAttackMeleeEntityAI(DinosaurEntity dinosaur, double speed, boolean useLongMemory) {
        super(dinosaur, speed, useLongMemory);
        this.dinosaur = dinosaur;
        this.setMutexBits(Mutex.ATTACK);
    }

    @Override
    public void updateTask() {
        EntityLivingBase target = this.attacker.getAttackTarget();

        if (target != null) {
            super.updateTask();
        }

        if (target == null || target.isDead || (target instanceof DinosaurEntity && ((DinosaurEntity) target).isCarcass())) {
            super.resetTask();
            this.attacker.setAttackTarget(null);
        }
    }

    @Override
    protected double getAttackReachSqr(EntityLivingBase attackTarget) {
        if (attackTarget.getEntityBoundingBox().intersects(this.dinosaur.getEntityBoundingBox().expand(1.3, 1.3, 1.3))) {
            return 1024.0;
        }
        double grownWidth = this.attacker.width + 1.0;
        return grownWidth * grownWidth;
    }
}
