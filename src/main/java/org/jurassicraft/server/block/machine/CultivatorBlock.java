package org.jurassicraft.server.block.machine;

import net.minecraft.world.IBlockAccess;
import org.jurassicraft.server.api.SubBlocksBlock;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.entity.CultivatorBlockEntity;
import org.jurassicraft.server.item.block.CultivateItemBlock;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CultivatorBlock extends BlockContainer {

    protected final EnumDyeColor color;

    public CultivatorBlock(EnumDyeColor color, String position) {
        super(Material.IRON);
        this.color = color;
        this.setUnlocalizedName("cultivator_" + position);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (world.getBlockState(pos).getBlock() == BlockHandler.CULTIVATOR_TOP) {
        	//TODO Verify Working
        	pos = pos.down();
        }

        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof CultivatorBlockEntity) {
            InventoryHelper.dropInventoryItems(world, pos, (CultivatorBlockEntity) tile);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new CultivatorBlockEntity();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
