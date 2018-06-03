package org.jurassicraft.server.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class PotionDart extends Dart {
    public PotionDart() {
        super((entity, stack) -> PotionUtils.getEffectsFromStack(stack).forEach(entity::addPotionEffect), -1);
    }

    @Override
    public int getDartColor(ItemStack stack) {
        return PotionUtils.getColor(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer worldIn, List<String> tooltip, boolean advanced) {
        PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
    }


}
