package org.jurassicraft.server.entity.ai_new.behaviours;

import org.jurassicraft.server.entity.ai_new.AIAction;
import org.jurassicraft.server.entity.ai_new.memories.MemoryBase;

public class AIBehaviourBase {

    public AIAction constructAction(MemoryBase memoryBase)
    {
        AIAction result = new AIAction(memoryBase, this);
        return result;
    }
}
