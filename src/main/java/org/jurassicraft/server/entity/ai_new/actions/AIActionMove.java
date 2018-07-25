package org.jurassicraft.server.entity.ai_new.actions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import org.jurassicraft.server.entity.ai_new.helpers.MovementHelper;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.Vec3d;
import org.jurassicraft.server.entity.ai.Mutex;
import org.jurassicraft.server.util.RandomHelper;

public class AIActionMove extends EntityAIBase {

    protected EntityLiving entity;

    protected boolean move;
    protected Double x;
    protected Double y;
    protected Double z;
    protected Entity target;
    protected double speed;

    public boolean setNextPosition(Vec3d position)
    {
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
        this.entity = null;

        entity.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, this.speed);
        if (entity.getNavigator().noPath())
            return false;

        return move = true;
    }

    public boolean setNextPosition(Entity target)
    {
        this.x = null;
        this.y = null;
        this.z = null;
        this.target = target;

        entity.getNavigator().tryMoveToEntityLiving(this.target, .8);
        if (entity.getNavigator().noPath())
            return false;

        return move = true;
    }

    public AIActionMove(EntityLiving creatureIn, double speedIn)
    {
        this.entity = creatureIn;
        this.speed = speedIn;
        this.setMutexBits(Mutex.MOVEMENT);
    }

    @Override
    public boolean shouldExecute()
    {
        if (move && !MovementHelper.isAtPosition(entity.posX, entity.posY, entity.posZ, x, y, z, .1))
            return true;

        //Escape Water
        if (!move && MovementHelper.isBelowWaterLine(entity)) {
            Vec3d newPos = MovementHelper.getRandomPosition(this.entity, 25, 25, true);
            if (setNextPosition(newPos))
                return move = true;

            return false;
        }

        //Random Search (Temporary)
        if (!move && RandomHelper.RND.nextInt(10) == 0) {

            Vec3d newPos = MovementHelper.getRandomPosition(this.entity, 25, 10, true);
            if (setNextPosition(newPos))
                return move = true;

            return false;
        }

        return false;
    }

    protected Vec3d getWanderPosition() {
        return MovementHelper.getRandomPosition(this.entity, 10, 10, true);
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        if (move && entity.getNavigator().noPath())
            return move = false;

        if (MovementHelper.isAtPosition(entity.posX, entity.posY, entity.posZ, x, y, z, .1))
            return move = false;
        else
        {
            entity.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, this.speed);
            return true;
        }
    }

    @Override
    public void startExecuting()
    {

    }

    @Override
    public void resetTask()
    {
        this.x = null;
        this.y = null;
        this.z = null;
        this.target = null;
        this.move = false;
    }
}
