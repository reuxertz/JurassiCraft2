package org.jurassicraft.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRail;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.jurassicraft.server.tab.TabHandler;

import java.util.Random;

import static net.minecraft.block.BlockRailBase.EnumRailDirection.*;

public final class TourRailBlock extends BlockRail {
    public final boolean isPowered;

    public TourRailBlock(boolean isPowered) {
        this.isPowered = isPowered;
        this.setCreativeTab(isPowered ? null : TabHandler.BLOCKS);
        this.setUnlocalizedName("tour_rail");
        this.setHarvestLevel("pickaxe", 1);
        this.setHardness(1);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        if (!world.isRemote) {
            world.notifyNeighborsOfStateChange(pos, this, true);
            world.notifyNeighborsOfStateChange(pos.down(), this, true);
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        if (!world.isRemote) {
            state.neighborChanged(world, pos, this, pos);
        }
    }

    protected boolean findSignal(World world, BlockPos pos, IBlockState state, EnumFacing direction, int distance) {
        if (distance >= 8) {
            return false;
        }
        EnumRailDirection shape = state.getValue(SHAPE);
        BlockPos newPos = pos.offset(direction);
        if (shape.isAscending()) {
            boolean ascendingEast = shape == ASCENDING_EAST && direction == EnumFacing.EAST;
            boolean ascendingWest = shape == ASCENDING_WEST && direction == EnumFacing.WEST;
            boolean ascendingNorth = shape == ASCENDING_NORTH && direction == EnumFacing.NORTH;
            boolean ascendingSouth = shape == ASCENDING_SOUTH && direction == EnumFacing.SOUTH;
            if (ascendingEast || ascendingWest || ascendingNorth || ascendingSouth) {
                newPos = newPos.up();
            }
        }
        return this.isSameRailWithPower(world, newPos, distance, direction, shape) || !shape.isAscending() && this.isSameRailWithPower(world, newPos.down(), distance, direction, shape);
    }

    protected boolean isSameRailWithPower(World world, BlockPos pos, int distance, EnumFacing direction, BlockRailBase.EnumRailDirection shape) {
        IBlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof TourRailBlock)) {
            return false;
        }
        BlockRailBase.EnumRailDirection checkedShape = state.getValue(SHAPE);
        boolean hadPower = ((TourRailBlock) state.getBlock()).isPowered;
        boolean isX = checkedShape != EAST_WEST && checkedShape != ASCENDING_EAST && checkedShape != ASCENDING_WEST;
        boolean isZ = checkedShape != NORTH_SOUTH && checkedShape != ASCENDING_NORTH && checkedShape != ASCENDING_SOUTH;
        boolean canMove = (shape != EAST_WEST || isZ) && (shape != NORTH_SOUTH || isX);
        return canMove && hadPower && world.isBlockPowered(pos) || (this.findSignal(world, pos, state, direction, distance + 1) || (!shape.isAscending() && this.findSignal(world, pos, state, direction.rotateY(), distance + 1) || this.findSignal(world, pos, state, direction.rotateYCCW(), distance + 1)));
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return isPowered;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        return 2;
    }

    @Override
    protected void updateState(IBlockState state, World world, BlockPos pos, Block block) {
        super.updateState(state, world, pos, block);
        state = world.getBlockState(pos);
        boolean currentlyPowered = world.isBlockPowered(pos);
        if (!currentlyPowered) {
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                if (this.findSignal(world, pos, state, facing, 0)) {
                    currentlyPowered = true;
                    break;
                }
            }
        }
        if (currentlyPowered != this.isPowered) {
            EnumRailDirection shape = state.getValue(SHAPE);
            state = (currentlyPowered ? BlockHandler.TOUR_RAIL_POWERED : BlockHandler.TOUR_RAIL).getDefaultState();
            world.setBlockState(pos, state.withProperty(SHAPE, shape), 3);
            world.notifyNeighborsOfStateChange(pos.down(), this, true);
            if (state.getValue(SHAPE).isAscending()) {
                world.notifyNeighborsOfStateChange(pos.up(), this, true);
            }
        }
    }

    @Override
    public IProperty<EnumRailDirection> getShapeProperty() {
        return SHAPE;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rotation) {
        EnumRailDirection shape = state.getValue(SHAPE);
        switch (rotation) {
            case CLOCKWISE_180:
                switch (shape) {
                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, ASCENDING_EAST);
                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, NORTH_WEST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, NORTH_EAST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, SOUTH_EAST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, SOUTH_WEST);
				default:
					break;
                }
            case COUNTERCLOCKWISE_90:
                switch (shape) {
                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, ASCENDING_NORTH);
                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, ASCENDING_SOUTH);
                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, ASCENDING_WEST);
                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, ASCENDING_EAST);
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, NORTH_EAST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, SOUTH_EAST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, SOUTH_WEST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, NORTH_WEST);
                    case NORTH_SOUTH:
                        return state.withProperty(SHAPE, EAST_WEST);
                    case EAST_WEST:
                        return state.withProperty(SHAPE, NORTH_SOUTH);
                }
            case CLOCKWISE_90:
                switch (shape) {
                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, ASCENDING_SOUTH);
                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, ASCENDING_NORTH);
                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, ASCENDING_EAST);
                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, ASCENDING_WEST);
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, NORTH_WEST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, NORTH_EAST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, SOUTH_EAST);
                    case NORTH_SOUTH:
                        return state.withProperty(SHAPE, EAST_WEST);
                    case EAST_WEST:
                        return state.withProperty(SHAPE, NORTH_SOUTH);
                }
            default:
                return state;
        }
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        BlockRailBase.EnumRailDirection shape = state.getValue(SHAPE);
        switch (mirrorIn) {
            case LEFT_RIGHT:
                switch (shape) {
                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, NORTH_EAST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, NORTH_WEST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, SOUTH_WEST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, SOUTH_EAST);
                    default:
                        return super.withMirror(state, mirrorIn);
                }
            case FRONT_BACK:
                switch (shape) {
                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, SOUTH_EAST);
                    case NORTH_WEST:
                        return state.withProperty(SHAPE, NORTH_EAST);
                    case NORTH_EAST:
                        return state.withProperty(SHAPE, NORTH_WEST);
                }
		default:
			break;
        }

        return super.withMirror(state, mirrorIn);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, SHAPE);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(BlockHandler.TOUR_RAIL);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return this.getItem(world, pos, state);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(BlockHandler.TOUR_RAIL);
    }
}
