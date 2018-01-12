package org.jurassicraft.server.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.tab.TabHandler;

public class GrowthSerumItem extends Item {
    public GrowthSerumItem() {
        super();
        this.setCreativeTab(TabHandler.ITEMS);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if (target instanceof DinosaurEntity) {
            DinosaurEntity dinosaur = (DinosaurEntity) target;

            if (!dinosaur.isCarcass()) {
                dinosaur.increaseGrowthSpeed();

                stack.shrink(1);

                if (!player.capabilities.isCreativeMode) {
                    player.inventory.addItemStackToInventory(new ItemStack(ItemHandler.EMPTY_SYRINGE));
                }

                return true;
            }
        }

        return false;
    }
}
