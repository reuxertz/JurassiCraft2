package org.jurassicraft.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import org.jurassicraft.server.item.ItemHandler;

import java.util.Random;

public class GypsumStoneBlock extends Block {
    GypsumStoneBlock() {
        super(Material.ROCK);
        this.setHardness(1.5F);
        this.setResistance(1.5F);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ItemHandler.GYPSUM_POWDER;
    }

    @Override
    public int quantityDropped(Random random) {
        return random.nextInt(5) + 4;
    }
}
