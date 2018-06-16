package org.jurassicraft.server.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.block.NestFossilBlock;
import org.jurassicraft.server.util.LangHelper;

public class NestFossilItemBlock extends ItemBlock {
    private final boolean encased;
    private final NestFossilBlock.Variant variant;

    public NestFossilItemBlock(Block block, boolean encased, NestFossilBlock.Variant variant) {
        super(block);
        this.variant = variant;
        this.setMaxDamage(0);
        this.encased = encased;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return new LangHelper(this.encased ? "tile.encased_nest_fossil.name" : "tile.nest_fossil.name").build();
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + this.variant.getName();
    }
}
