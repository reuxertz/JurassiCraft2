package org.jurassicraft.server.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.List;

import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.message.OpenFieldGuideGuiMessage;
import org.jurassicraft.server.tab.TabHandler;

public class FieldGuideItem extends Item {
    public FieldGuideItem() {
        super();
        this.setCreativeTab(TabHandler.ITEMS);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if (target instanceof DinosaurEntity) {
            if (!player.world.isRemote) {
                JurassiCraft.NETWORK_WRAPPER.sendTo(new OpenFieldGuideGuiMessage((DinosaurEntity) target), (EntityPlayerMP) player);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    	tooltip.add("Use Me on a Dinosaur!");
    	super.addInformation(stack, playerIn, tooltip, advanced);
    }
    
}
