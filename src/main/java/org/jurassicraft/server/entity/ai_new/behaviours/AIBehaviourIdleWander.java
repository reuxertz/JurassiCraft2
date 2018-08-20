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
        return 1.0;
    }

    @Override
    public AIAction.ActionState start(AIAction action)
    {
        Vec3d position = RandomPositionGenerator.getLandPos(action.aiController.entity, 1, 1);
        action.aiController.instinctMove.setMove(position);
        action.setStarted(true);

        return AIAction.ActionState.Continue;
    }

    @Override
    public AIAction.ActionState update(AIAction action)
    {
        boolean isAtPosition = action.aiController.instinctMove.isAtPosition();
        boolean noPath = action.aiController.entity.getNavigator().noPath();

        if (isAtPosition || noPath)
            action.setFinished(true);

        return AIAction.ActionState.Continue;
    }

    @Override
    public AIAction.ActionState finish(AIAction action)
    {
        return AIAction.ActionState.Reset;
    }
}
