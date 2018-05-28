package org.jurassicraft.server.entity.vehicle;


import com.google.common.collect.Lists;
import net.minecraft.block.BlockRail;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.TourRailBlock;
import org.jurassicraft.server.entity.ai.util.InterpValue;
import org.jurassicraft.server.entity.ai.util.MathUtils;
import org.jurassicraft.server.entity.vehicle.util.WheelParticleData;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.message.FordExplorerChangeStateMessage;
import org.jurassicraft.server.message.FordExplorerUpdatePositionStateMessage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector4d;
import java.util.Arrays;
import java.util.List;

public class FordExplorerEntity extends CarEntity {

    public static final BlockPos INACTIVE = new BlockPos(-1, -1, -1);
    
    public boolean prevOnRails;
    public boolean onRails;
    private BlockPos prevRailTracks = INACTIVE;
    public BlockPos railTracks = INACTIVE;
    
    private boolean lastDirBackwards;

    public final MinecartLogic minecart = new MinecartLogic();
    
    private final InterpValue rotationYawInterp = new InterpValue(4f); //TODO: config
    
    /* =================================== CAR START ===========================================*/
    
    public FordExplorerEntity(World world) {
    	super(world);
	}

    @Override
    public void dropItems() {
        this.dropItem(ItemHandler.FORD_EXPLORER, 1);
    }

    @Override
    protected Seat[] createSeats() {
        Seat frontLeft = new Seat(0.563F, 0.45F, 0.4F, 0.5F, 0.25F);
        Seat frontRight = new Seat(-0.563F, 0.45F, 0.4F, 0.5F, 0.25F);
        Seat backLeft = new Seat( 0.5F, 0.7F, -2.2F, 0.4F, 0.25F);
        Seat backRight = new Seat( -0.5F, 0.7F, -2.2F, 0.4F, 0.25F);
        return new Seat[] { frontLeft, frontRight, backLeft, backRight };
    }
    
    @Override
    protected boolean shouldStopUpdates() {
        return onRails;
    }
    
    @Override
    public void onUpdate() {
		if(!world.isRemote) {
			BlockPos rail = getPosition();
			boolean isRails = world.getBlockState(rail).getBlock() instanceof TourRailBlock;
			if(!isRails) {
				rail = rail.down();
				isRails = world.getBlockState(rail).getBlock() instanceof TourRailBlock;
			}

			if(!isRails && world.getBlockState(rail.down()).getBlock() instanceof TourRailBlock && Arrays.asList(BlockRailBase.EnumRailDirection.ASCENDING_EAST, BlockRailBase.EnumRailDirection.ASCENDING_NORTH, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH, BlockRailBase.EnumRailDirection.ASCENDING_WEST).contains(world.getBlockState(rail.down()).getValue(BlockRail.SHAPE))) {
				rail = rail.down(1);
				isRails = world.getBlockState(rail).getBlock() instanceof TourRailBlock;
			}

			if(onRails != isRails) {
				if(isRails) {
					minecart.isInReverse = lastDirBackwards;
				}
				onRails = isRails;
				JurassiCraft.NETWORK_WRAPPER.sendToDimension(new FordExplorerChangeStateMessage(this), world.provider.getDimension());
			}
			this.railTracks = isRails ? rail : INACTIVE;
			if(!this.railTracks.equals(prevRailTracks)) {
				JurassiCraft.NETWORK_WRAPPER.sendToDimension(new FordExplorerUpdatePositionStateMessage(this, rail), world.provider.getDimension());
			}
			this.prevRailTracks = railTracks;

		}
		if(onRails) {
			this.setSize(1F, 0.5F);
			this.stepHeight = 0F;
		} else {
			this.setSize(3.0F, 2.5F);
			this.stepHeight = 1.5F;
		}
		this.setPosition(this.posX, this.posY, this.posZ); //Make sure that the car is in the right position. Can cause issues when changing size of car
		super.onUpdate();
		if(onRails) {
			minecart.onUpdate();
			Vector4d vec = wheeldata.carVector;
			this.backValue.setTarget(this.calculateWheelHeight(vec.y, false));
			this.frontValue.setTarget(this.calculateWheelHeight(vec.w, false));
			this.leftValue.setTarget(posY);
			this.rightValue.setTarget(posY);
		}
		prevOnRails = onRails;
    }

	@Override
    protected void doBlockCollisions() {
		if(!onRails) {
			super.doBlockCollisions();
		}
    }
    
    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        for (Seat seat : this.seats) {
            if (passenger.equals(seat.getOccupant())) {
                passenger.noClip = false;
        	break;
            }
        }
    }
    
    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if(onRails) {
            if(this.canPassengerSteer()) {
                if (this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof EntityPlayer)) {
                    this.setControlState(0);
                }
                if(this.world.isRemote) {
                    this.handleControl(); //+Z-X
                }
            }
        } else {
            rotationYawInterp.reset(this.rotationYaw - 180D);
        }
        if(forward()) {
            lastDirBackwards = false;
        } else if(backward()) {
            lastDirBackwards = true;
        }
    }
    
    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("OnRails", onRails);
        compound.setLong("BlockPosition", railTracks.toLong());
    }
    
    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        onRails = compound.getBoolean("OnRails");
        railTracks = BlockPos.fromLong(compound.getLong("BlockPosition"));
    }
    
    @Override
    public float getSoundVolume() {
        return onRails ? this.getControllingPassenger() != null ? this.getSpeed().modifier / 2f : 0f : super.getSoundVolume();
    }

    @Nonnull
    @Override
    public EnumFacing getAdjustedHorizontalFacing() {
        return onRails ? minecart.getAdjustedHorizontalFacing() : super.getAdjustedHorizontalFacing();
    }

    @Override
    protected WheelData createWheels() {
	return new WheelData(1, 2, -1, -2.2);
    }
    
    @Override
    protected boolean shouldTyresRender() {
        return super.shouldTyresRender() && !onRails;
    }

	@Override
	public Vector2d getBackWheelRotationPoint() {
        Vector2d point = super.getBackWheelRotationPoint();
		return new Vector2d(point.x, onRails ? 0 : point.y);
	}

    @Override
    public float getCollisionBorderSize() {
        return 2f;
    }

    /* =================================== CAR END ===========================================*/
    /* ================================ MINECART START =======================================*/
    private static final int[][][] MATRIX = new int[][][] {{{0, 0, -1}, {0, 0, 1}}, {{ -1, 0, 0}, {1, 0, 0}}, {{ -1, -1, 0}, {1, 0, 0}}, {{ -1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, { -1, 0, 0}}, {{0, 0, -1}, { -1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};

    public class MinecartLogic {
        private boolean isInReverse;
        private boolean prevKeyDown;

        public EnumFacing getAdjustedHorizontalFacing() {
            return this.isInReverse ? getHorizontalFacing().getOpposite().rotateY() : getHorizontalFacing().rotateY();
        }

        public void onUpdate() {
            //CAR STUFF START
            rotationDelta *= 0.8f;
            allWheels.forEach(FordExplorerEntity.this::processWheel);

            for(int i = 0; i < 4; i++) {
                List<WheelParticleData> markedRemoved = Lists.newArrayList();
                wheelDataList[i].forEach(wheel -> wheel.onUpdate(markedRemoved));
                markedRemoved.forEach(wheelDataList[i]::remove);
            }
            //CAR STUFF END

            if (posY < -64.0D) {
                outOfWorld();
            }

            if (!world.isRemote && world instanceof WorldServer) {
                world.profiler.startSection("portal");
                MinecraftServer minecraftserver = world.getMinecraftServer();
                int i = getMaxInPortalTime();
                if (inPortal) {
                    if (minecraftserver.getAllowNether()) {
                        if (!isRiding() && portalCounter++ >= i) {
                            portalCounter = i;
                            timeUntilPortal = getPortalCooldown();
                            int j;
                            if (world.provider.getDimensionType().getId() == -1) {
                                j = 0;
                            } else {
                                j = -1;
                            }

                            changeDimension(j);
                        }

                        inPortal = false;
                    }
                } else {
                    if (portalCounter > 0) {
                        portalCounter -= 4;
                    }

                    if (portalCounter < 0) {
                        portalCounter = 0;
                    }
                }

                if (timeUntilPortal > 0) {
                    --timeUntilPortal;
                }

                world.profiler.endSection();
            }

            if (!hasNoGravity()) {
                motionY -= 0.03999999910593033D;
            }
            if(railTracks.equals(INACTIVE)) { //Shouldn't occur
                return;
            }
            if(getControllingPassenger() == null) {
                return;
            }
            IBlockState iblockstate = world.getBlockState(railTracks);
            moveAlongTrack(iblockstate);

            if(!world.isRemote) {
                doBlockCollisions();
                rotationPitch = 0.0F;

                handleWaterMovement();
            }

        }

        protected void moveAlongTrack(IBlockState state) {
            fallDistance = 0.0F;
            Vec3d vec3d = getPos();

            posY = (double)railTracks.getY();

            double slopeAdjustment = 0.0078125D;
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = state.getValue(((TourRailBlock)state.getBlock()).getShapeProperty());

            switch (blockrailbase$enumraildirection) {
                case ASCENDING_EAST:
                    motionX -= slopeAdjustment;
                    ++posY;
                    break;
                case ASCENDING_WEST:
                    motionX += slopeAdjustment;
                    ++posY;
                    break;
                case ASCENDING_NORTH:
                    motionZ += slopeAdjustment;
                    ++posY;
                    break;
                case ASCENDING_SOUTH:
                    motionZ -= slopeAdjustment;
                    ++posY;
            }
            int[][] aint = MATRIX[blockrailbase$enumraildirection.getMetadata()];
            double d1 = (double)(aint[1][0] - aint[0][0]);
            double d2 = (double)(aint[1][2] - aint[0][2]);
            double d3 = Math.sqrt(d1 * d1 + d2 * d2);
            double d4 = motionX * d1 + motionZ * d2;

            if (d4 < 0.0D) {
                d1 = -d1;
                d2 = -d2;
            }

            double d5 = Math.sqrt(motionX * motionX + motionZ * motionZ);

            if (d5 > 2.0D) {
                d5 = 2.0D;
            }
            double d = 1;
            if(forward()) {
                if(!prevKeyDown && isInReverse) {
                    d = -1;
                }
                isInReverse = false;
                prevKeyDown = true;
            } else if(backward()) {
                if(!prevKeyDown && !isInReverse) {
                    d = -1;
                }
                isInReverse = true;
                prevKeyDown = true;
            } else {
                prevKeyDown = false;
            }
            if(!world.isRemote) {
                d5 *= d;
            }


            motionX = d5 * d1 / d3;
            motionZ = d5 * d2 / d3;

            double target;
            double d22;

            if(world.isRemote)
            {
                Vec3d vec = getPositionVector();
                Vec3d dirVec = new Vec3d(-d1, 0, d2).add(vec);
                target = MathUtils.cosineFromPoints(vec.addVector(0, 0, 1), dirVec, vec);
                if(dirVec.x < vec.x) {
                    target = -target;
                }
                if(isInReverse) {
                    target += 180F;
                }

                do {
                    d22 = Math.abs(rotationYawInterp.getCurrent() - target);
                    double d23 = Math.abs(rotationYawInterp.getCurrent() - (target + 360f));
                    double d24 = Math.abs(rotationYawInterp.getCurrent() - (target - 360f));

                    if(d23 < d22) {
                    target += 360f;
                    } else if(d24 < d22) {
                    target -= 360f;
                    }
                } while(d22 > 180);

                target = Math.round(target * 100D) / 100D;

                if(!prevOnRails) {
                        rotationYawInterp.reset(target);
                } else if(d != -1) {
                    rotationYawInterp.setTarget(target);
                }
            }

            setRotation((float) rotationYawInterp.getCurrent(), rotationPitch);

            double d18 = (double)railTracks.getX() + 0.5D + (double)aint[0][0] * 0.5D;
            double d19 = (double)railTracks.getZ() + 0.5D + (double)aint[0][2] * 0.5D;
            double d20 = (double)railTracks.getX() + 0.5D + (double)aint[1][0] * 0.5D;
            double d21 = (double)railTracks.getZ() + 0.5D + (double)aint[1][2] * 0.5D;
            d1 = d20 - d18;
            d2 = d21 - d19;
            double d10;

            if (d1 == 0.0D) {
                posX = (double)railTracks.getX() + 0.5D;
                d10 = posZ - (double)railTracks.getZ();
            } else if (d2 == 0.0D) {
                posZ = (double)railTracks.getZ() + 0.5D;
                d10 = posX - (double)railTracks.getX();
            } else {
                double d11 = posX - d18;
                double d12 = posZ - d19;
                d10 = (d11 * d1 + d12 * d2) * 2.0D;
            }

            posX = d18 + d1 * d10;
            posZ = d19 + d2 * d10;
            setPosition(posX, posY, posZ);
            moveMinecartOnRail();

            double drag = isBeingRidden() ? 0.9D : 0.75D;

            motionX *= drag;
            motionZ *= drag;

            Vec3d vec3d1 = getPos();

            if (vec3d1 != null && vec3d != null) {
                double d14 = (vec3d.y - vec3d1.y) * 0.05D;
                d5 = Math.sqrt(motionX * motionX + motionZ * motionZ);

                if (d5 > 0.0D) {
                    motionX = motionX / d5 * (d5 + d14);
                    motionZ = motionZ / d5 * (d5 + d14);
                }
            }

            int j = MathHelper.floor(posX);
            int i = MathHelper.floor(posZ);

            if (j != railTracks.getX() || i != railTracks.getZ()) {
                d5 = Math.sqrt(motionX * motionX + motionZ * motionZ);
                motionX = d5 * (double)(j - railTracks.getX());
                motionZ = d5 * (double)(i - railTracks.getZ());
            }
            double d15 = Math.sqrt(motionX * motionX + motionZ * motionZ);
            if(d15 == 0) {
                d15 = 1;
            }
            double d16 = 0.06D;
                motionX += motionX / d15 * d16;
                motionZ += motionZ / d15 * d16;
        }

        public Vec3d getPos() {
            double x = posX;
            double y = posY;
            double z = posZ;

            IBlockState iblockstate = world.getBlockState(new BlockPos(railTracks));

            if (iblockstate.getBlock() instanceof TourRailBlock)
            {
                BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getValue(((TourRailBlock) iblockstate.getBlock()).getShapeProperty());
                int[][] aint = MATRIX[blockrailbase$enumraildirection.getMetadata()];
                double d0 = x + 0.5D + (double)aint[0][0] * 0.5D;
                double d1 = y + 0.0625D + (double)aint[0][1] * 0.5D;
                double d2 = z + 0.5D + (double)aint[0][2] * 0.5D;
                double d3 = x + 0.5D + (double)aint[1][0] * 0.5D;
                double d4 = y + 0.0625D + (double)aint[1][1] * 0.5D;
                double d5 = z + 0.5D + (double)aint[1][2] * 0.5D;
                double d6 = d3 - d0;
                double d7 = (d4 - d1) * 2.0D;
                double d8 = d5 - d2;
                double d9;

                if (d6 == 0.0D) {
                    d9 = z - z;
                } else if (d8 == 0.0D) {
                    d9 = x - x;
                } else {
                    double d10 = x - d0;
                    double d11 = z - d2;
                    d9 = (d10 * d6 + d11 * d8) * 2.0D;
                }

                x = d0 + d6 * d9;
                y = d1 + d7 * d9;
                z = d2 + d8 * d9;

                if (d7 < 0.0D) {
                    ++y;
                }

                if (d7 > 0.0D) {
                    y += 0.5D;
                }

                return new Vec3d(x, y, z);
            } else {
                return null;
            }
        }

        public void moveMinecartOnRail() {
            double mX = motionX;
            double mZ = motionZ;
            if(mX == 0 && mZ == 0 && getControllingPassenger() != null) { //Should only happen when re-logging. //TODO: make a more elegant solution
                mX = getLook(1f).x;
                mZ = getLook(1f).z;
            }

            double max = getSpeed().modifier / 4f;
            mX = MathHelper.clamp(mX, -max, max);
            mZ = MathHelper.clamp(mZ, -max, max);
            FordExplorerEntity.this.move(MoverType.SELF, mX, 0D, mZ);
        }
    }
    /* ================================= MINECART END ========================================*/
}