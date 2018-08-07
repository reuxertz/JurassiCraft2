package org.jurassicraft.server.entity.ai_new.instincts;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.server.entity.ai_new.AIController;

public abstract class AIInstinctBase extends EntityAIBase {

    protected AIController aiController;
    protected EntityCreature entity;

    public AIInstinctBase(AIController aiController)
    {
        this.aiController = aiController;
        this.entity = aiController.getEntity();
    }
}
