package org.jurassicraft.server.entity.ai_new;

import org.jurassicraft.server.entity.ai_new.behaviours.AIBehaviourBase;
import org.jurassicraft.server.entity.ai_new.memories.MemoryBase;

import javax.swing.*;

public class AIAction {

    public enum ActionState { Continue, Reset }

    public AIController aiController;
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

    public double getValue()
    {
        return behaviour.getValue(this);
    }

    public void setStarted(boolean value)
    {
        started = value;
        System.out.println("action started: " + behaviour.getClass().toString());
    }
    public void setFinished(boolean value)
    {
        finished = value;
        System.out.println("action finished: " + behaviour.getClass().toString());
    }

    public AIAction(AIController aiController, MemoryBase memory, AIBehaviourBase behaviour)
    {
        this.aiController = aiController;
        this.memory = memory;
        this.behaviour = behaviour;
    }

    public ActionState update()
    {
        ActionState result = _update();



        return result;
    }
    
    public ActionState _update()
    {
        if (!this.isStarted()) {
            AIAction.ActionState result = this.behaviour.start(this);

            if (result == ActionState.Reset)
                return result;

            return ActionState.Continue;
        }

        if (this.isStarted() && !this.isFinished()) {
            AIAction.ActionState result = this.behaviour.update(this);

            if (result == ActionState.Reset)
                return result;


            return ActionState.Continue;
        }

        if (this.isStarted() && this.isFinished()) {
            AIAction.ActionState result = this.behaviour.finish(this);

            if (result == ActionState.Reset)
                return result;

            return ActionState.Continue;
        }

        return ActionState.Reset;
    }
}
