package org.jurassicraft.server.entity.ai_new.behaviours;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.ai_new.AIAction;
import org.jurassicraft.server.entity.ai_new.helpers.MovementHelper;
import org.jurassicraft.server.entity.ai_new.memories.MemoryBlock;

public class AIBehaviourGraze extends AIBehaviourBase {

    public boolean isGrazeable(World world, BlockPos blockPos)
    {
        Block block = world.getBlockState(blockPos).getBlock();

        if (block == Blocks.GRASS)
            return true;

//        Block blockDown = world.getBlockState(blockPos.down()).getBlock();
//
//        if (blockDown == Blocks.GRASS)
//            return true;

        return false;
    }



    @Override
    public double getValue(AIAction action)
    {
        if (!(action.memory instanceof MemoryBlock))
            return -1.0;

        BlockPos blockPos = ((MemoryBlock)action.memory).blockPos;

        if (isGrazeable(action.aiController.entity.world, blockPos))
            return 2.0;

        return -1.0;
    }

    @Override
    public AIAction.ActionState start(AIAction action)
    {
        BlockPos blockPos = ((MemoryBlock)action.memory).blockPos;

        if (!isGrazeable(action.aiController.entity.world, blockPos))
            return AIAction.ActionState.Reset;

        Vec3d position = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());


        action.aiController.instinctMove.setMove(position);

        action.setStarted(true);

        return AIAction.ActionState.Continue;
    }

    @Override
    public AIAction.ActionState update(AIAction action)
    {
        boolean isAtPosition = action.aiController.instinctMove.isAtPosition();
        boolean noPath = action.aiController.entity.getNavigator().noPath();

        if (isAtPosition) {

            BlockPos blockPos = ((MemoryBlock)action.memory).blockPos;
            IBlockState blockstate = action.aiController.entity.world.getBlockState(blockPos);
            action.aiController.entity.world.setBlockState(blockPos, Blocks.DIRT.getDefaultState(), 3);
        }

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
