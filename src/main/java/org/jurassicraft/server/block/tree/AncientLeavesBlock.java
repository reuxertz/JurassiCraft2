package org.jurassicraft.server.block.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.jurassicraft.server.tab.TabHandler;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AncientLeavesBlock extends BlockLeaves {
    private TreeType treeType;

    public AncientLeavesBlock(TreeType type) {
        super();
        this.treeType = type;
        this.setUnlocalizedName(type.name().toLowerCase(Locale.ENGLISH) + "_leaves");
        this.setHardness(0.2F);
        this.setLightOpacity(1);
        this.setSoundType(SoundType.PLANT);
        this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, false)); //TODO Until we fix the decay code, keep this false
        this.setCreativeTab(TabHandler.PLANTS);
    }

    public TreeType getTreeType() {
        return this.treeType;
    }

    @Override
    protected void dropApple(World world, BlockPos pos, IBlockState state, int chance) {
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return Blocks.LEAVES.isOpaqueCube(state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return Blocks.LEAVES.getBlockLayer();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return Blocks.LEAVES.shouldSideBeRendered(state, world, pos, side);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = new ArrayList<>();
        Random rand = world instanceof World ? ((World) world).rand : new Random();
        int chance = this.treeType.getDropChance();

        if (fortune > 0) {
            chance -= 2 << fortune;
            if (chance < 2) {
                chance = 2;
            }
        }

        if (rand.nextInt(chance) == 0) {
            ItemStack drop = this.treeType.getDrop();
            drops.add(new ItemStack(drop.getItem(), drop.getCount()));
        }

        this.captureDrops(true);
        drops.addAll(this.captureDrops(false));
        return drops;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return this.treeType.getDrop().getItem();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
        list.add(new ItemStack(this, 1, 0));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean dec = meta < 4;
        boolean check = meta < 8;
        return this.getDefaultState().withProperty(DECAYABLE, dec).withProperty(CHECK_DECAY, check);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;

        if (!state.getValue(DECAYABLE)) {
            i = 4;
        }

        if (!state.getValue(CHECK_DECAY)) {
            i = 8;
        }

        return i;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return Lists.newArrayList(new ItemStack(this, 1, 0));
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return BlockPlanks.EnumType.BIRCH;
    }
}
