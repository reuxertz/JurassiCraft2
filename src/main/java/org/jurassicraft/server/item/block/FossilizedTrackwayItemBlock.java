package org.jurassicraft.server.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.block.FossilizedTrackwayBlock;
import org.jurassicraft.server.util.LangHelper;

public class FossilizedTrackwayItemBlock extends ItemBlock {

    private final FossilizedTrackwayBlock.TrackwayType type;

    public FossilizedTrackwayItemBlock(Block block, FossilizedTrackwayBlock.TrackwayType trackwayType) {
        super(block);
        this.type = trackwayType;
        this.setMaxDamage(0);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return new LangHelper("tile.fossilized_trackway.name").withProperty("variant", "trackway." + type.getName() + ".name").build();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + type.getName();
    }
}
