package org.jurassicraft.server.item;

import org.jurassicraft.server.entity.TranquilizerDartEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class DartGun extends Item {
    
    private static final int MAX_CARRY_SIZE = 12; //TODO config ?
    
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!playerIn.capabilities.isCreativeMode) {
            itemstack.shrink(1);
        }

        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        ItemStack dartItem = getDartItem(itemstack);
        if(dartItem.isEmpty()) {
            setDartItem(itemstack, 
        	    playerIn.inventoryContainer.inventorySlots.stream()
        	    	.map(Slot::getStack)
                	.filter(stack -> stack.getItem() instanceof Dart)
                	.findFirst()
                	.orElse(ItemStack.EMPTY));
        } else if (!worldIn.isRemote) {
            TranquilizerDartEntity dart = new TranquilizerDartEntity(worldIn, playerIn, dartItem);
            dart.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 2.5F, 0.5F);
            worldIn.spawnEntity(dart);
            dartItem.shrink(1);
            setDartItem(itemstack, dartItem);
        }

        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }
    
    public static ItemStack getDartItem(ItemStack dartGun) {
	NBTTagCompound nbt = dartGun.getOrCreateSubCompound("dart_gun");
	ItemStack stack = new ItemStack(nbt.getCompoundTag("itemstack"));
	stack.setCount(Math.min(stack.getCount(), MAX_CARRY_SIZE));
	return stack;
    }
    
    public static void setDartItem(ItemStack dartGun, ItemStack dartItem) {
	ItemStack dartItem2 = dartItem.splitStack(MAX_CARRY_SIZE);
	dartGun.getOrCreateSubCompound("dart_gun").setTag("itemstack", dartItem2.serializeNBT());
    }
}
