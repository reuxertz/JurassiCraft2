package org.jurassicraft.server.entity;

import java.util.Random;

import org.jurassicraft.client.model.animation.EntityAnimation;
import org.jurassicraft.server.entity.ai.DinosaurWanderEntityAI;
import org.jurassicraft.server.entity.ai.util.MathUtils;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class FlyingDinosaurEntity extends DinosaurEntity {
	
    private int ticksOnFloor;
    private int ticksInAir;
    private boolean takingOff;
    
    public FlyingDinosaurEntity(World world) {
        super(world);
        this.moveHelper = new FlyingDinosaurEntity.FlyingMoveHelper();
        this.tasks.addTask(1, new FlyingDinosaurEntity.AIFlyLand()); 
        this.tasks.addTask(2, new FlyingDinosaurEntity.AIStartFlying());
        this.tasks.addTask(2, new FlyingDinosaurEntity.AIRandomFly());
    }
    
    @Override
    public void onEntityUpdate() {
    	setNoGravity(!isOnGround());
    	if(isOnGround()) {
    	    ticksInAir = 0;
    	    ticksOnFloor++;
    	} else {
    	    ticksInAir++;
    	    ticksOnFloor = 0;
    	}
    	
    	
    	if(ticksInAir > 150) {
    	    this.takingOff = false;
    	}
    	super.onEntityUpdate();
    }
    
    @Override
    public EntityAIBase getWanderAI() {
    	return new FlyingDinosaurEntity.AIWander();
    }

    public boolean isOnGround() {
    	return !this.world.getCollisionBoxes(this, this.getEntityBoundingBox().grow(0.3d)).isEmpty() && !takingOff;
    }
    
    public void startTakeOff() {
	takingOff = true;
    }
    
    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
    	if(isOnGround()) {
    		super.moveEntityWithHeading(strafe, forward);
    		return;
    	}
        if (this.inWater()) {
            this.moveRelative(strafe, forward, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        } else if (this.inLava()) {
            this.moveRelative(strafe, forward, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        } else {
            float friction = 0.91F;

            if (this.onGround) {
                friction = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.91F;
            }

            float f3 = 0.16277136F / (friction * friction * friction);
            this.moveRelative(strafe, forward, this.onGround ? f3 * 0.1F : 0.02F);
            friction = 0.91F;

            if (this.onGround) {
                friction = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.91F;
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double) friction;
            this.motionY *= (double) friction;
            this.motionZ *= (double) friction;
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double moveX = this.posX - this.prevPosX;
        double moveZ = this.posZ - this.prevPosZ;
        float dist = MathHelper.sqrt(moveX * moveX + moveZ * moveZ) * 4.0F;

        if (dist > 1.0F) {
            dist = 1.0F;
        }

        this.limbSwingAmount += (dist - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    @Override
    public boolean isOnLadder() {
        return false;
    }
    
    private boolean isCourseTraversable(Vec3d loc) {
    	double distance = this.getPositionVector().distanceTo(loc);
    	distance++;
        double d4 = (loc.x - this.posX) / distance;
        double d5 = (loc.y - this.posY) / distance;
        double d6 = (loc.z - this.posZ) / distance;
        AxisAlignedBB axisalignedbb = this.getCollisionBoundingBox();
        if(axisalignedbb == null) {
        	axisalignedbb = this.getEntityBoundingBox();
        }
        for(int i = 1; (double)i < distance; ++i) {
           axisalignedbb.offset(d4, d5, d6);
           if(!this.world.getCollisionBoxes(this, axisalignedbb).isEmpty()) {
              return false;
           }
        }

        return true;
     }
    
    class AIStartFlying extends EntityAIBase {
	private final FlyingDinosaurEntity dino = FlyingDinosaurEntity.this;

	
	public AIStartFlying() {
            this.setMutexBits(1);
	}
	
	@Override
	public boolean shouldExecute() {
	    return dino.ticksOnFloor >= 150/*TODO: config this ?*/ && dino.isOnGround() && this.dino.rand.nextFloat() < 0.1F; //TODO: config this value
	}
	
	@Override
	public boolean shouldContinueExecuting() {
	    return false;
	}
	
	@Override
	public void startExecuting() {
	    this.dino.startTakeOff();
	    this.dino.setAnimation(EntityAnimation.FLYING_TAKING_OFF.get());
	}
	
	
    }

    class AIRandomFly extends EntityAIBase {
        private FlyingDinosaurEntity dino = FlyingDinosaurEntity.this;

        public AIRandomFly() {
            this.setMutexBits(1);
        }

        @Override
        public boolean shouldExecute() {	
            if(dino.isOnGround()) {
        	return false;
            }
            EntityMoveHelper moveHelper = this.dino.getMoveHelper();
            if (!moveHelper.isUpdating()) {
                return true;
            } else {
                double moveX = moveHelper.getX() - this.dino.posX;
                double moveY = moveHelper.getY() - this.dino.posY;
                double moveZ = moveHelper.getZ() - this.dino.posZ;
                double distance = moveX * moveX + moveY * moveY + moveZ * moveZ;
                return distance < 3.0D || distance > 3600.0D;
            }
        }
        
        @Override
        public boolean shouldContinueExecuting() {
            return false;
        }

        @Override
        public void startExecuting() {//TODO: Fix cosine issues. Not sure why they're happening. Maybe clash with diffrent ai or the task being run multiple times??
            Vec3d lookVec = new Vec3d(dino.getLookVec().x * 10D, dino.getLookVec().y * 10D, dino.getLookVec().z * 10D).add(new Vec3d(getPosition()));
            Random random = this.dino.getRNG();
            for(int i = 0; i < 100; i++) {
            	double destinationX = this.dino.posX + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
                double destinationY = this.dino.posY + (double) ((random.nextFloat() * 2.0F - 1.0F) * 4.0F);
                double destinationZ = this.dino.posZ + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
                destinationY = Math.max(destinationY, dino.world.getTopSolidOrLiquidBlock(new BlockPos(destinationX, 100, destinationZ)).getY() + 15); //Don't allow the entity to fly within 15 blocks of the worlds surface. This way the dinos wont get stuck truing to get to underground blocks. TODO: Make a better system for unsticking dinos
                Vec3d vecPos = new Vec3d(destinationX, destinationY, destinationZ);
                if(dino.isCourseTraversable(vecPos) && Math.abs(MathUtils.cosineFromPoints(vecPos, lookVec, new Vec3d(getPosition()))) < 45D) //TODO: make angle change depending on speed.
                {
                    this.dino.setAnimation(EntityAnimation.FLYING.get());
                    this.dino.getMoveHelper().setMoveTo(destinationX, destinationY, destinationZ, 2D);
                    world.setBlockState(new BlockPos(destinationX, destinationY, destinationZ), Blocks.STONE.getDefaultState());
                    return;
                }
            }
        }
    }
    
    class AIFlyLand extends EntityAIBase {
        private FlyingDinosaurEntity dino = FlyingDinosaurEntity.this;

        public AIFlyLand() {
            this.setMutexBits(1);
        }

        @Override
        public boolean shouldExecute() {
        	if(dino.ticksOnFloor <= 150 && dino.isOnGround()) {
        	    return false;
        	}
        	EntityMoveHelper moveHelper = this.dino.getMoveHelper();
            if (!moveHelper.isUpdating() && dino.rand.nextFloat() < 0.1f) {
                return true;
            } else {
                double moveX = moveHelper.getX() - this.dino.posX;
                double moveY = moveHelper.getY() - this.dino.posY;
                double moveZ = moveHelper.getZ() - this.dino.posZ;
                double distance = moveX * moveX + moveY * moveY + moveZ * moveZ;
                if(distance < 1.0D || distance > 3600.0D) {	
                    return this.dino.world.getBlockState(this.dino.getPosition().down()).getMaterial() == Material.AIR && this.dino.getRNG().nextFloat() < 0.01f;//TODO: change float value
                }
            }
        	 return false;
        }
        
        @Override
        public boolean shouldContinueExecuting() {
            return false;
        }

        @Override
        public void startExecuting() {
            Random random = this.dino.getRNG();
            double destinationX = this.dino.posX + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double destinationZ = this.dino.posZ + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double destinationY = this.dino.world.getTopSolidOrLiquidBlock(new BlockPos(destinationX, 100, destinationZ)).getY();
            if(world.getBlockState(new BlockPos(destinationX, destinationY - 1, destinationZ)).getMaterial() != Material.AIR) {
            	((FlyingMoveHelper)this.dino.getMoveHelper()).setMoveTo(destinationX, destinationY - 1.5D, destinationZ, 2.0D);
            	this.dino.setAnimation(EntityAnimation.FLYING_LANDING.get());
            }
        }
    }

    class FlyingMoveHelper extends EntityMoveHelper {
        private FlyingDinosaurEntity parentEntity = FlyingDinosaurEntity.this;
        private int timer;

        public FlyingMoveHelper() {
            super(FlyingDinosaurEntity.this);
        }


        @Override
        public void onUpdateMoveHelper() {
        	if(parentEntity.isOnGround()) {
        		super.onUpdateMoveHelper();
        		return;
        	}
            if (this.action == EntityMoveHelper.Action.MOVE_TO) {
                double distanceX = this.posX - this.parentEntity.posX;
                double distanceY = this.posY - this.parentEntity.posY;
                double distanceZ = this.posZ - this.parentEntity.posZ;
                double distance = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;

                if (this.timer-- <= 0) {
                    this.timer += this.parentEntity.getRNG().nextInt(5) + 2;
                    distance = (double) MathHelper.sqrt(distance);

                    if (this.parentEntity.isCourseTraversable(new Vec3d(this.posX, this.posY, this.posZ))) {
                        this.parentEntity.motionX += distanceX / distance * this.speed * 0.1D;
                        this.parentEntity.motionY += distanceY / distance * this.speed * 0.1D;
                        this.parentEntity.motionZ += distanceZ / distance * this.speed * 0.1D;
                    } else {
                        this.action = EntityMoveHelper.Action.WAIT;
                    }
                }
            }
        }

        private boolean isNotColliding(double x, double y, double z, double distance) {
            double d0 = (x - this.parentEntity.posX) / distance;
            double d1 = (y - this.parentEntity.posY) / distance;
            double d2 = (z - this.parentEntity.posZ) / distance;
            AxisAlignedBB bounds = this.parentEntity.getEntityBoundingBox();

            for (int i = 1; (double) i < distance; ++i) {
                bounds = bounds.offset(d0, d1, d2);

                if (!this.parentEntity.world.getCollisionBoxes(this.parentEntity, bounds).isEmpty()) {
                    return false;
                }
            }

            return true;
        }
    }

    class AILookAround extends EntityAIBase {
        private FlyingDinosaurEntity dino = FlyingDinosaurEntity.this;

        public AILookAround() {
            this.setMutexBits(2);
        }

        @Override
        public boolean shouldExecute() {
            return true;
        }

        @Override
        public void updateTask() {
            if (this.dino.getAttackTarget() == null) {
                this.dino.renderYawOffset = this.dino.rotationYaw = -((float) Math.atan2(this.dino.motionX, this.dino.motionZ)) * 180.0F / (float) Math.PI;
            } else {
                EntityLivingBase attackTarget = this.dino.getAttackTarget();
                double maxDistance = 64.0D;

                if (attackTarget.getDistanceSqToEntity(this.dino) < maxDistance * maxDistance) {
                    double diffX = attackTarget.posX - this.dino.posX;
                    double diffZ = attackTarget.posZ - this.dino.posZ;
                    this.dino.renderYawOffset = this.dino.rotationYaw = -((float) Math.atan2(diffX, diffZ)) * 180.0F / (float) Math.PI;
                }
            }
        }
    }
    
    class AIWander extends DinosaurWanderEntityAI {

    	private FlyingDinosaurEntity dino = FlyingDinosaurEntity.this;
    	
		public AIWander() {
			super(FlyingDinosaurEntity.this, 0.8D, 10);
		}
		
		@Override
		public boolean shouldExecute()
	    {
			if(!this.dino.isOnGround()) {
				return false;
			}
	        return super.shouldExecute();
	    }
    	
    }
}
