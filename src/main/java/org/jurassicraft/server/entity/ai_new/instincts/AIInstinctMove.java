package org.jurassicraft.server.entity.ai_new.instincts;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.ai.Mutex;
import org.jurassicraft.server.entity.ai_new.AIController;

public class AIInstinctMove extends AIInstinctBase {

    private double speed;

    protected boolean randomMove;
    protected Vec3d position;

    public void setRandomMove(boolean random)
    {
        this.randomMove = random;
    }
    public void setMove(Vec3d position)
    {
        this.position = position;
    }

    public AIInstinctMove(AIController aiController, double speedIn)
    {
        super(aiController);
        this.speed = speedIn;
        this.setMutexBits(Mutex.MOVEMENT);
    }

    @Override
    public boolean shouldExecute()
    {
        if (this.entity.getNavigator().noPath())
        {
            //TODO change me from not random to random
            if (!randomMove) {
                Vec3d vec = getWanderPosition();

                if (vec == null)
                    return false;

                this.randomMove = false;
                this.position = new Vec3d(vec.x, vec.y, vec.z);
                return true;
            }
        }

        return false;
    }

    protected Vec3d getWanderPosition() {
        return RandomPositionGenerator.getLandPos(this.entity, 5, 5);
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return !this.entity.getNavigator().noPath() && !this.entity.isInWater();
    }

    @Override
    public void startExecuting()
    {
        this.entity.getNavigator().tryMoveToXYZ(this.position.x, this.position.y, this.position.z, this.speed);
    }
}
