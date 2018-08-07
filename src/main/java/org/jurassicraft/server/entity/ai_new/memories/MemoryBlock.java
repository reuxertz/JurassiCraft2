package org.jurassicraft.server.entity.ai_new.memories;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MemoryBlock extends MemoryBase {

    public BlockPos blockPos;
    public IBlockState blockState;

    public MemoryBlock(MemoryBlock memoryBlock)
    {
        super(memoryBlock.value);
        blockPos = new BlockPos(memoryBlock.blockPos.getX(), memoryBlock.blockPos.getY(), memoryBlock.blockPos.getZ());
        blockState = memoryBlock.blockState;
    }

    public MemoryBlock(World world, BlockPos blockPos)
    {
        super(1);
        this.blockPos = blockPos;
        this.blockState = world.getBlockState(blockPos);
        this.uniqueKey = MemoryBase.createUniqueKeyBlock(world, blockPos);
    }
}
