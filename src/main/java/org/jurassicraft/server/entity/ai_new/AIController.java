package org.jurassicraft.server.entity.ai_new;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.server.entity.ai_new.behaviours.AIBehaviourBase;
import org.jurassicraft.server.entity.ai_new.instincts.AIInstinctMove;
import org.jurassicraft.server.entity.ai_new.instincts.AIInstinctObserve;
import org.jurassicraft.server.entity.ai_new.memories.MemoryBase;

import java.util.*;

public class AIController extends EntityAIBase {

    protected long age = 0;
    protected int shortTermMemorySize = 10;
    protected List<AIBehaviourBase> behaviours = new ArrayList<>();
    protected Map<String, MemoryBase> shortTermMemoryList = new HashMap<>();

    protected AIAction action;

    public EntityCreature entity;
    public AIInstinctMove instinctMove;
    public AIInstinctObserve instinctObserve;

    public void addMemory(MemoryBase memory)
    {
        shortTermMemoryList.put(memory.getUniqueKey(), memory);
    }
    public void addBehaviour(AIBehaviourBase behaviour)
    {
        behaviours.add(behaviour);
    }

    public void decayMemories()
    {
        //Remove faulty memories, and decay
        List<String> keys = new ArrayList<>(shortTermMemoryList.keySet());
        for (int i = 0; i < keys.size(); i++) {

            String key = keys.get(i);
            MemoryBase memory = shortTermMemoryList.get(key);

            if (memory.getValue() <= 0)
            {
                shortTermMemoryList.remove(key);
                keys.remove(i);
                continue;
            }

            memory.decay();
        }

        //Remove decayed memories
        keys = new ArrayList<>(shortTermMemoryList.keySet());
        while (shortTermMemoryList.size() > shortTermMemorySize) {
            double lowestValue = Double.MAX_VALUE;
            String lowestKey = "";
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                if (shortTermMemoryList.containsKey(key) && shortTermMemoryList.get(key).getValue() < lowestValue) {
                    lowestValue = shortTermMemoryList.get(key).getValue();
                    lowestKey = key;
                }
            }
            shortTermMemoryList.remove(lowestKey);
        }
    }

    public AIController(EntityCreature entity)
    {
        this.entity = entity;

        this.entity.tasks.addTask(0, this);
        this.entity.tasks.addTask(1, this.instinctObserve = new AIInstinctObserve(this));
        this.entity.tasks.addTask(2, this.instinctMove = new AIInstinctMove(this, 0.8F));
    }

    @Override
    public void updateTask()
    {
        age++;
        decayMemories();

        if (action != null)
        {
            AIAction.ActionState actionResult = action.update();

            if (actionResult == AIAction.ActionState.Reset)
                resetTask();

            return;
        }

        List<AIAction> potentialActions = new ArrayList<>();
        List<Double> potentialActionValues = new ArrayList<>();
        List<String> keys = new ArrayList<>(shortTermMemoryList.keySet());
        for (int behaviourIndex = 0; behaviourIndex < behaviours.size(); behaviourIndex++) {
            for (int keyIndex = 0; keyIndex < keys.size(); keyIndex++) {
                MemoryBase memory = shortTermMemoryList.get(keys.get(keyIndex));
                AIBehaviourBase behaviour = behaviours.get(behaviourIndex);

                AIAction action = new AIAction(this, memory, behaviour);

                if (action.getValue() <= 0)
                    continue;

                potentialActions.add(action);
                potentialActionValues.add(action.getValue());
            }
        }

//        if (potentialActions.size() > 0)
//            action = potentialActions.get(0);

        if (potentialActions.size() == 0)
            return;

        double highestValue = Double.MIN_VALUE;
        int highestIndex = -1;
        for (int i = 0; i < potentialActionValues.size(); i++)
        {
            double value = potentialActionValues.get(i);
            if (value > highestValue)
            {
                highestValue = value;
                highestIndex = i;
                continue;
            }
        }

        AIAction action = potentialActions.get(highestIndex);
        this.action = action;

        return;
    }

    @Override
    public boolean shouldExecute() {
        return true;
    }

    @Override
    public void resetTask()
    {
        action = null;
        instinctMove.resetTask();
        instinctObserve.resetTask();
    }
}
