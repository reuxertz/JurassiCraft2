package org.jurassicraft.server.entity.vehicle;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.TourRailBlock;
import org.jurassicraft.server.message.UpdateFordExplorerStateMessage;

import java.util.Locale;
import java.util.Set;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleFunction;
import java.util.function.Function;

public class FordExplorerEntity extends CarEntity implements IEntityAdditionalSpawnData {
    private static final double SQRT_2 = Math.sqrt(2);

    private static final double SQRT_2_OVER_2 = SQRT_2 / 2;

    private static final float LENGTH = 4.5F;

    private static final float RAIL_HEIGHT = 0.15F;

    private State state = new StateOffRail(this);

    private double headX, headY, headZ;

    private double tailX, tailY, tailZ;

    private float speed = 0.0F;

    public FordExplorerEntity(World world) {
        super(world);
    }

    public void setState(State state) {
        if (state.getType() != this.state.getType()) {
            this.state = state;
            if (!this.world.isRemote) {
                WorldServer worldServer = (WorldServer) this.world;
                Set<? extends EntityPlayer> trackers = worldServer.getEntityTracker().getTrackingPlayers(this);
                UpdateFordExplorerStateMessage message = new UpdateFordExplorerStateMessage(this);
                for (EntityPlayer player : trackers) {
                    JurassiCraft.NETWORK_WRAPPER.sendTo(message, (EntityPlayerMP) player);
                }
            }
        }
    }

    public State getState() {
        return this.state;
    }

    @Override
    public Entity getControllingPassenger() {
        return this.state.getControllingPassenger();
    }

    private Entity getActualControllingPassenger() {
        return super.getControllingPassenger();
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        if (!this.isInWater()) {
            if (this.forward()) {
                this.speed += 0.3F;
            }
        }
        this.speed *= 0.8F;

        if (!this.world.isRemote) {
            this.state.update();
        }
    }

    @Override
    public void setPosition(double x, double y, double z) {
        super.setPosition(x, y, z);
        this.setBodyToPos();
    }

    public void pull() {
        double deltaX = this.tailX - this.headX;
        double deltaY = this.tailY - this.headY;
        double deltaZ = this.tailZ - this.headZ;
        double len = FordExplorerEntity.LENGTH / Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
        this.tailX = this.headX + deltaX * len;
        this.tailY = this.headY + deltaY * len;
        this.tailZ = this.headZ + deltaZ * len;
        this.setPosToBody();
    }

    private void setBodyToPos() {
        Vec3d v = this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
        double dx = v.x * (FordExplorerEntity.LENGTH / 2);
        double dy = v.y * (FordExplorerEntity.LENGTH / 2);
        double dz = v.z * (FordExplorerEntity.LENGTH / 2);
        this.setHead(this.posX + dx, this.posY + dy, this.posZ + dz);
        this.tailX = this.posX - dx;
        this.tailY = this.posY - dy;
        this.tailZ = this.posZ - dz;
        BlockPos.MutableBlockPos tail = new BlockPos.MutableBlockPos((int) this.tailX, (int) this.tailY, (int) this.tailZ);
        if (this.world.isBlockLoaded(tail)) {
            int offset = this.world.isSideSolid(tail, EnumFacing.UP) ? 1 : -1;
            while (this.world.isSideSolid(tail.up(offset), EnumFacing.UP)) {
                tail.setY(tail.getY() + offset);
                this.tailY = tail.getY() + 1;
            }
            AxisAlignedBB bounds = this.world.getBlockState(tail).getCollisionBoundingBox(this.world, tail);
            if (bounds != null) {
                this.tailY = (this.tailY - 1) + bounds.maxY;
            } else {
                this.tailY--;
            }
        }
    }

    public void setHead(double headX, double headY, double headZ) {
        this.headX = headX;
        this.headY = headY;
        this.headZ = headZ;
        if (this.state != null) { // state only null during Entity#<init> setPosition
            BlockPos pos = new BlockPos(headX, headY, headZ);
            if (this.world.isBlockLoaded(pos)) {
                if (this.state.isRail(this.world.getBlockState(pos.down()))) {
                    this.headY -= 0.5;
                } else if (!this.state.isRail(this.world.getBlockState(pos)) && this.state.isRail(this.world.getBlockState(pos.up()))) {
                    this.headY += 0.5;
                }
            }
            if (!this.world.isRemote) {
                this.state.onMove(this.getHeadPos());
            }
        }
    }

    private BlockPos getHeadPos() {
        return new BlockPos(this.headX, this.headY, this.headZ);
    }

    private void setPosToBody() {
        double dx = this.tailX - this.headX;
        double dy = this.tailY - this.headY;
        double dz = this.tailZ - this.headZ;
        float yaw = (float) Math.toDegrees(MathHelper.atan2(dz, dx)) + 90;
        float pitch = (float) (Math.toDegrees(MathHelper.atan2(dy, MathHelper.sqrt(dx * dx + dz * dz))));
        this.setRotation(yaw, pitch);
        super.setPosition((this.headX + this.tailX) / 2, (this.headY + this.tailY) / 2 - FordExplorerEntity.RAIL_HEIGHT, (this.headZ + this.tailZ) / 2);
    }

    @Override
    public void dropItems() {
//        this.dropItem(ItemHandler.FORD_EXPLORER, 1); TODO
    }

    @Override
    protected Seat[] createSeats() {
        Seat frontLeft = new Seat(0, 0.563F, 0.35F, 0.35F, 0.5F, 0.25F);
        Seat frontRight = new Seat(1, -0.563F, 0.35F, 0.35F, 0.5F, 0.25F);
        Seat backLeft = new Seat(2, 0.5F, 0.35F, -0.97F, 0.4F, 0.25F);
        Seat backRight = new Seat(3, -0.5F, 0.35F, -0.97F, 0.4F, 0.25F);
        return new Seat[] { frontLeft, frontRight, backLeft, backRight };
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.state != null) {
            NBTTagCompound state = new NBTTagCompound();
            state.setByte("state_type", (byte) this.state.getType().ordinal());
            this.state.serialize(state);
            compound.setTag("state", state);
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("state")) {
            NBTTagCompound state = compound.getCompoundTag("state");
            byte stateType = state.getByte("state_type");
            if (stateType >= 0 && stateType < StateType.values().length) {
                this.state = StateType.values()[stateType].create(this);
                this.state.deserialize(compound);
            }
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeByte(this.state.getType().ordinal());
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        int stateTypeIndex = additionalData.readUnsignedByte();
        FordExplorerEntity.StateType[] stateTypes = FordExplorerEntity.StateType.values();
        StateType type = stateTypeIndex >= 0 && stateTypeIndex < stateTypes.length ? stateTypes[stateTypeIndex] : StateType.OFF_RAIL;
        this.state = type.create(this);
    }

    public enum StateType {
        OFF_RAIL(StateOffRail::new),
        ON_RAIL(StateOnRail::new);

        private final Function<FordExplorerEntity, ? extends State> supplier;

        StateType(Function<FordExplorerEntity, ? extends State> supplier) {
            this.supplier = supplier;
        }

        public State create(FordExplorerEntity entity) {
            return this.supplier.apply(entity);
        }
    }

    public static abstract class State {
        protected FordExplorerEntity entity;
        private BlockPos prevHead;

        private State(FordExplorerEntity entity) {
            this.entity = entity;
        }

        private void onMove(BlockPos pos) {
            if (this.entity.world.isBlockLoaded(pos)) {
                IBlockState state = this.entity.world.getBlockState(pos);
                if (this.applies(this.isRail(state) && this.isPowered(state))) {
                    if (!pos.equals(this.prevHead)) {
                        this.onMove(pos, state);
                    }
                } else {
                    this.entity.setState(this.getNextState(pos, state));
                }
                this.prevHead = pos;
            } else {
                this.prevHead = null;
            }
        }

        boolean isRail(IBlockState state) {
            return state.getBlock() instanceof TourRailBlock;
        }

        boolean isPowered(IBlockState state) {
            Block block = state.getBlock();
            return block instanceof TourRailBlock && ((TourRailBlock) block).isPowered;
        }

        public abstract StateType getType();

        protected abstract Entity getControllingPassenger();

        protected abstract boolean applies(boolean isRail);

        protected abstract State getNextState(BlockPos pos, IBlockState state);

        protected abstract void serialize(NBTTagCompound compound);

        protected abstract void deserialize(NBTTagCompound compound);

        protected void onMove(BlockPos pos, IBlockState state) {
        }

        protected void update() {
        }
    }

    public static final class StateOffRail extends State {
        private StateOffRail(FordExplorerEntity entity) {
            super(entity);
        }

        @Override
        public StateType getType() {
            return StateType.OFF_RAIL;
        }

        @Override
        protected Entity getControllingPassenger() {
            return null;
        }

        @Override
        protected boolean applies(boolean isRail) {
            return !isRail;
        }

        @Override
        protected State getNextState(BlockPos pos, IBlockState state) {
            return new StateOnRail(this.entity, pos, state);
        }

        @Override
        protected void serialize(NBTTagCompound compound) {
        }

        @Override
        protected void deserialize(NBTTagCompound compound) {
        }

        @Override
        protected void update() {
            this.entity.noClip = false;
            this.entity.setBodyToPos();
        }
    }

    public static final class StateOnRail extends State {
        private BlockPos pos;

        private OrientatedRail rail;

        private Direction direction;

        private float position;

        private StateOnRail(FordExplorerEntity entity, BlockPos pos, IBlockState state) {
            super(entity);
            this.setPosition(pos, state);
            this.direction = Direction.FORWARD;
        }

        private StateOnRail(FordExplorerEntity entity) {
            this(entity, BlockPos.ORIGIN, BlockHandler.TOUR_RAIL.getDefaultState());
        }

        private OrientatedRail setPosition(BlockPos pos, IBlockState state) {
            this.pos = pos;
            this.rail = OrientatedRail.get(state);
            return this.rail;
        }

        @Override
        public StateType getType() {
            return StateType.ON_RAIL;
        }

        @Override
        protected Entity getControllingPassenger() {
            return this.entity.getActualControllingPassenger();
        }

        @Override
        protected boolean applies(boolean isRail) {
            return isRail;
        }

        @Override
        protected State getNextState(BlockPos pos, IBlockState state) {
            return new StateOffRail(this.entity);
        }

        @Override
        protected void serialize(NBTTagCompound compound) {
            compound.setInteger("x", this.pos.getX());
            compound.setInteger("y", this.pos.getY());
            compound.setInteger("z", this.pos.getZ());
            compound.setFloat("position", this.position);
            compound.setByte("rail_rotation", (byte) this.rail.rotation.ordinal());
            compound.setByte("rail_shape", (byte) this.rail.shape.ordinal());
            compound.setByte("direction", (byte) this.direction.ordinal());
        }

        @Override
        protected void deserialize(NBTTagCompound compound) {
            this.pos = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
            this.position = compound.getFloat("position");
            this.rail = new OrientatedRail(RailShape.get(compound.getByte("rail_shape")), AxisRotation.get(compound.getByte("rail_rotation")));
            this.direction = Direction.get(compound.getByte("direction"));
        }

        @Override
        protected void onMove(BlockPos pos, IBlockState state) {
            OrientatedRail fromRail = this.rail;
            OrientatedRail toRail = this.setPosition(pos, state);
            if (this.direction == Direction.FORWARD ? fromRail.getOutputVector() != toRail.getInputVector() : fromRail.getInputVector() != toRail.getOutputVector()) {
                this.direction = this.direction.opposite();
            }
            if (this.position > fromRail.getLength()) {
                this.position -= fromRail.getLength();
            } else {
                this.position = 0;
            }
        }

        @Override
        protected void update() {
            this.entity.noClip = true;
            Vec3d point = this.rail.pointAt(this.direction, this.position);
            this.position += this.entity.speed;
            double x = this.pos.getX() + point.x;
            double y = this.pos.getY() + point.y;
            double z = this.pos.getZ() + point.z;
            this.entity.setHead(x, y + FordExplorerEntity.RAIL_HEIGHT, z);
            this.entity.pull();
        }
    }

    public static final class OrientatedRail {
        private static final Vec3d CENTER = new Vec3d(0.5, 0, 0.5);

        private final RailShape shape;

        private final AxisRotation rotation;

        private OrientatedRail(RailShape shape, AxisRotation rotation) {
            this.shape = shape;
            this.rotation = rotation;
        }

        private EnumFacing getInputVector() {
            return this.rotation.apply(this.shape.getInput());
        }

        public EnumFacing getOutputVector() {
            return this.rotation.apply(this.shape.getOutput());
        }

        private Vec3d pointAt(Direction direction, double distance) {
            double d = direction.apply(this.shape.getLength(), distance);
            return OrientatedRail.CENTER.add(this.rotation.apply(this.shape.pointAt(d)));
        }

        public double getLength() {
            return this.shape.getLength();
        }

        @Override
        public String toString() {
            return String.format("%s rail rotated %s", this.shape.name().toLowerCase(Locale.ROOT), this.rotation.name().toLowerCase(Locale.ROOT));
        }

        public static OrientatedRail get(IBlockState state) {
            BlockRailBase.EnumRailDirection stateShape = state.getValue(TourRailBlock.SHAPE);
            RailShape shape;
            AxisRotation rotation;
            switch (stateShape) {
                case NORTH_SOUTH:
                    shape = RailShape.STRAIGHT;
                    rotation = AxisRotation.NONE;
                    break;
                case EAST_WEST:
                    shape = RailShape.STRAIGHT;
                    rotation = AxisRotation.COUNTERCLOCKWISE_90;
                    break;
                case ASCENDING_EAST:
                    shape = RailShape.ASCENDING;
                    rotation = AxisRotation.CLOCKWISE_90;
                    break;
                case ASCENDING_WEST:
                    shape = RailShape.ASCENDING;
                    rotation = AxisRotation.COUNTERCLOCKWISE_90;
                    break;
                case ASCENDING_NORTH:
                    shape = RailShape.ASCENDING;
                    rotation = AxisRotation.CLOCKWISE_180;
                    break;
                case ASCENDING_SOUTH:
                    shape = RailShape.ASCENDING;
                    rotation = AxisRotation.NONE;
                    break;
                case SOUTH_EAST:
                    shape = RailShape.CURVE;
                    rotation = AxisRotation.CLOCKWISE_180;
                    break;
                case SOUTH_WEST:
                    shape = RailShape.CURVE;
                    rotation = AxisRotation.CLOCKWISE_90;
                    break;
                case NORTH_WEST:
                    shape = RailShape.CURVE;
                    rotation = AxisRotation.NONE;
                    break;
                case NORTH_EAST:
                    shape = RailShape.CURVE;
                    rotation = AxisRotation.COUNTERCLOCKWISE_90;
                    break;
                default:
                    throw new IllegalStateException();
            }
            return new OrientatedRail(shape, rotation);
        }
    }

    public enum Direction {
        FORWARD((l, v) -> v) {
            @Override
            public Direction opposite() {
                return BACKWARD;
            }
        },
        BACKWARD((l, v) -> l - v) {
            @Override
            public Direction opposite() {
                return FORWARD;
            }
        };

        private final DoubleBinaryOperator function;

        Direction(DoubleBinaryOperator function) {
            this.function = function;
        }

        public final double apply(double length, double value) {
            return this.function.applyAsDouble(length, value);
        }

        public abstract Direction opposite();

        public static Direction get(int index) {
            if (index < 0 || index >= Direction.values().length) {
                return Direction.FORWARD;
            }
            return Direction.values()[index];
        }
    }

    private enum RailShape {
        STRAIGHT(d -> new Vec3d(0, 0, d - 0.5), 1, EnumFacing.SOUTH),
        ASCENDING(d -> new Vec3d(0, d * SQRT_2_OVER_2, d * SQRT_2_OVER_2 - 0.5), SQRT_2, EnumFacing.SOUTH),
        CURVE(d -> new Vec3d(Math.cos(d) / 2 - 0.5, 0, Math.sin(d) / 2 - 0.5), Math.PI / 2, EnumFacing.SOUTH, EnumFacing.EAST);

        private final DoubleFunction<Vec3d> curve;

        private final double length;

        private final EnumFacing input;

        private final EnumFacing output;

        RailShape(DoubleFunction<Vec3d> curve, double length, EnumFacing input) {
            this(curve, length, input, input);
        }

        RailShape(DoubleFunction<Vec3d> curve, double length, EnumFacing input, EnumFacing output) {
            this.curve = curve;
            this.length = length;
            this.input = input;
            this.output = output;
        }

        public final Vec3d pointAt(double distance) {
            return this.curve.apply(distance);
        }

        public final double getLength() {
            return this.length;
        }

        public final EnumFacing getInput() {
            return this.input;
        }

        public final EnumFacing getOutput() {
            return this.output;
        }

        public static RailShape get(int index) {
            if (index < 0 || index >= RailShape.values().length) {
                return RailShape.STRAIGHT;
            }
            return RailShape.values()[index];
        }
    }

    private enum AxisRotation {
        NONE(1, 0, 0, 1),
        CLOCKWISE_90(0, 1, -1, 0),
        CLOCKWISE_180(-1, 0, 0, -1),
        COUNTERCLOCKWISE_90(0, -1, 1, 0);

        private final double xx, xz, zx, zz;

        AxisRotation(double xx, double xz, double zx, double zz) {
            this.xx = xx;
            this.xz = xz;
            this.zx = zx;
            this.zz = zz;
        }

        public final double getX(double x, double z) {
            return x * this.xx + z * this.xz;
        }

        public final double getZ(double x, double z) {
            return x * this.zx + z * this.zz;
        }

        public final Vec3d apply(Vec3d vec) {
            return new Vec3d(this.getX(vec.x, vec.z), vec.y, this.getZ(vec.x, vec.z));
        }

        public final EnumFacing apply(EnumFacing facing) {
            return EnumFacing.getHorizontal(facing.getHorizontalIndex() + this.ordinal());
        }

        public static AxisRotation get(int index) {
            if (index < 0 || index >= AxisRotation.values().length) {
                return AxisRotation.NONE;
            }
            return AxisRotation.values()[index];
        }
    }
}
