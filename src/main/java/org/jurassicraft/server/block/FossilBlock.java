package org.jurassicraft.server.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jurassicraft.server.api.SubBlocksBlock;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.registries.JurassicraftRegisteries;
import org.jurassicraft.server.item.DinosaurProvider;
import org.jurassicraft.server.item.block.FossilItemBlock;
import org.jurassicraft.server.tab.TabHandler;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public class FossilBlock extends Block implements SubBlocksBlock, DinosaurProvider {

    public FossilBlock() {
        super(Material.ROCK);
        this.setHardness(2.0F);
        this.setResistance(8.0F);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(TabHandler.FOSSILS);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return super.createTileEntity(world, state);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(this.getItemStack(this.getDinosaur(world, pos)));
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if(willHarvest) {
            return true;
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        worldIn.setBlockToAir(pos);
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
    public boolean canDropFromExplosion(Explosion explosion) {
        return false;
    }

    @Override
    public ItemBlock getItemBlock() {
        return new FossilItemBlock(this);
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return false;
    }

    public Dinosaur getDinosaur(IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity instanceof FossilBlockEntity ? ((FossilBlockEntity)tileEntity).getDinosaur() : Dinosaur.MISSING;
    }


    public static class FossilBlockEntity extends TileEntity {

        private Dinosaur dinosaur;

        public void setDinosaur(Dinosaur dinosaur) {
            this.dinosaur = dinosaur;
        }

        public Dinosaur getDinosaur() {
            return dinosaur;
        }

        @Override
        public SPacketUpdateTileEntity getUpdatePacket() {
            NBTTagCompound nbt = new NBTTagCompound();
            this.writeToNBT(nbt);
            int metadata = getBlockMetadata();
            return new SPacketUpdateTileEntity(this.pos, metadata, nbt);
        }

        @Override
        public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
            this.readFromNBT(pkt.getNbtCompound());
        }

        @Override
        public NBTTagCompound getUpdateTag() {
            NBTTagCompound nbt = new NBTTagCompound();
            this.writeToNBT(nbt);
            return nbt;
        }

        @Override
        public void handleUpdateTag(NBTTagCompound tag) {
            this.readFromNBT(tag);
        }

        @Override
        public NBTTagCompound getTileData() {
            NBTTagCompound nbt = new NBTTagCompound();
            this.writeToNBT(nbt);
            return nbt;
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound compound) {
            compound.setString("Dinosaur", this.dinosaur.getRegistryName().toString());
            return super.writeToNBT(compound);
        }

        @Override
        public void readFromNBT(NBTTagCompound compound) {
            this.dinosaur = JurassicraftRegisteries.DINOSAUR_REGISTRY.getValue(new ResourceLocation("Dinosaur"));
            super.readFromNBT(compound);
        }
    }
}
