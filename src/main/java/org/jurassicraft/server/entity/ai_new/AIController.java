package org.jurassicraft.server.entity.ai_new;

import net.minecraft.entity.EntityLiving;
import org.jurassicraft.server.entity.ai_new.actions.AIActionMove;
import org.jurassicraft.server.entity.ai_new.actions.AIActionObserve;

public class AIController {

    public EntityLiving entity;

    public AIActionMove actionMove;
    public AIActionObserve actionObserve;

    public AIController(EntityLiving entity)
    {
        this.entity = entity;

        //this.actionMove = new AIActionMove(entity, .8);
        this.actionObserve = new AIActionObserve(entity);

        //entity.tasks.addTask(0, actionMove);
        entity.tasks.addTask(0, actionObserve);
    }
}
