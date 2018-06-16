package org.jurassicraft.server.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.util.LangHelper;

public class CultivateItemBlock extends ItemBlock {
    private final EnumDyeColor color;
    public CultivateItemBlock(Block block, EnumDyeColor color) {
        super(block);
        this.color = color;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return new LangHelper("tile.cultivate.name").withProperty("color", "color." + this.color.getName() + ".name").build();
    }
}
