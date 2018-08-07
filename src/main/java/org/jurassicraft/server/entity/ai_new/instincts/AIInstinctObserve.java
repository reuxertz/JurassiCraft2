package org.jurassicraft.server.entity.ai_new.instincts;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jurassicraft.server.entity.ai.Mutex;
import org.jurassicraft.server.entity.ai_new.AIController;
import org.jurassicraft.server.entity.ai_new.helpers.MovementHelper;
import org.jurassicraft.server.entity.ai_new.memories.MemoryBlock;
import org.jurassicraft.server.entity.ai_new.memories.MemoryEntity;

import java.util.List;

public class AIInstinctObserve extends AIInstinctBase {

    protected double observationRange = 10;

    public double observationRange() { return observationRange; }

    public AIInstinctObserve(AIController aiController)
    {
        super(aiController);
        this.setMutexBits(Mutex.ATTACK);
    }

    @Override
    public boolean shouldExecute()
    {
        return true;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return true;
    }

    @Override
    public void updateTask()
    {
        Vec3d pos = MovementHelper.getRandomPosition(this.entity, observationRange, observationRange, true);
        IBlockState blockState = this.entity.world.getBlockState(new BlockPos(pos.x, pos.y, pos.z));
        MemoryBlock memoryBlock = new MemoryBlock(this.entity.world, new BlockPos((int)pos.x, (int)pos.y, (int)pos.z));
        aiController.addMemory(memoryBlock);

        double x = entity.posX;
        double y = entity.posY;
        double z = entity. posZ;

        double x1 = x + observationRange;
        double y1 = y + observationRange;
        double z1 = z + observationRange;

        double x2 = x - observationRange;
        double y2 = y - observationRange;
        double z2 = z - observationRange;

        List<Entity> list = entity.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(x1, y1, z1, x2, y2, z2));

        for (int i = 0; i < list.size(); i++)
        {
            Entity e = list.get(0);

            if (e == entity)
                continue;

            MemoryEntity memoryEntity = new MemoryEntity(e);

            aiController.addMemory(memoryEntity);
        }

        return;
    }
}
