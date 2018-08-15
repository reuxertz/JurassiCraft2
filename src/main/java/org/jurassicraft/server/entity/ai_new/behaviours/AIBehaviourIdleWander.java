package org.jurassicraft.server.entity.ai_new.behaviours;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;
import org.jurassicraft.server.entity.ai_new.AIAction;
import org.jurassicraft.server.entity.ai_new.AIController;
import org.jurassicraft.server.entity.ai_new.helpers.MovementHelper;
import org.jurassicraft.server.entity.ai_new.instincts.AIInstinctMove;

public class AIBehaviourIdleWander extends AIBehaviourBase {

    @Override
    public double getValue(AIAction action)
    {
        return 0.0;
    }

    @Override
    public AIAction.ActionState start(AIAction action)
    {
        Vec3d position = RandomPositionGenerator.getLandPos(action.aiController.getEntity(), 5, 5);
        action.aiController.getInstinctMove().setMove(position);
        action.setStarted(true);

        return AIAction.ActionState.Continue;
    }

    @Override
    public AIAction.ActionState update(AIAction action)
    {
        if (action.aiController.getInstinctMove().isAtPosition())
            action.setFinished(true);

        return AIAction.ActionState.Continue;
    }

    @Override
    public AIAction.ActionState finish(AIAction action)
    {
        return AIAction.ActionState.Continue;
    }
}
