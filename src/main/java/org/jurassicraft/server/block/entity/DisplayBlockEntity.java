package org.jurassicraft.server.block.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.entity.Dinosaur;
import org.jurassicraft.server.entity.dinosaur.DinosaurEntity;

public class DisplayBlockEntity extends TileEntity {
    private DinosaurEntity entity;
    private int rotation;

    private boolean isMale;
    private boolean isSkeleton;

    protected NBTTagCompound data;

    public void setDinosaur(Dinosaur dinosaur, boolean isMale, boolean isSkeleton) {
        this.isMale = isMale;
        this.isSkeleton = isSkeleton;
        this.entity = dinosaur.createEntity(this.world);
        this.initializeEntity(this.entity);
        this.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        this.entity = null;
        this.data = nbt.getCompoundTag("DinosaurTag");
        this.rotation = nbt.getInteger("Rotation");
        this.isMale = !nbt.hasKey("IsMale") || nbt.getBoolean("IsMale");
        this.isSkeleton = nbt.getBoolean("IsSkeleton");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        if (this.entity != null) {
            NBTTagCompound tag = this.entity.serializeNBT();
            nbt.setTag("DinosaurTag", tag);
        }

        nbt.setInteger("Rotation", this.rotation);
        nbt.setBoolean("IsMale", this.isMale);
        nbt.setBoolean("IsSkeleton", this.isSkeleton);

        return nbt;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        if (this.isSkeleton && this.entity != null) {
            return this.entity.getRenderBoundingBox().expand(3.0, 3.0, 3.0).offset(this.pos);
        }
        return super.getRenderBoundingBox();
    }

    public boolean isMale() {
        return this.isMale;
    }

    public boolean isSkeleton() {
        return this.isSkeleton;
    }

    public int getRot() {
        return this.rotation;
    }

    public void setRot(int rotation) {
        this.markDirty();
        this.rotation = rotation;
    }

    public DinosaurEntity getEntity() {
        if (this.entity == null) {
            return this.createEntity();
        }
        return this.entity;
    }

    public Dinosaur getDinosaur() {
        return this.entity == null ? Dinosaur.MISSING : this.entity.getDinosaur();
    }

    private DinosaurEntity createEntity() {
        if(this.data != null) {
            Entity entity1 = EntityList.createEntityFromNBT(this.data, world);
            if (entity1 instanceof DinosaurEntity) {
                DinosaurEntity entity = (DinosaurEntity) entity1;
                this.initializeEntity(entity);
                this.entity = entity;
                return entity;
            }
        }
        return null;
    }

    private void initializeEntity(DinosaurEntity entity) {
        entity.setupDisplay(this.isMale);
        entity.setSkeleton(this.isSkeleton);
    }
}
