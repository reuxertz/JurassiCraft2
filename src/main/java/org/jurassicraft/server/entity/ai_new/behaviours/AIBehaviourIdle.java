package org.jurassicraft.server.entity.ai_new.behaviours;

import org.jurassicraft.server.entity.ai_new.AIController;

public class AIBehaviourIdle extends AIBehaviourBase {

    public boolean startExecution(AIController controller)
    {
        //Random Search
//        if (!controller.getInstinctMove().getMove() && RandomHelper.RND.nextInt(10) == 0) {

//            controller.getInstinctMove().setMove(true);
//            Vec3d newPos = MovementHelper.getRandomPosition(controller.getEntity(), 25, 10, true);
//            controller.getInstinctMove().setNextPosition(newPos);
//        }

        return false;
    }

    public boolean isFinished(AIController controller)
    {
        return false;//!controller.getInstinctMove().getMove();
    }
}
