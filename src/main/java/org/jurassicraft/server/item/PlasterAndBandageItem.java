package org.jurassicraft.server.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.FossilBlock;
import org.jurassicraft.server.block.NestFossilBlock;
import org.jurassicraft.server.entity.Dinosaur;

public class PlasterAndBandageItem extends Item {
    PlasterAndBandageItem() {
        super();

    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    	ItemStack stack = player.getHeldItem(hand);
        if (player.canPlayerEdit(pos.offset(side), side, stack)) {
            IBlockState state = world.getBlockState(pos);

            Block block = state.getBlock();

            if (block instanceof FossilBlock) {
                if (!world.isRemote) {
                    Dinosaur dinosaur = ((FossilBlock)block).getDinosaur(world, pos);
                    world.setBlockState(pos, BlockHandler.ENCASED_FOSSIL.getDefaultState());

                    TileEntity tileEntity = world.getTileEntity(pos);
                    if(tileEntity instanceof FossilBlock.FossilBlockEntity) {
                        ((FossilBlock.FossilBlockEntity)tileEntity).setDinosaur(dinosaur);
                    }

                    if (!player.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                }

                return EnumActionResult.SUCCESS;
            } else if (block instanceof NestFossilBlock && !((NestFossilBlock) block).encased) {
                if (!world.isRemote) {
                    world.setBlockState(pos, BlockHandler.ENCASED_NEST_FOSSIL.get(((NestFossilBlock)block).getVariant()).getDefaultState());

                    if (!player.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                }

                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }
}
