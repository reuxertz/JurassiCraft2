package org.jurassicraft.server.item;

import net.minecraft.util.*;
import org.jurassicraft.server.api.PlantProvider;
import org.jurassicraft.server.block.plant.DoublePlantBlock;
import org.jurassicraft.server.block.plant.JCBlockCropsBase;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.util.LangHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlantCallusItem extends Item implements PlantProvider {
    public PlantCallusItem() {
        super();
        this.setCreativeTab(TabHandler.PLANTS);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        ResourceLocation registryName = this.getValue(stack).getRegistryName();
        return new LangHelper("item.plant_callus.name").withProperty("plant", "plants." + registryName.getResourceDomain() + "." + registryName.getResourcePath() + ".name").build();
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    	ItemStack stack = player.getHeldItem(hand);
        if (side == EnumFacing.UP && player.canPlayerEdit(pos.offset(side), side, stack)) {
            if (world.isAirBlock(pos.offset(side)) && world.getBlockState(pos).getBlock() == Blocks.FARMLAND) {
                Plant plant = this.getValue(stack);

                if (plant != null) {
                    Block block = plant.getBlock();

                    if (!(block instanceof BlockCrops || block instanceof JCBlockCropsBase)) {
                        world.setBlockState(pos, Blocks.DIRT.getDefaultState());
                    }

                    if (block instanceof DoublePlantBlock) {
                        world.setBlockState(pos.up(), block.getDefaultState().withProperty(DoublePlantBlock.HALF, DoublePlantBlock.BlockHalf.LOWER));
                        world.setBlockState(pos.up(2), block.getDefaultState().withProperty(DoublePlantBlock.HALF, DoublePlantBlock.BlockHalf.UPPER));
                    } else {
                        world.setBlockState(pos.up(), block.getDefaultState());
                    }
                    stack.shrink(1);
                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public boolean shouldOverrideModel(Plant value) {
        return false;
    }

    @Override
    public boolean canBeInCreativeTab(Plant value) {
        return true;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if(this.isInCreativeTab(tab)) {
            subItems.addAll(this.getAllStacksOrdered());
        }
    }
}
