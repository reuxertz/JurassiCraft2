package org.jurassicraft.server.entity.item;

import io.netty.buffer.ByteBuf;
import jline.internal.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import org.jurassicraft.server.entity.Dinosaur;
import org.jurassicraft.server.entity.dinosaur.DinosaurEntity;
import org.jurassicraft.server.registries.JurassicraftRegisteries;
import org.jurassicraft.server.item.ItemHandler;

import java.util.Optional;
import java.util.UUID;

public class DinosaurEggEntity extends Entity implements IEntityAdditionalSpawnData {
    private DinosaurEntity entity;
    private UUID parent;
    private Dinosaur dinosaur;
    private int hatchTime;

    public DinosaurEggEntity(World world, DinosaurEntity entity, DinosaurEntity parent) {
        this(world);
        this.entity = entity;
        this.dinosaur = entity.getDinosaur();
        this.parent = parent.getUniqueID();
    }

    public DinosaurEggEntity(World world) {
        super(world);
        this.setSize(0.3F, 0.5F);
        this.hatchTime = this.random(5000, 6000);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(dinosaur == null) {
            Optional<Entity> parentEntity =  world.loadedEntityList.stream().filter(entity -> entity.getUniqueID().equals(this.parent)).findFirst();
            if(parentEntity.isPresent() && parentEntity.get() instanceof DinosaurEntity) {
                this.dinosaur = ((DinosaurEntity)parentEntity.get()).getDinosaur();
            }
        }

        if (!this.world.isRemote) {
            if (this.entity == null) {
                this.setDead();
            }

            this.hatchTime--;

            if (this.hatchTime <= 0) {
                this.hatch();
            }

            if (!this.onGround) {
                this.motionY -= 0.035D;
            }

            this.motionX *= 0.85;
            this.motionY *= 0.85;
            this.motionZ *= 0.85;

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        }
    }

    @Override
    public void entityInit() {
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (this.entity != null && !this.world.isRemote) {
            ItemStack eggStack = ItemHandler.EGG.getItemStack(this.entity.getDinosaur());
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("DNAQuality", this.entity.getDNAQuality());
            nbt.setString("Genetics", this.entity.getGenetics());
            eggStack.setTagCompound(nbt);
            this.entityDropItem(eggStack, 0.1F);
            this.setDead();
        }
        return true;
    }

    public void hatch() {
        if(dinosaur != null) {
            DinosaurEntity entity = this.dinosaur.createEntity(this.world);
            entity.setPosition(this.posX, this.posY, this.posZ);
            entity.setAge(0);
            this.world.spawnEntity(entity);
            entity.playLivingSound();
            this.setDead();
            for (Entity loadedEntity : this.world.loadedEntityList) {
                if (loadedEntity instanceof DinosaurEntity && loadedEntity.getUniqueID().equals(this.parent)) {
                    DinosaurEntity parent = (DinosaurEntity) loadedEntity;
                    if (parent.family != null && this.dinosaur.defendOffspring) {
                        parent.family.addChild(entity.getUniqueID());
                    }
                    break;
                }
            }
        }
    }

    public int random(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.hatchTime = compound.getInteger("HatchTime");
        NBTTagCompound entityTag = compound.getCompoundTag("Hatchling");
        this.entity = (DinosaurEntity) EntityList.createEntityFromNBT(entityTag, this.world);
        this.parent = compound.getUniqueId("Parent");
        this.dinosaur = JurassicraftRegisteries.DINOSAUR_REGISTRY.getValue(new ResourceLocation(compound.getString("Dinosaur")));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("HatchTime", this.hatchTime);
        if (this.entity != null) {
            compound.setTag("Hatchling", this.entity.serializeNBT());
        }
        compound.setUniqueId("Parent", this.parent);
        compound.setString("Dinosaur", this.dinosaur.getRegistryName().toString());
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        ByteBufUtils.writeRegistryEntry(buffer, this.dinosaur);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        this.dinosaur = ByteBufUtils.readRegistryEntry(additionalData, JurassicraftRegisteries.DINOSAUR_REGISTRY);
    }

    @Nullable
    public Dinosaur getDinosaur() {
        return this.dinosaur;
    }
}