package org.jurassicraft.server.item.vehicles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.vehicle.HelicopterBaseEntity;
import org.jurassicraft.server.tab.TabHandler;

import java.util.List;
import java.util.UUID;

public class HelicopterItem extends Item {
    public HelicopterItem() {
        this.setCreativeTab(TabHandler.ITEMS);
        this.setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        tooltip.add("Right click on a block to spawn the helicopter");
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            HelicopterBaseEntity helicopter = new HelicopterBaseEntity(world, UUID.randomUUID());
            helicopter.setPosition(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
            world.spawnEntity(helicopter);
        }

        return EnumActionResult.SUCCESS;
    }
}
