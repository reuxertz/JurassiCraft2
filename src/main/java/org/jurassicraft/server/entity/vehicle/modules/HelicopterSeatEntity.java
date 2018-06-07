package org.jurassicraft.server.entity.vehicle.modules;

import com.google.common.base.Predicate;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import org.jurassicraft.server.entity.vehicle.HelicopterBaseEntity;

import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Entity representing a seat inside the helicopter. Should NOT be spawned inside the world, the {@link HelicopterBaseEntity Helicopter Entity} handles that for you.
 */
@Deprecated //REMOVE
public class HelicopterSeatEntity extends Entity implements IEntityAdditionalSpawnData {
    public HelicopterBaseEntity parent;
    private UUID parentID;
    private float dist;
    private int index;

    public HelicopterSeatEntity(World worldIn) {
        super(worldIn);
        this.setEntityBoundingBox(this.createBoundingBox());
        this.noClip = true;
        this.parentID = UUID.randomUUID();
    }

    public HelicopterSeatEntity(float dist, int index, HelicopterBaseEntity parent) {
        super(parent.getEntityWorld());
        this.setEntityBoundingBox(this.createBoundingBox());
        this.dist = dist;
        this.index = index;
        this.parent = checkNotNull(parent, "parent");
        this.parentID = parent.getHeliID();
        this.noClip = true;
    }

    public static HelicopterBaseEntity getParentFromID(World world, final UUID id) {
        List<HelicopterBaseEntity> list = world.getEntities(HelicopterBaseEntity.class, new Predicate<Entity>() {
            @Override
            public boolean apply(Entity input) {
                if (input instanceof HelicopterBaseEntity) {
                    HelicopterBaseEntity helicopterBase = (HelicopterBaseEntity) input;
                    return helicopterBase.getHeliID().equals(id);
                }
                return false;
            }
        });
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    private AxisAlignedBB createBoundingBox() {
        return new AxisAlignedBB(this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ);
    }

    @Override
    protected void entityInit() {
        this.width = 0f;
        this.height = 0f;
    }

    @Override
    public void onUpdate() {
        this.update();
    }

    public void update() {
        this.motionX = 0f;
        this.motionY = 0f;
        this.motionZ = 0f;
        if (this.parent == null) // we are in this state right after reloading a map
        {
            this.parent = getParentFromID(this.world, this.parentID);
        }
        if (this.parent != null) {
            float angle = this.parent.rotationYaw;

            this.resetPos();
            if (this.parent.getSeat(this.index) == null) {
                this.parent.setSeat(this.index, this);
            }
            if (this.parent.isDead && !this.world.isRemote) {
                this.world.removeEntity(this);
            }
        } else {
            System.out.println("no parent :c " + this.parentID);
        }
        this.setEntityBoundingBox(this.createBoundingBox());
    }

    public void resetPos() {
        float nx = -MathHelper.sin(this.parent.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.parent.rotationPitch / 180.0F * (float) Math.PI) * this.dist;
        float nz = MathHelper.cos(this.parent.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.parent.rotationPitch / 180.0F * (float) Math.PI) * this.dist;
        float ny = MathHelper.sin((this.parent.rotationPitch) / 180.0F * (float) Math.PI) * this.dist;

        this.posX = this.parent.posX + nx - (this.parent.lastTickPosX - this.parent.posX);
        this.posY = this.parent.posY + ny + 0.4f - (this.parent.lastTickPosY - this.parent.posY);
        this.posZ = this.parent.posZ + nz - (this.parent.lastTickPosZ - this.parent.posZ);
        if (Double.isNaN(this.posX) || Double.isNaN(this.posY) || Double.isNaN(this.posZ)) {
            this.posX = this.lastTickPosX;
            this.posY = this.lastTickPosY;
            this.posZ = this.lastTickPosZ;
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompound) {
        this.dist = tagCompound.getFloat("dist");
        this.index = tagCompound.getInteger("index");
        this.parentID = UUID.fromString(tagCompound.getString("heliID"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setFloat("dist", this.dist);
        tagCompound.setInteger("index", this.index);
        tagCompound.setString("heliID", this.parentID.toString());
    }

    public HelicopterBaseEntity getParent() {
        return this.parent;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public double getMountedYOffset() {
        return 0f;
    }

    public void setParentID(UUID parentID) {
        this.parentID = parentID;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        ByteBufUtils.writeUTF8String(buffer, this.parentID.toString());
        buffer.writeFloat(this.dist);
        buffer.writeInt(this.index);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        this.parentID = UUID.fromString(ByteBufUtils.readUTF8String(additionalData));
        this.dist = additionalData.readFloat();
        this.index = additionalData.readInt();
    }
}
