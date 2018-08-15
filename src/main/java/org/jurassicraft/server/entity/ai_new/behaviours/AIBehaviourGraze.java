package org.jurassicraft.server.entity.ai_new.behaviours;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jurassicraft.server.entity.ai_new.AIAction;
import org.jurassicraft.server.entity.ai_new.memories.MemoryBlock;

public class AIBehaviourGraze extends AIBehaviourBase {

    public boolean isGrazeable(Block block)
    {
        if (block == Blocks.GRASS)
        {
            return true;
        }

        return false;
    }



    @Override
    public double getValue(AIAction action)
    {
        if (!(action.memory instanceof MemoryBlock))
            return -1.0;

        BlockPos blockPos = ((MemoryBlock)action.memory).blockPos;
        Block block = action.aiController.getEntity().world.getBlockState(blockPos).getBlock();

        if (!(action.memory instanceof MemoryBlock))
            return -1.0;

        if (isGrazeable(block))
            return 1.0;

        return 0;
    }

    @Override
    public AIAction.ActionState start(AIAction action)
    {
        BlockPos blockPos = ((MemoryBlock)action.memory).blockPos;
        Block block = action.aiController.getEntity().world.getBlockState(blockPos).getBlock();

        if (!isGrazeable(block))
            return AIAction.ActionState.Reset;

        Vec3d position = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
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
