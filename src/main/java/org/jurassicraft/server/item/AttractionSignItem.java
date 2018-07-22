package org.jurassicraft.server.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.item.AttractionSignEntity;
import org.jurassicraft.server.util.LangHelper;

import java.util.Locale;

public class AttractionSignItem extends Item {

    private final AttractionSignEntity.AttractionSignType type;

    AttractionSignItem(AttractionSignEntity.AttractionSignType type) {
        this.type = type;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return new LangHelper("item.attraction_sign.name").withProperty("type", "attraction_sign." + (this.type.name().toLowerCase(Locale.ENGLISH)) + ".name").build();
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    	ItemStack stack = player.getHeldItem(hand);
        if (side != EnumFacing.DOWN && side != EnumFacing.UP) {
            BlockPos offset = pos.offset(side);

            if (player.canPlayerEdit(offset, side, stack)) {
                AttractionSignEntity sign = new AttractionSignEntity(world, offset, side, this.type);

                if (sign.onValidSurface()) {
                    if (!world.isRemote) {
                        world.spawnEntity(sign);
                    }

                    stack.shrink(1);

                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return EnumActionResult.PASS;
    }
}
