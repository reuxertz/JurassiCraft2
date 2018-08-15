package org.jurassicraft.server.entity.ai_new.behaviours;

import org.jurassicraft.server.entity.ai_new.AIAction;
import org.jurassicraft.server.entity.ai_new.AIController;
import org.jurassicraft.server.entity.ai_new.memories.MemoryBase;

public abstract class AIBehaviourBase {

    public AIAction constructAction(AIController aiController, MemoryBase memoryBase)
    {
        AIAction result = new AIAction(aiController, memoryBase, this);
        return result;
    }

    public abstract double getValue(AIAction action);

    public abstract AIAction.ActionState start(AIAction action);
    public abstract AIAction.ActionState update(AIAction action);
    public abstract AIAction.ActionState finish(AIAction action);
}
