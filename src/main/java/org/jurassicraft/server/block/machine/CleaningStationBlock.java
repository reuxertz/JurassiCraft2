package org.jurassicraft.server.block.machine;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.OrientedBlock;
import org.jurassicraft.server.block.entity.BugCrateBlockEntity;
import org.jurassicraft.server.block.entity.CleaningStationBlockEntity;
import org.jurassicraft.server.proxy.ServerProxy;
import org.jurassicraft.server.tab.TabHandler;

public class CleaningStationBlock extends OrientedBlock {
    public CleaningStationBlock() {
        super(Material.IRON);
        this.setUnlocalizedName("cleaning_station");
        this.setHardness(2.0F);
        this.setLightOpacity(0);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(TabHandler.BLOCKS);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof BugCrateBlockEntity) {
            IItemHandler handler = ((BugCrateBlockEntity)tile).getInventory();
            for (int i = 0; i < handler.getSlots(); i++) {
                InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i));
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
    		EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else if (!player.isSneaking()) {
            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity instanceof CleaningStationBlockEntity) {
                CleaningStationBlockEntity cleaningStation = (CleaningStationBlockEntity) tileEntity;
                player.openGui(JurassiCraft.INSTANCE, ServerProxy.GUI_CLEANING_STATION_ID, world, pos.getX(), pos.getY(), pos.getZ());
                return true;
            }
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new CleaningStationBlockEntity();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
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
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }
}
