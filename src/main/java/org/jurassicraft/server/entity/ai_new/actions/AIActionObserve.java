package org.jurassicraft.server.entity.ai_new.actions;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.server.entity.DinosaurEntity;

public class AIActionObserve extends EntityAIBase {

    protected EntityLiving entity;

    public AIActionObserve(EntityLiving creatureIn)
    {

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
        return;
    }
}
