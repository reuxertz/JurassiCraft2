package org.jurassicraft.server.entity.ai;

import org.jurassicraft.server.entity.DinosaurEntity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class DinosaurWanderAvoidWater extends DinosaurWanderEntityAI {

    public DinosaurWanderAvoidWater(DinosaurEntity creatureIn, double speedIn) {
	super(creatureIn, speedIn, 1);
    }
    
    @Override
    protected boolean innerShouldExecute() { 
        return this.entity.canDinoSwim() && !this.entity.world.isMaterialInBB(this.entity.getEntityBoundingBox(), Material.WATER);
    }
    
    @Override
    protected boolean outterShouldExecute() {
        return true;
    }
    
    @Override
    protected Vec3d getWanderPosition() {
	Vec3d vec3d = RandomPositionGenerator.getLandPos(this.entity, 32, 7);
        return vec3d == null ? super.getWanderPosition() : vec3d;
    }

}
