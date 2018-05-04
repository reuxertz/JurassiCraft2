package org.jurassicraft.server.entity.vehicle;

import java.util.List;

import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.proxy.ClientProxy;
import org.jurassicraft.server.entity.vehicle.util.CarWheel;
import org.jurassicraft.server.entity.vehicle.util.WheelParticleData;
import org.jurassicraft.server.entity.vehicle.util.WheelVec;
import org.jurassicraft.server.message.UpdateVehicleControlMessage;
import org.omg.CORBA.DoubleHolder;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class CarEntity extends Entity {
    public static final DataParameter<Byte> WATCHER_STATE = EntityDataManager.createKey(CarEntity.class, DataSerializers.BYTE);
    public static final DataParameter<Float> WATCHER_HEALTH = EntityDataManager.createKey(CarEntity.class, DataSerializers.FLOAT);
    public static final float MAX_HEALTH = 40;
    private static final int LEFT     = 0b0001;
    private static final int RIGHT    = 0b0010;
    private static final int FORWARD  = 0b0100;
    private static final int BACKWARD = 0b1000;

    protected final Seat[] seats = createSeats();

    public float wheelRotation;
    public float wheelRotateAmount;
    public float prevWheelRotateAmount;

    private float rotationDelta;

    private int interpProgress;
    private double interpTargetX;
    private double interpTargetY;
    private double interpTargetZ;
    private double interpTargetYaw;
    private double interpTargetPitch;

    public final CarWheel backWheel = new CarWheel(); 
    public final CarWheel frontWheel = new CarWheel();
    public final CarWheel leftWheel = new CarWheel();
    public final CarWheel rightWheel = new CarWheel();
    
    public final List<WheelParticleData> wheelDataList = Lists.newArrayList(); //Entirely useless server-side. //TODO: stop adding to this on server-side.
    private final List<WheelParticleData> markedRemoved = Lists.newArrayList();
    
    public List<CarWheel> allInterps = Lists.newArrayList(backWheel, frontWheel, leftWheel, rightWheel);
    
    private float healAmount;
    private int healCooldown = 40;

    public CarEntity(World world) {
        super(world);
        this.setSize(3.0F, 2.5F);
        this.stepHeight = 1.5F;
        if (world.isRemote) {
            this.startSound();
        }
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(WATCHER_STATE, (byte) 0);
        this.dataManager.register(WATCHER_HEALTH, MAX_HEALTH);
    }

    public boolean left() {
        return this.getStateBit(LEFT);
    }

    public boolean right() {
        return this.getStateBit(RIGHT);
    }

    public boolean forward() {
        return this.getStateBit(FORWARD);
    }

    public boolean backward() {
        return this.getStateBit(BACKWARD);
    }

    public void left(boolean left) {
        this.setStateBit(LEFT, left);
    }

    public void right(boolean right) {
        this.setStateBit(RIGHT, right);
    }

    public void forward(boolean forward) {
        this.setStateBit(FORWARD, forward);
    }

    public void backward(boolean backward) {
        this.setStateBit(BACKWARD, backward);
    }

    private boolean getStateBit(int mask) {
        return (this.getControlState() & mask) != 0;
    }

    private void setStateBit(int mask, boolean newState) {
        byte state = this.getControlState();
        this.setControlState(newState ? state | mask : state & ~mask);
    }

    public byte getControlState() {
        return this.dataManager.get(WATCHER_STATE);
    }

    public void setControlState(int state) {
        this.dataManager.set(WATCHER_STATE, (byte) state);
    }

    public void setHealth(float health) {
        this.dataManager.set(WATCHER_HEALTH, health);
    }

    public float getHealth() {
        return this.dataManager.get(WATCHER_HEALTH);
    }

    @Override
    public boolean isInRangeToRenderDist(double dist) {
        return true;
    }

    @Override
    protected boolean canFitPassenger(Entity passenger) {
        return this.getPassengers().size() < this.seats.length;
    }

    @Override
    public Entity getControllingPassenger() {
        List<Entity> passengers = this.getPassengers();
        return passengers.isEmpty() ? null : passengers.get(0);
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int duration, boolean teleport) {
        this.interpTargetX = x;
        this.interpTargetY = y;
        this.interpTargetZ = z;
        this.interpTargetYaw = yaw;
        this.interpTargetPitch = pitch;
        this.interpProgress = duration;
    }

    @Override
    public void onEntityUpdate() {
        allInterps.forEach(CarWheel::onUpdate);
        wheelDataList.forEach(WheelParticleData::onUpdate);
        
        markedRemoved.forEach(wheelDataList::remove);
        markedRemoved.clear();
        
        super.onEntityUpdate();
        processWheel(this.backWheel, getWheelVec(2f, 1.3f, false));
        processWheel(this.frontWheel, getWheelVec(-2.5f, -1.3f, false));
        processWheel(this.leftWheel, getWheelVec(-2.5f, 1.3f, true));
        processWheel(this.rightWheel, getWheelVec(2f, -1.3f, true));      
        
        if (!this.world.isRemote) {
            if (this.healCooldown > 0) {
                this.healCooldown--;
            } else if (this.healAmount > 0) {
                this.setHealth(this.getHealth() + 1);
                this.healAmount--;
                if (this.getHealth() > MAX_HEALTH) {
                    this.setHealth(MAX_HEALTH);
                    this.healAmount = 0;
                }
            }
        }
        this.tickInterp();
        if (this.canPassengerSteer()) {
            if (this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof EntityPlayer)) {
                this.setControlState(0);
            }
            this.updateMotion();
            if (this.world.isRemote) {
                this.handleControl();
            }
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        } else {
            this.motionX = this.motionY = this.motionZ = 0;
        }
    }
    
    public void markRemoval(WheelParticleData data) {
	markedRemoved.add(data);
    }

    private void processWheel(CarWheel wheel, WheelVec wheelLoc) {
        wheel.setTargetY(MathHelper.clamp(wheelLoc.targetY, posY - 3, posY + 3));
        wheel.setCurrentWheelPos(wheelLoc.getPos());
        this.wheelDataList.add(new WheelParticleData(this, wheelLoc.actualPos));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        AxisAlignedBB aabb = this.getEntityBoundingBox().shrink(0.9f);
        for(BlockPos pos : BlockPos.getAllInBoxMutable(new BlockPos(Math.floor(aabb.minX), Math.floor(aabb.minY), Math.floor(aabb.minZ)), new BlockPos(Math.ceil(aabb.maxX), Math.ceil(aabb.maxY), Math.ceil(aabb.maxZ)))) {
            IBlockState state = world.getBlockState(pos);
            if(state.getMaterial() == Material.VINE) {
                if(world.isRemote) {
                    world.playEvent(2001, pos, Block.getStateId(state));
                } else {
                    state.getBlock().dropBlockAsItem(world, pos, state, 0);
                }
                world.setBlockToAir(pos);
            }
        }
        
        if(!world.isRemote) {
            world.getEntitiesWithinAABB(EntityLivingBase.class, aabb.grow(1f)).forEach(entity -> entity.attackEntityFrom(new DamageSource("jeep"), 5F));//TODO: create own damage source singleton, and make source dependant on speed
        }
        
        this.prevWheelRotateAmount = this.wheelRotateAmount;
        double deltaX = this.posX - this.prevPosX;
        double deltaZ = this.posZ - this.prevPosZ;
        float delta = Math.min(MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) * 4, 1);
        this.wheelRotateAmount += (delta - this.wheelRotateAmount) * 0.4F;
        this.wheelRotation += this.wheelRotateAmount;
        while (this.rotationYaw - this.prevRotationYaw < -180) {
            this.prevRotationYaw -= 360;
        }
        this.doBlockCollisions();
    }

    private void updateMotion() {
        final double resist = 0.8F;
        this.motionX *= resist;
        this.motionY *= resist;
        this.motionZ *= resist;
        this.rotationDelta *= resist;
        this.motionY -= 0.15F;
    }

    private void handleControl() {
        Entity driver = this.getControllingPassenger();
        if (!(driver instanceof EntityPlayer) || !((EntityPlayer) driver).isUser()) {
            return;
        }
        EntityPlayerSP player = (EntityPlayerSP) driver;
        MovementInput movementInput = player.movementInput;
        byte previous = this.getControlState();
        this.left(movementInput.leftKeyDown);
        this.right(movementInput.rightKeyDown);
        this.forward(movementInput.forwardKeyDown);
        this.backward(movementInput.backKeyDown);
        this.applyMovement();
        if (this.getControlState() != previous) {
            JurassiCraft.NETWORK_WRAPPER.sendToServer(new UpdateVehicleControlMessage(this));
        }
    }

    protected void applyMovement() {
        if (!this.isInWater()) {
            float moveAmount = 0.0F;
            if ((this.left() || this.right()) && !(this.forward() || this.backward())) {
                moveAmount += 0.05F;
            }
            if (this.forward()) {
                moveAmount += 0.1F;
            } else if (this.backward()) {
                moveAmount -= 0.05F;
            }
            if (this.left()) {
                this.rotationDelta -= 20.0F * moveAmount;
            } else if (this.right()) {
                this.rotationDelta += 20.0F * moveAmount;
            }
            this.rotationDelta = MathHelper.clamp(this.rotationDelta, -30 * 0.1F, 30 * 0.1F);
            this.rotationYaw += this.rotationDelta;
            this.motionX += MathHelper.sin(-this.rotationYaw * 0.017453292F) * moveAmount;
            this.motionZ += MathHelper.cos(this.rotationYaw * 0.017453292F) * moveAmount;
        }
    }

    private void tickInterp() {
        allInterps.forEach(CarWheel::doInterps);
        if (this.interpProgress > 0 && !this.canPassengerSteer()) {
            double interpolatedX = this.posX + (this.interpTargetX - this.posX) / (double) this.interpProgress;
            double interpolatedY = this.posY + (this.interpTargetY - this.posY) / (double) this.interpProgress;
            double interpolatedZ = this.posZ + (this.interpTargetZ - this.posZ) / (double) this.interpProgress;
            double deltaYaw = MathHelper.wrapDegrees(this.interpTargetYaw - (double) this.rotationYaw);
            this.rotationYaw = (float) ((double) this.rotationYaw + deltaYaw / (double) this.interpProgress);
//            double deltaPitch = MathHelper.wrapDegrees(this.interpTargetPitch - (double) this.rotationPitch);
//            this.rotationPitch = (float) ((double) this.rotationPitch + deltaPitch  / (double) this.interpProgress);
//            this.rotationPitch = (float) ((double) this.rotationPitch + (this.interpTargetPitch - (double) this.rotationPitch) / (double) this.interpProgress);
            this.interpProgress--;
            this.setPosition(interpolatedX, interpolatedY, interpolatedZ);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
    }
    
    private WheelVec getWheelVec(float wheelOffsetX, float wheelOffsetZ, boolean leftRight) { 

        float localYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw);
	double x = Math.sin(Math.toRadians(localYaw)) * wheelOffsetX - Math.cos(Math.toRadians(localYaw)) * wheelOffsetZ; 
        double z = - Math.cos(Math.toRadians(localYaw)) * wheelOffsetX - Math.sin(Math.toRadians(localYaw)) * wheelOffsetZ;
               
        Vec3d actualPos = new Vec3d(this.posX + x, this.posY, this.posZ + z);
        
        double ret = Integer.MIN_VALUE;
        
        double retX = Integer.MIN_VALUE;
        double retY = Integer.MIN_VALUE;
        double retZ = Integer.MIN_VALUE;
        
        double retTargetY = Integer.MIN_VALUE;
        
        boolean calculate = true;
        
        for(int i = -1 ; i <= 1; i++) {
            double rad = Math.toRadians(localYaw);
            double xRot = Math.sin(Math.toRadians(localYaw)) * (leftRight ? i : wheelOffsetX) - Math.cos(Math.toRadians(localYaw)) * (leftRight ? wheelOffsetZ : i); 
            double zRot = - Math.cos(Math.toRadians(localYaw)) * (leftRight ? i : wheelOffsetX) - Math.sin(Math.toRadians(localYaw)) * (leftRight ? wheelOffsetZ : i);
            Vec3d vec = new Vec3d(posX + xRot, this.posY, posZ + zRot);
            BlockPos pos = new BlockPos(vec);
            
            if(i == 0) {
                retX = vec.x;
                retY = vec.y;
                retZ = vec.z;
            }
            
            if(!calculate) {
                continue;
            }
            boolean found = false;
            List<AxisAlignedBB> aabbList = Lists.newArrayList();;
            while(!found) {
                if(pos.getY() < 0) {
                    calculate = false;
                    found = true;
                }
                aabbList.clear();
                world.getBlockState(pos).addCollisionBoxToList(world, pos, new AxisAlignedBB(pos), aabbList, this, false);
                if(world.isAirBlock(pos) || aabbList.isEmpty()) {
                    pos = pos.down();
                } else {
                    found = true;
                }
            }
            if(!found) {
                retTargetY = posY;
                calculate = false;
            }
            if(found && !world.isAirBlock(pos.up()) && !world.isAirBlock(pos.up(2))) {
                List<AxisAlignedBB> list = Lists.newArrayList();
                world.getBlockState(pos.up()).addCollisionBoxToList(world, pos.up(), new AxisAlignedBB(pos.up()), list, this, false);
                world.getBlockState(pos.up(2)).addCollisionBoxToList(world, pos.up(2), new AxisAlignedBB(pos.up(2)), list, this, false);
                if(!list.isEmpty()) {
                    retTargetY = posY;
                    calculate = false; //Means its facing a wall. 
                }
            }
            if(aabbList.isEmpty()) {
                retTargetY = pos.getY();
                calculate = false;
            }
            DoubleHolder holder = new DoubleHolder(Integer.MIN_VALUE);
            aabbList.forEach(aabb -> holder.value = Math.max(aabb.maxY, holder.value));
            if(holder.value > ret) {
                ret = holder.value;
            }
        }
        if(retX == Integer.MIN_VALUE || retY == Integer.MIN_VALUE || retZ == Integer.MIN_VALUE) {
            throw new RuntimeException("Returning positions wern't set. This should be impossile");
        }
        return new WheelVec(retX, retY, retZ, retTargetY != Integer.MIN_VALUE ? retTargetY : ret, actualPos);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (!this.world.isRemote && !player.isSneaking()) {
            player.startRiding(this);
        }
        return true;
    }
    
    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (!this.world.isRemote && passenger instanceof EntityPlayer && !(this.getControllingPassenger() instanceof EntityPlayer)) {
            Entity existing = this.seats[0].occupant;
            this.seats[0].occupant = passenger;
            this.usherPassenger(existing, 1);
        } else {
            this.usherPassenger(passenger, 0);
        }
    }

    private void usherPassenger(Entity passenger, int start) {
        for (int i = start; i < this.seats.length; i++) {
            Seat seat = this.seats[i];
            if (seat.occupant == null) {
                seat.occupant = passenger;
                return;
            }
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        for (Seat seat : this.seats) {
            if (passenger.equals(seat.occupant)) {
                seat.occupant = null;
                break;
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (!this.world.isRemote) {
            if (source.getTrueSource() instanceof EntityPlayer) {
                amount *= 10;
                this.healAmount += amount;
                this.healCooldown = 40;
            }
            this.setHealth(this.getHealth() - amount);
            if (this.getHealth() < 0 && !this.isDead) {
                this.setDead();
                if (this.world.getGameRules().getBoolean("doEntityDrops")) {
                    this.dropItems();
                }
            }
        }
        return true;
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger)) {
            Seat seat = null;
            for (Seat s : this.seats) {
                if (passenger.equals(s.occupant)) {
                    seat = s;
                    break;
                }
            }
            Vec3d pos;
            if (seat == null) {
                pos = new Vec3d(this.posX, this.posY + this.height, this.posZ);
            } else {
                pos = seat.getPos();
            }
            passenger.setPosition(pos.x, pos.y, pos.z);
            passenger.rotationYaw += this.rotationDelta;
            passenger.setRotationYawHead(passenger.getRotationYawHead() + this.rotationDelta);
            if (passenger instanceof EntityLivingBase) {
                EntityLivingBase living = (EntityLivingBase) passenger;
                living.renderYawOffset += (living.rotationYaw - living.renderYawOffset) * 0.6F;
            }
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.setHealth(compound.getFloat("Health"));
        this.healAmount = compound.getFloat("HealAmount");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setFloat("Health", this.getHealth());
        compound.setFloat("HealAmount", this.healAmount);
    }

    private void startSound() {
        ClientProxy.playCarSound(this);
    }

    protected final class Seat {
        private final int index;

        private float offsetX;
        private float offsetY;
        private float offsetZ;

        private final float radius;

        private final float height;

        private Entity occupant;

        public Seat(int index, float offsetX, float offsetY, float offsetZ, float radius, float height) {
            this.index = index;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.radius = radius;
            this.height = height;
        }

        protected void so(float x, float y, float z) {
            this.offsetX = x;
            this.offsetY = y;
            this.offsetZ = z;
        }

        public Entity getOccupant() {
            return this.occupant;
        }

        public Vec3d getPos() {
            double theta = Math.toRadians(CarEntity.this.rotationYaw);
            double sideX = Math.cos(theta);
            double sideZ = Math.sin(theta);
            double forwardTheta = theta + Math.PI / 2;
            double forwardX = Math.cos(forwardTheta);
            double forwardZ = Math.sin(forwardTheta);
            double x = CarEntity.this.posX + sideX * this.offsetX + forwardX * this.offsetZ;
            double y = CarEntity.this.posY + this.offsetY;
            double z = CarEntity.this.posZ + sideZ * this.offsetX + forwardZ * this.offsetZ;
            return new Vec3d(x, y, z);
        }

        public AxisAlignedBB getBounds() {
            Vec3d pos = this.getPos();
            double x = pos.x;
            double y = pos.y;
            double z = pos.z;
            return new AxisAlignedBB(x - this.radius, y, z - this.radius, x + this.radius, y + this.offsetY + this.height, z + this.radius);
        }
    }

    protected abstract Seat[] createSeats();

    public abstract void dropItems();
}
