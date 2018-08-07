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

    protected EntityCreature entity;
    protected long age = 0;
    protected int shortTermMemorySize = 10;
    protected List<AIBehaviourBase> behaviours = new ArrayList<>();
    protected Map<String, MemoryBase> shortTermMemoryList = new HashMap<>();

    protected AIInstinctMove instinctMove;
    protected AIInstinctObserve instinctObserve;

    public AIInstinctMove getInstinctMove() { return instinctMove; }
    public AIInstinctObserve getInstinctObserve() { return instinctObserve; }
    public EntityCreature getEntity() { return entity; }


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
        List<String> keys = new ArrayList<>(shortTermMemoryList.keySet());
        for (int i = 0; i < keys.size(); i++)
            shortTermMemoryList.get(keys.get(i)).decay();

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
        this.entity.tasks.addTask(1, new AIInstinctObserve(this));
        this.entity.tasks.addTask(2, new AIInstinctMove(this, 0.8F));
    }

    @Override
    public void updateTask()
    {
        age++;

        decayMemories();

//        if (action != null && !action.isFinished())
//            return;
//
//        if (action != null && action.isFinished())
//            action = null;

//        if (action != null && !action.isStarted())
//            action.behaviour.startExecution(this);
//
//        if (action != null && action.isStarted() && !action.isFinished())
//            action.behaviour.(this);


        List<AIAction> potentialActions = new ArrayList<>();
        for (int behaviourIndex = 0; behaviourIndex < behaviours.size(); behaviourIndex++) {
            List<String> keys = new ArrayList<>(shortTermMemoryList.keySet());
            for (int keyIndex = 0; keyIndex < keys.size(); keyIndex++) {
                MemoryBase memory = shortTermMemoryList.get(keys.get(keyIndex));
                AIBehaviourBase behaviour = behaviours.get(behaviourIndex);

                AIAction action = behaviour.constructAction(memory);

                if (action == null)
                    continue;

                potentialActions.add(action);
            }
        }

//        if (potentialActions.size() > 0)
//            setAction(potentialActions.get(0));

        return;
    }

    @Override
    public boolean shouldExecute() {
        return true;
    }
}
