package org.jurassicraft.server.entity.dinosaur;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jurassicraft.client.model.animation.EntityAnimation;
import org.jurassicraft.client.sound.SoundHandler;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.entity.GoatEntity;
import org.jurassicraft.server.entity.LegSolverBiped;

@Deprecated
public class TyrannosaurusEntity extends DinosaurEntity {
    private int stepCount = 0;

    public LegSolverBiped legSolver;

    public TyrannosaurusEntity(World world) {
        super(world);
        this.target(new DinosaurClassAttackPredicate(GoatEntity.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class, EntityMob.class),
                new DinosaurAttackPredicate(EntityHandler.DILOPHOSAURUS, EntityHandler.GALLIMIMUS, EntityHandler.TRICERATOPS, EntityHandler.PARASAUROLOPHUS, EntityHandler.VELOCIRAPTOR, EntityHandler.MUSSAURUS));
    }

    @Override
    protected LegSolverBiped createLegSolver() {
        return this.legSolver = new LegSolverBiped(-0.5F, 1.0F, 1.0F);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.TYRANNOSAURUS_LIVING;
            case CALLING:
                return SoundHandler.TYRANNOSAURUS_CALL;
            case ROARING:
                return SoundHandler.TYRANNOSAURUS_CALL;
            case DYING:
                return SoundHandler.TYRANNOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.TYRANNOSAURUS_HURT;
            default:
                return null;
        }
    }
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
    }



    @Override
    public SoundEvent getBreathingSound() {
        return SoundHandler.TYRANNOSAURUS_BREATHING;
    }
   
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.onGround && !this.isInWater()) {
            if (this.moveForward > 0 && (this.posX - this.prevPosX > 0 || this.posZ - this.prevPosZ > 0) && this.stepCount <= 0) {
                this.playSound(SoundHandler.STOMP, (float) this.interpolate(0.1F, 1.0F), this.getSoundPitch());
                this.stepCount = 65;
            }
            this.stepCount -= this.moveForward * 9.5;
        }
    }
}
