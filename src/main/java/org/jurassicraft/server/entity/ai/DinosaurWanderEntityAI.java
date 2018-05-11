package org.jurassicraft.server.entity.ai;

import org.jurassicraft.server.entity.DinosaurEntity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class DinosaurWanderEntityAI extends EntityAIBase {
    private DinosaurEntity entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;
    private int executionChance;
    private boolean mustUpdate;


    public DinosaurWanderEntityAI(DinosaurEntity creatureIn, double speedIn, int chance) {
        this.entity = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setMutexBits(Mutex.MOVEMENT);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.mustUpdate) {
            if (this.entity.getRNG().nextInt(this.executionChance) != 0) {
                return false;
            }
        }

        if (this.entity.getNavigator().noPath() && this.entity.getAttackTarget() == null) {
            overlist:
            for(int i = 0; i < 100; i++) {
        	Vec3d vec = RandomPositionGenerator.getLandPos(this.entity, 10, 10);
        	
                if (vec != null) {
                    for(BlockPos pos : BlockPos.getAllInBox(new BlockPos(vec.addVector(0, 1, 0)), new BlockPos(vec.addVector(1, 1, 1)))) {
            	    	if(this.entity.world.getBlockState(pos).getMaterial() != Material.AIR) {
            	    	    continue overlist;
            	    	}
                    }
                    this.xPosition = vec.x;
                    this.yPosition = vec.y;
                    this.zPosition = vec.z;
                    this.mustUpdate = false;
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.entity.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    public void makeUpdate() {
        this.mustUpdate = true;
    }

    public void setExecutionChance(int chance) {
        this.executionChance = chance;
    }
}