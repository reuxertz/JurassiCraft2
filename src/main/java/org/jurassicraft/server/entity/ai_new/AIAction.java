package org.jurassicraft.server.entity.ai_new;

import org.jurassicraft.server.entity.ai_new.behaviours.AIBehaviourBase;
import org.jurassicraft.server.entity.ai_new.memories.MemoryBase;

public class AIAction {

    public MemoryBase memory;
    public AIBehaviourBase behaviour;

    protected boolean started;
    protected boolean finished;

    public boolean isStarted()
    {
        return started;
    }
    public boolean isFinished()
    {
        return finished;
    }

    public AIAction(MemoryBase memory, AIBehaviourBase behaviour)
    {
        this.memory = memory;
        this.behaviour = behaviour;
    }
}
