package org.jurassicraft.server.block;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.jurassicraft.server.api.CleanableItem;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.item.FossilItem;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.item.block.EncasedFossilItemBlock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EncasedFossilBlock extends FossilBlock implements CleanableItem {

    @Override
    public ItemBlock getItemBlock() {
        return new EncasedFossilItemBlock(this);
    }

    @Override
    public String getHarvestTool(IBlockState state) {
        return "pickaxe";
    }

    @Override
    public int getHarvestLevel(IBlockState state) {
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.SOLID;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return true;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return true;
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosion) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isCleanable(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCleanedItem(ItemStack stack, Random random) {
        Dinosaur dinosaur = this.getValue(stack);
        String[] bones = dinosaur.bones;
        return ItemHandler.FOSSIL.createNewStack(new FossilItem.FossilInfomation(dinosaur, bones[random.nextInt(bones.length)]));
    }

    @Override
    public List<ItemStack> getJEIRecipeTypes() {
        return this.getAllStacks();
    }

    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        Dinosaur dinosaur = this.getValue(inputItem);
        String[] bones = dinosaur.bones;
        float single = 100F / bones.length;

        List<Pair<Float, ItemStack>> list = Lists.newArrayList();
        for(String bone : bones) {
            list.add(Pair.of(single, ItemHandler.FOSSIL.createNewStack(new FossilItem.FossilInfomation(dinosaur, bone))));
        }
        return list;
    }
}
