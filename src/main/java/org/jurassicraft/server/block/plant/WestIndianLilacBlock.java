package org.jurassicraft.server.block.plant;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import java.util.Random;

public class WestIndianLilacBlock extends DoublePlantBlock implements IGrowable{

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 2);

    private ItemStack itemDrop;

    public WestIndianLilacBlock() {
        super(Material.VINE);
    }

    public boolean isMaxAge(IBlockState state)
    {
        return state.getValue(AGE).intValue() >= 2;
    }

    public void setItemDrop(ItemStack itemIn)
    {
        this.itemDrop = itemIn;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() != this)
        {
            return this.getDefaultState();
        }

        return state;
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return !this.isMaxAge(state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AGE, meta % 3).withProperty (HALF, meta >= 3 ? BlockHalf.UPPER : BlockHalf.LOWER);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(AGE) * (state.getValue(HALF) ==  BlockHalf.UPPER ? 2 : 1);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, AGE, HALF);
    }


    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return true;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        int age = state.getValue(AGE).intValue();

        if (age <= 2) {
            worldIn.setBlockState(pos, this.getDefaultState().withProperty(AGE, MathHelper.clamp(++age, 0, 2)));
        }
    }


    @Override
    public int damageDropped(IBlockState state)
    {
        return 0;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        int age = state.getValue(AGE).intValue();

        if (age == 2)
        {
            if (worldIn.isRemote)
            {
                return true;
            }

            worldIn.setBlockState(pos, this.getDefaultState().withProperty(AGE, MathHelper.clamp(++age, 0, 2)));

            ItemStack itemDrop = new ItemStack(this.itemDrop.getItem(), 1, this.itemDrop.getItemDamage());
            EntityItem entityitem = new EntityItem(worldIn, playerIn.posX, playerIn.posY - 1.0D, playerIn.posZ, itemDrop);

            worldIn.spawnEntity(entityitem);

            if (!(playerIn instanceof FakePlayer))
            {
                entityitem.onCollideWithPlayer(playerIn);
            }

            return true;
        }

        return false;
    }

}
