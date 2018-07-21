package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import org.jurassicraft.server.conf.JurassiCraftConfig;
import org.jurassicraft.server.entity.dinosaur.DinosaurEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class SelectTargetEntityAI extends EntityAIBase {
    private DinosaurEntity entity;
    private Predicate<Entity> attackPredicate;
    private EntityLivingBase targetEntity;

    public SelectTargetEntityAI(DinosaurEntity entity, Predicate<Entity> attackPredicate) {
        this.entity = entity;
        this.attackPredicate = attackPredicate;
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.entity.resetAttackCooldown();
    }

    @Override
    public void startExecuting() {
        this.entity.setAttackTarget(this.targetEntity);

        Herd herd = this.entity.herd;

        if (herd != null && this.targetEntity != null) {
            List<EntityLivingBase> enemies = new LinkedList<>();

            if (this.targetEntity instanceof DinosaurEntity && ((DinosaurEntity) this.targetEntity).herd != null) {
                enemies.addAll(((DinosaurEntity) this.targetEntity).herd.members);
            } else {
                enemies.add(this.targetEntity);
            }

            for (EntityLivingBase enemy : enemies) {
                if (!herd.enemies.contains(enemy)) {
                    herd.enemies.add(enemy);
                }
            }
        }
    }

    @Override
    public boolean shouldExecute() {
        if (this.entity.getRNG().nextInt(10) != 0) {
            return false;
        }

        if (!this.entity.getMetabolism().isHungry() && JurassiCraftConfig.ENTITIES.huntWhenHungry) {
            return false;
        }

        Entity target = this.entity.getAttackTarget();
        if (target != null && !target.isDead && this.entity.getEntitySenses().canSee(target) ) {
            return false;
        }

        if (!(this.entity.herd != null && this.entity.herd.fleeing) && this.entity.getAgePercentage() > 50 && (this.entity.getOwner() == null || this.entity.getMetabolism().isStarving()) && !this.entity.isSleeping() && this.entity.getAttackCooldown() <= 0) {
            List<EntityLivingBase> entities = this.entity.world.getEntitiesWithinAABB(EntityLivingBase.class, this.entity.getEntityBoundingBox().expand(16, 16, 16));

            if (entities.size() > 0) {
                this.targetEntity = null;
                double bestScore = Double.MAX_VALUE;

                for (EntityLivingBase entity : entities) {
                    if (!entity.isInLava() && this.entity.getEntitySenses().canSee(entity) && this.canAttack(entity)) {
                        double score = entity.getHealth() <= 0.0F ? (this.entity.getDistanceSq(entity) / entity.getHealth()) : 0;

                        if (entity.isInWater()) {
                            score *= 3;
                        }

                        if (score < bestScore) {
                            bestScore = score;

                            if (entity.getRidingEntity() instanceof EntityLivingBase) {
                                this.targetEntity = (EntityLivingBase) entity.getRidingEntity();
                            } else {
                                this.targetEntity = entity;
                            }
                        }
                    }
                }

                return this.targetEntity != null;
            }
        }

        return false;
    }

    private boolean canAttack(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode) {
            return false;
        }
        return this.attackPredicate.test(entity);
    }
}
