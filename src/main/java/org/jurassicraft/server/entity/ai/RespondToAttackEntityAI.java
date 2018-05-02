package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import org.jurassicraft.server.entity.DinosaurEntity;

public class RespondToAttackEntityAI extends EntityAIBase {
    private DinosaurEntity dinosaur;
    private EntityLivingBase attacker;

    public RespondToAttackEntityAI(DinosaurEntity dinosaur) {
        this.dinosaur = dinosaur;
        this.setMutexBits(0);
    }

    @Override
    public boolean shouldExecute() {
        this.attacker = this.dinosaur.getAttackTarget();

        return this.attacker != null && !this.attacker.isDead && !(this.attacker instanceof DinosaurEntity && ((DinosaurEntity) this.attacker).isCarcass()) && !(this.attacker instanceof EntityPlayer && ((EntityPlayer) this.attacker).capabilities.isCreativeMode);
    }

    @Override
    public void startExecuting() {
        this.dinosaur.respondToAttack(this.attacker);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }
}
