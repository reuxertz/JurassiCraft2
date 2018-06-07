package org.jurassicraft.server.entity.vehicle;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.entity.vehicle.modules.HelicopterDoor;
import org.jurassicraft.server.entity.vehicle.modules.HelicopterModule;
import org.jurassicraft.server.entity.vehicle.modules.HelicopterModuleSpot;
import org.jurassicraft.server.entity.vehicle.modules.HelicopterSeatEntity;
import org.jurassicraft.server.entity.vehicle.modules.ModulePosition;
import org.jurassicraft.server.item.vehicles.HelicopterModuleItem;
import org.jurassicraft.server.message.HelicopterDirectionMessage;
import org.jurassicraft.server.message.HelicopterEngineMessage;
import org.jurassicraft.server.message.HelicopterModulesMessage;
import org.jurassicraft.server.util.Easings;
import org.jurassicraft.server.util.MutableVec3;

import java.util.Locale;
import java.util.UUID;

/**
 * Base entity for the helicopter, also holds the {@link HelicopterSeatEntity Seat} entities and updates/handles them.
 */
public class HelicopterBaseEntity extends EntityLivingBase implements IEntityAdditionalSpawnData {
    public static final int FRONT = ModulePosition.FRONT.ordinal();
    public static final int LEFT_SIDE = ModulePosition.LEFT_SIDE.ordinal();
    public static final int RIGHT_SIDE = ModulePosition.RIGHT_SIDE.ordinal();
    public static final float MAX_POWER = 80.0F;
    public static final float REQUIRED_POWER = MAX_POWER / 2.0F;
    private static final DataParameter<Boolean> DATA_WATCHER_ENGINE_RUNNING = EntityDataManager.createKey(HelicopterBaseEntity.class, DataSerializers.BOOLEAN);
    private final HelicopterModuleSpot[] moduleSpots;
    private final HelicopterSeatEntity[] seats;
    private boolean syncModules;
    private UUID heliID;
    private float roll;
    private boolean engineRunning;
    private float enginePower;
    private MutableVec3 direction;
    private boolean modulesSynced;
    private float rotorRotation;

    public HelicopterBaseEntity(World worldIn, UUID id) {
        this(worldIn);
        this.prepareDefaultModules();
        this.setID(id);
    }

    public HelicopterBaseEntity(World worldIn) {
        super(worldIn);
        double w = 3f; // width in blocks
        double h = 3.1f; // height in blocks
        double d = 8f; // depth in blocks
        this.setBox(0, 0, 0, w, h, d);

        this.seats = new HelicopterSeatEntity[3];
        for (int i = 0; i < this.seats.length; i++) {
            float distance = i == 0 ? 1.5f : 0; // TODO: Better way to define position
            this.seats[i] = new HelicopterSeatEntity(distance, i, this);
            this.world.spawnEntity(this.seats[i]);
        }
        this.setID(UUID.randomUUID());
        this.moduleSpots = new HelicopterModuleSpot[ModulePosition.values().length];
        this.moduleSpots[FRONT] = new HelicopterModuleSpot(ModulePosition.FRONT, this, 0);
        this.moduleSpots[LEFT_SIDE] = new HelicopterModuleSpot(ModulePosition.LEFT_SIDE, this, (float) Math.PI);
        this.moduleSpots[RIGHT_SIDE] = new HelicopterModuleSpot(ModulePosition.RIGHT_SIDE, this, 0);

        this.direction = new MutableVec3(0, 1, 0);
        this.syncModules = true;
    }

    private void setID(UUID id) {
        this.heliID = id;
        if (this.seats != null) {
            for (HelicopterSeatEntity seat : this.seats) {
                if (seat != null) {
                    seat.setParentID(id);
                }
            }
        }
    }

    public void prepareDefaultModules() {
        this.syncModules = false;
        this.getModuleSpot(ModulePosition.LEFT_SIDE).addModule(new HelicopterDoor());
//        getModuleSpot(ModulePosition.RIGHT_SIDE).addModule(new HelicopterMinigun());
        this.syncModules = true;
    }

    /**
     * Sets entity size
     *
     * @param offsetX The offset of the box in blocks on the X axis
     * @param offsetY The offset of the box in blocks on the Y axis
     * @param offsetZ The offset of the box in blocks on the Z axis
     * @param w The width of the entity
     * @param h The height of the entity
     * @param d The depth of the entity
     */
    private void setBox(double offsetX, double offsetY, double offsetZ, double w, double h, double d) {
        double minX = this.getEntityBoundingBox().minX + offsetX;
        double minY = this.getEntityBoundingBox().minY + offsetY;
        double minZ = this.getEntityBoundingBox().minZ + offsetZ;
        double maxX = this.getEntityBoundingBox().minX + w + offsetX;
        double maxY = this.getEntityBoundingBox().minY + h + offsetY;
        double maxZ = this.getEntityBoundingBox().minZ + d + offsetZ;
        this.setEntityBoundingBox(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ));
        this.width = (float) (maxX - minX);
        this.height = (float) (maxY - minY);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(DATA_WATCHER_ENGINE_RUNNING, false);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound) {
        super.readEntityFromNBT(tagCompound);
        this.setID(UUID.fromString(tagCompound.getString("heliID")));

        NBTTagList spots = tagCompound.getTagList("spots", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < spots.tagCount(); i++) {
            NBTTagCompound spotData = spots.getCompoundTagAt(i);
            ModulePosition position = ModulePosition.valueOf(spotData.getString("position").toUpperCase(Locale.ENGLISH));
            this.getModuleSpot(position).readFromNBT(spotData);
        }

        this.modulesSynced = false;
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return null;
    }

    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slot) {
        return null;
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setString("heliID", this.heliID.toString());

        NBTTagList spots = new NBTTagList();
        for (HelicopterModuleSpot spot : this.moduleSpots) {
            NBTTagCompound spotData = new NBTTagCompound();
            spot.writeToNBT(spotData);
            String position = spot.getPosition().name().toLowerCase(Locale.ENGLISH);
            spotData.setString("position", position);
            spots.appendTag(spotData);
        }
        tagCompound.setTag("spots", spots);
    }

    // apparently up to the entity to update its position given the motion

    @Override
    public void onLivingUpdate() {
        if (!this.modulesSynced && this.isServerWorld()) {
            for (HelicopterModuleSpot spot : this.moduleSpots) {
                JurassiCraft.NETWORK_WRAPPER.sendToAll(new HelicopterModulesMessage(this.getEntityId(), spot.getPosition(), spot));
            }
            this.modulesSynced = true;
        }
        super.onLivingUpdate();

        // update rotor angle
        float time = this.enginePower / MAX_POWER;
        this.rotorRotation += Easings.easeInCubic(time, this.rotorRotation, this.enginePower, 1f);
        this.rotorRotation %= 360f;

        this.fallDistance = 0f;
        this.ignoreFrustumCheck = true; // always draws the entity
        // Update seats positions
        for (HelicopterSeatEntity seat : this.seats) {
            if (seat != null) {
                seat.setParentID(this.heliID);
                seat.parent = this;
                //     seat.update();
            }
        }

        HelicopterSeatEntity seat = this.seats[0];
        if (seat != null) {
            Entity controller = seat.getControllingPassenger();
            boolean runEngine;
            if (controller != null) // There is a pilot!
            {
                EntityPlayer rider = (EntityPlayer) controller;
                if (this.world.isRemote) // We are on client
                {
                    runEngine = this.handleClientRunning(rider);
                    if (this.isPilotThisClient(rider)) {
                        this.updateEngine(runEngine);
                        this.engineRunning = runEngine;
                        if (this.enginePower >= REQUIRED_POWER) {
                            this.direction = this.drive(this.direction);
                        } else {
                            this.direction.set(0, 1, 0);
                        }
                    }
                }
            } else {
                runEngine = false;
                this.updateEngine(runEngine);
                this.direction.set(0, 1f, 0);
            }
            this.rotationYaw -= this.direction.xCoord * 1.25f;
            this.roll = (float) (this.direction.xCoord * 20f);
            this.rotationPitch = (float) -(this.direction.zCoord * 40f);
            this.updateDirection(this.direction);
            if (this.engineRunning) {
                this.enginePower++;
            } else {
                if (this.enginePower >= REQUIRED_POWER) {
                    this.enginePower -= 0.5f;
                } else {
                    this.enginePower--;
                }
                if (this.enginePower < 0f) {
                    this.enginePower = 0f;
                }
            }
            if (this.enginePower >= REQUIRED_POWER) {
                // We can fly \o/
                // ♪ Fly on the wings of code! ♪
                MutableVec3 localDir = new MutableVec3(this.direction.xCoord, this.direction.yCoord, this.direction.zCoord * 8f);
                localDir = localDir.rotateYaw((float) Math.toRadians(-this.rotationYaw));
                final float gravityCancellation = 0.08f;
                final float speedY = gravityCancellation + 0.005f;
                double my = speedY * localDir.yCoord;
                if (my < gravityCancellation) {
                    my = gravityCancellation;
                }
                this.motionY += my * 1 * (this.enginePower / MAX_POWER);
                this.motionX = localDir.xCoord / 10f;
                this.motionZ = localDir.zCoord / 10f;
            }
            if (this.enginePower >= MAX_POWER) {
                this.enginePower = MAX_POWER;
            }
        }
    }

    private void updateDirection(MutableVec3 direction) {
        if (this.world.isRemote) {
            JurassiCraft.NETWORK_WRAPPER.sendToServer(new HelicopterDirectionMessage(this.getEntityId(), direction));
        } else {
            JurassiCraft.NETWORK_WRAPPER.sendToAll(new HelicopterDirectionMessage(this.getEntityId(), direction));
        }
    }

    @SideOnly(Side.CLIENT)
    private MutableVec3 drive(MutableVec3 direction) {
        if (Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown()) {
            direction.addVector(0, 0, 1f);
        }

        if (Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown()) {
            direction.addVector(0, 0, -1f);
        }

        if (Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown()) {
            direction.addVector(1f, 0, 0);
        }

        if (Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown()) {
            direction.addVector(-1f, 0, 0);
        }

        if (!Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown()) {
            direction.addVector(0, 1, 0);
        }

        return direction.normalize();
    }

    @Override
    protected void collideWithNearbyEntities() {
    }

    public void updateEngine(boolean engineState) {
        if (this.world.isRemote) {
            JurassiCraft.NETWORK_WRAPPER.sendToServer(new HelicopterEngineMessage(this.getEntityId(), engineState));
        } else {
            JurassiCraft.NETWORK_WRAPPER.sendToAll(new HelicopterEngineMessage(this.getEntityId(), engineState));
        }
    }

    /**
     * Checks if the current pilot is the player using this client
     *
     * @param pilot The pilot
     * @return True if Client's player's UUID is equal to pilot
     */
    @SideOnly(Side.CLIENT)
    private boolean isPilotThisClient(EntityPlayer pilot) {
        return pilot.getUniqueID().equals(Minecraft.getMinecraft().player.getUniqueID());
    }

    @SideOnly(Side.CLIENT)
    private boolean handleClientRunning(EntityPlayer rider) {
        if (this.isPilotThisClient(rider)) {
            if (Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
        // Transforms the vector in local coordinates (cancels possible rotations to simplify 'seat detection')
        Vec3d localVec = vec.rotateYaw((float) Math.toRadians(this.rotationYaw));

        if (!this.attachModule(player, localVec, activeItemStack)) {
            if (localVec.z > 0.6) {
                player.startRiding(this.seats[0]);
                return EnumActionResult.SUCCESS;
            } else if (localVec.z < 0.6 && localVec.x > 0) {
                player.startRiding(this.seats[1]);
                return EnumActionResult.SUCCESS;
            } else if (localVec.z < 0.6 && localVec.x < 0) {
                player.startRiding(this.seats[2]);
                return EnumActionResult.SUCCESS;
            }
            for (HelicopterModuleSpot spot : this.moduleSpots) {
                if (spot != null && spot.isClicked(localVec)) {
                    spot.onClicked(player, vec);
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        return EnumActionResult.PASS;
    }

    private boolean attachModule(EntityPlayer player, Vec3d localVec, ItemStack stack) {
        if (!this.world.isRemote) {
            if (stack != null) {
                Item item = stack.getItem();
                if (item instanceof HelicopterModuleItem) {
                    HelicopterModuleItem moduleItem = (HelicopterModuleItem) item;
                    HelicopterModule module = HelicopterModule.createFromID(moduleItem.getModuleID());
                    for (HelicopterModuleSpot spot : this.moduleSpots) {
                        if (spot != null && spot.isClicked(localVec)) {
                            if (spot.addModule(module)) {
                                if (!player.capabilities.isCreativeMode) {
                                    stack.shrink(1);
                                }
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be pushable on contact, like boats or minecarts.
     */
    @Override
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return this.getBoundingBox();
    }

    /**
     * returns the bounding box for this entity
     */
    public AxisAlignedBB getBoundingBox() {
        return this.getEntityBoundingBox();
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public EnumHandSide getPrimaryHand() {
        return EnumHandSide.RIGHT;
    }

    public float getRoll() {
        return this.roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public UUID getHeliID() {
        return this.heliID;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        ByteBufUtils.writeUTF8String(buffer, this.heliID.toString());

        for (HelicopterModuleSpot spot : this.moduleSpots) {
            spot.writeSpawnData(buffer);
        }
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        this.setID(UUID.fromString(ByteBufUtils.readUTF8String(additionalData)));

        for (HelicopterModuleSpot spot : this.moduleSpots) {
            spot.readSpawnData(additionalData);
        }
    }

    public boolean isEngineRunning() {
        return this.engineRunning;
    }

    public void setEngineRunning(boolean engineRunning) {
        this.engineRunning = engineRunning;
    }

    public float getEnginePower() {
        return this.enginePower;
    }

    public void setDirection(MutableVec3 direction) {
        this.direction.set(direction);
    }

    public HelicopterModuleSpot[] getModuleSpots() {
        return this.moduleSpots;
    }

    public HelicopterModuleSpot getModuleSpot(ModulePosition pos) {
        return this.moduleSpots[pos.ordinal()];
    }

    public boolean shouldSyncModules() {
        return this.syncModules;
    }

    public HelicopterSeatEntity getSeat(int index) {
        if (index < 0 || index >= this.seats.length) {
            throw new ArrayIndexOutOfBoundsException(index + ", size is " + this.seats.length);
        }
        return this.seats[index];
    }

    public void setSeat(int index, HelicopterSeatEntity seat) {
        this.seats[index] = seat;
    }

    public float getRotorRotation() {
        return this.rotorRotation;
    }
}
