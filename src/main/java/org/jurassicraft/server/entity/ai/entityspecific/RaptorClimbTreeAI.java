package org.jurassicraft.server.entity.ai.entityspecific;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.jurassicraft.client.model.animation.EntityAnimation;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.ai.core.Mutex;

import java.util.Random;

public class RaptorClimbTreeAI extends EntityAIBase
{
    private static final int CLIMB_INTERVAL = 1200;
    private static final int MAX_TREE_HEIGHT = 14;

    private final DinosaurEntity entity;
    private final double movementSpeed;
    private final World world;

    private Path path;

    private BlockPos targetTrunk;
    private EnumFacing approachSide;

    private double targetX;
    private double targetY;
    private double targetZ;

    private boolean gliding;
    private boolean active;
    private boolean reachedTarget;

    private int lastActive = -CLIMB_INTERVAL;

    public RaptorClimbTreeAI(DinosaurEntity entity, double speed)
    {
        this.entity = entity;
        this.movementSpeed = speed;
        this.world = entity.world;
        this.setMutexBits(Mutex.MOVEMENT | Mutex.ANIMATION);
    }

    @Override
    public boolean shouldExecute()
    {
        if (this.active || (this.entity.ticksExisted - this.lastActive) < CLIMB_INTERVAL)
        {
            return false;
        }
        BlockPos pos = new BlockPos(this.entity.posX, this.entity.getEntityBoundingBox().minY, this.entity.posZ);
        Random rand = this.entity.getRNG();
        for (int i = 0; i < 2; ++i)
        {
            BlockPos target = pos.add(rand.nextInt(7) - rand.nextInt(7), -5, rand.nextInt(7) - rand.nextInt(7));
            for (int iteration = 0; iteration <= 15; iteration++)
            {
                target = target.up();
                IBlockState state = this.world.getBlockState(target);
                IBlockState ground = this.world.getBlockState(target.down());
                if (state.getBlock().isWood(this.world, target) && !ground.getBlock().isWood(this.world, target.down()))
                {
                    for (EnumFacing direction : EnumFacing.HORIZONTALS)
                    {
                        BlockPos offsetTarget = target.offset(direction);
                        if (!this.world.isSideSolid(offsetTarget, EnumFacing.DOWN))
                        {
                            boolean canTravel = true;
                            int height = 0;
                            for (; height < MAX_TREE_HEIGHT; height++)
                            {
                                BlockPos trunkPos = target.up(height);
                                BlockPos climbPos = offsetTarget.up(height);
                                IBlockState climbState = this.world.getBlockState(climbPos);
                                if (!climbState.getBlock().isAir(climbState, this.world, climbPos) && !climbState.getBlock().isLeaves(climbState, this.world, climbPos))
                                {
                                    canTravel = false;
                                    break;
                                }
                                IBlockState trunkState = this.world.getBlockState(trunkPos);
                                if (!trunkState.getBlock().isWood(this.world, trunkPos))
                                {
                                    break;
                                }
                            }
                            if (canTravel)
                            {
                                float offsetX = direction.getFrontOffsetX() * (this.entity.width + 0.25F);
                                float offsetZ = direction.getFrontOffsetZ() * (this.entity.width + 0.25F);
                                this.targetTrunk = target;
                                this.targetX = target.getX() + 0.5F + offsetX;
                                this.targetY = target.getY();
                                this.targetZ = target.getZ() + 0.5F + offsetZ;
                                this.approachSide = direction;
                                AxisAlignedBB bounds = this.getBoundsAtPos(this.targetX, this.targetY, this.targetZ);
                                if (!this.entity.world.collidesWithAnyBlock(bounds))
                                {
                                    this.path = this.entity.getNavigator().getPathToXYZ(this.targetX, this.targetY, this.targetZ);
                                    if (this.path != null)
                                    {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                } else if (state.getBlock().isAir(state, this.world, target))
                {
                    break;
                }
            }
        }
        return false;
    }

    private AxisAlignedBB getBoundsAtPos(double targetX, double targetY, double targetZ)
    {
        float widthOffset = this.entity.width / 2.0F;
        return new AxisAlignedBB(targetX - widthOffset, targetY, targetZ - widthOffset, targetX + widthOffset, targetY + this.entity.height, targetZ + widthOffset);
    }

    @Override
    public void startExecuting()
    {
        this.entity.getNavigator().setPath(this.path, this.movementSpeed);
        this.active = true;
        this.gliding = false;
    }

    @Override
    public void updateTask()
    {
        if (this.reachedTarget)
        {
            BlockPos currentTrunk = new BlockPos(this.targetTrunk.getX(), this.entity.getEntityBoundingBox().minY, this.targetTrunk.getZ());
            if (!this.gliding && this.world.isAirBlock(currentTrunk.up()))
            {
                Random random = this.entity.getRNG();
                this.entity.addVelocity(random.nextFloat() - 0.5 * 0.2, random.nextFloat() * 0.2 + 0.1, random.nextFloat() - 0.5 * 0.2);
                this.gliding = true;
                this.active = false;
                this.entity.setAnimation(EntityAnimation.GLIDING.get());
            } else
            {
                this.entity.getMoveHelper().setMoveTo(this.targetX, this.entity.getEntityBoundingBox().minY, this.targetZ, this.movementSpeed);
                this.entity.setAnimation(EntityAnimation.CLIMBING.get());
                if (this.entity.isCollidedHorizontally || this.world.isSideSolid(currentTrunk, this.approachSide))
                {
                    this.entity.motionY = 0.2;
                    this.entity.setPosition(this.targetX, this.entity.posY, this.targetZ);
                    if (this.entity.getDistanceSqToCenter(currentTrunk) > 2.0)
                    {
                        this.active = false;
                    }
                }
                if (this.entity.isCollidedVertically && !this.gliding)
                {
                    BlockPos top = new BlockPos(this.entity.posX, this.entity.getEntityBoundingBox().maxY + 0.1, this.entity.posZ);
                    if (this.isBlockLeaves(top))
                    {
                        if (this.world.isAirBlock(top.up()))
                        {
                            this.entity.setPosition(this.targetX, MathHelper.ceil(this.entity.posY + 1) + 0.2, this.targetZ);
                        } else
                        {
                            this.entity.setPosition(this.targetX, this.entity.posY + 0.2, this.targetZ);
                        }
                    }
                }
                this.entity.onGround = true;
            }
        } else
        {
            if (this.path.isFinished())
            {
                this.entity.setAnimation(EntityAnimation.START_CLIMBING.get());
                this.reachedTarget = true;
                Vec3d origin = this.entity.getPositionVector().addVector(0.0, this.entity.getEyeHeight(), 0.0);
                RayTraceResult traceResult = this.world.rayTraceBlocks(origin, new Vec3d(this.targetX, this.targetY, this.targetZ), false, true, false);
                if (traceResult != null && traceResult.typeOfHit == RayTraceResult.Type.BLOCK)
                {
                    this.path = null;
                }
            }
        }
    }

    private boolean isBlockLeaves(BlockPos pos)
    {
        IBlockState state = this.world.getBlockState(pos);
        return state.getBlock().isLeaves(state, this.world, pos);
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        if (this.path == null || (this.world.collidesWithAnyBlock(this.entity.getEntityBoundingBox()) && !this.isBlockLeaves(this.entity.getPosition())))
        {
            return false;
        }
        if (!this.reachedTarget)
        {
            if (!this.path.isFinished() && !this.path.isSamePath(this.entity.getNavigator().getPath()))
            {
                return false;
            }
        }
        return this.active && !this.gliding;
    }

    @Override
    public void resetTask()
    {
        this.lastActive = this.entity.ticksExisted;
        this.path = null;
        this.targetTrunk = null;
        this.active = false;
        this.reachedTarget = false;
    }
}
