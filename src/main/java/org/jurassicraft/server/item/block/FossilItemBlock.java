package org.jurassicraft.server.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.block.FossilBlock;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.util.LangHelper;

import java.util.Locale;

public class FossilItemBlock extends ItemBlock {
    public FossilItemBlock(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Dinosaur dinosaur = ((FossilBlock) this.block).getValue(stack);
        return new LangHelper("tile.fossil_block.name").withProperty("dinosaur", dinosaur.getTranslationKey()).build();
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        Dinosaur dinosaur = ((FossilBlock) this.block).getValue(stack);
        return super.getUnlocalizedName() + "." + dinosaur.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
    }
}
