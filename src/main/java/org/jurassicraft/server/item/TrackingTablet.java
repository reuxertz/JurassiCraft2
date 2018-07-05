package org.jurassicraft.server.item;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.gui.TrackingTabletGui;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.registries.JurassicraftRegisteries;
import org.jurassicraft.server.util.TrackingMapIterator;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class TrackingTablet extends Item {

    public static final int DISTANCE = 2048; //Maybe have diffrent tiers of tracking tablets with diffrent ranges?

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if(worldIn.isRemote) {
            this.openGui(handIn, null);
        } else {
            new TrackingMapIterator(playerIn).start();
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        ItemStack copyOld = oldStack.copy();
        ItemStack copyNew = newStack.copy();
        copyOld.getOrCreateSubCompound("jurassicraft").removeTag("tracking_info");
        copyNew.getOrCreateSubCompound("jurassicraft").removeTag("tracking_info");
        return !ItemStack.areItemStacksEqual(copyOld, copyNew);
    }

    @SideOnly(Side.CLIENT)
    public void openGui(EnumHand hand, TrackingMapIterator mapIterator) {
        Minecraft.getMinecraft().displayGuiScreen(new TrackingTabletGui(hand, mapIterator));
    }

    public static class TrackingInfo {
        private final List<DinosaurInfo> dinosaurInfos = Lists.newArrayList();

        public TrackingInfo(ItemStack stack){
            this(stack.getOrCreateSubCompound("jurassicraft").getTagList("tracking_info", Constants.NBT.TAG_COMPOUND));
        }

        public TrackingInfo(NBTTagList list) {
            for (NBTBase nbtBase : list) {
                dinosaurInfos.add(DinosaurInfo.deserializeNBT((NBTTagCompound)nbtBase));
            }
        }

        public List<DinosaurInfo> getDinosaurInfos() {
            return dinosaurInfos;
        }

        public NBTTagList serialize() {
            NBTTagList list = new NBTTagList();
            for (DinosaurInfo dinosaurInfo : dinosaurInfos) {
                list.appendTag(dinosaurInfo.serializeNBT());
            }
            return list;
        }

        public void putInStack(ItemStack stack) {
            stack.getOrCreateSubCompound("jurassicraft").setTag("tracking_info", this.serialize());
        }

        public void update(World world, BlockPos playerPos) { //TODO: maybe dont clear the list and just simply add / remove stuff that dosnt exist
            this.dinosaurInfos.clear();
            for (DinosaurInfo dinosaurInfo : TrackingSavedData.getData(world).getDinosaurInfos()) {
                int disX = Math.abs(dinosaurInfo.getPos().getX() - playerPos.getX());
                int disZ = Math.abs(dinosaurInfo.getPos().getZ() - playerPos.getZ());

                if(disX > -DISTANCE && disX < DISTANCE && disZ > -DISTANCE && disZ < DISTANCE) {
                    this.dinosaurInfos.add(dinosaurInfo);
                }
            }
//            for (DinosaurEntity entity : world.getEntitiesWithinAABB(DinosaurEntity.class, new AxisAlignedBB(playerPos.add(DISTANCE, DISTANCE, DISTANCE), playerPos.add(-DISTANCE, -DISTANCE, -DISTANCE)))) { // DinosaurEntity::hasTracker
//                this.dinosaurInfos.add(DinosaurInfo.fromEntity(entity));
//            }
        }
    }

    @Mod.EventBusSubscriber(modid = JurassiCraft.MODID)
    public static class TrackingSavedData extends WorldSavedData {

        public static final String ID = JurassiCraft.MODID + "_tracking_saved_data";

        private final List<DinosaurInfo> dinosaurInfos = Lists.newArrayList();

        public TrackingSavedData(String name) {
            super(name);
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt) {
            NBTTagList list = nbt.getTagList("List", Constants.NBT.TAG_COMPOUND);
            for (NBTBase nbtBase : list) {
                dinosaurInfos.add(DinosaurInfo.deserializeNBT((NBTTagCompound)nbtBase));
            }
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound compound) {
            NBTTagList list = new NBTTagList();
            for (DinosaurInfo dinosaurInfo : dinosaurInfos) {
                list.appendTag(dinosaurInfo.serializeNBT());
            }
            compound.setTag("List", list);
            return compound;
        }

        @Nonnull
        public static TrackingSavedData getData(World world) {
            TrackingSavedData data = (TrackingSavedData)world.loadData(TrackingSavedData.class, ID);
            if(data == null) {
                data = new TrackingSavedData(ID);
                world.setData(ID, data);
            }
            return data;
        }

        @SubscribeEvent
        public static void onEntityTick(LivingEvent.LivingUpdateEvent event) {
            Entity entity = event.getEntity();
            World world = entity.world;
            if(!world.isRemote && entity instanceof DinosaurEntity) {
                TrackingSavedData data = getData(world);
                for (DinosaurInfo dinosaurInfo : data.dinosaurInfos) {
                    if(dinosaurInfo.entityUUID.equals(entity.getPersistentID())) {
                        return;
                    }
                }
                System.out.println("Added DATA: " + entity.getUniqueID());
                data.dinosaurInfos.add(DinosaurInfo.fromEntity((DinosaurEntity)entity));
                data.markDirty();
            }
        }

        public List<DinosaurInfo> getDinosaurInfos() {
            return dinosaurInfos;
        }
    }


    @Data
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class DinosaurInfo {
       BlockPos pos;
       Dinosaur dinosaur;
       boolean male;
       int growthPercentage;
       UUID entityUUID;

       public static DinosaurInfo fromEntity(DinosaurEntity entity) {
           return new DinosaurInfo(entity.getPosition(), entity.getDinosaur(), entity.isMale(), entity.getAgePercentage(), entity.getPersistentID());
       }

        public NBTTagCompound serializeNBT() {
           NBTTagCompound nbt = new NBTTagCompound();
           nbt.setLong("Position", this.getPos().toLong());
           nbt.setString("Dinosaur", this.getDinosaur().getRegistryName().toString());
           nbt.setBoolean("Male", this.isMale());
           nbt.setInteger("Growth", this.getGrowthPercentage());
           nbt.setUniqueId("UUID", this.entityUUID);
           return nbt;
        }

        public static DinosaurInfo deserializeNBT(NBTTagCompound nbt) {
            return new DinosaurInfo(
                    BlockPos.fromLong(nbt.getLong("Position")),
                    JurassicraftRegisteries.DINOSAUR_REGISTRY.getValue(new ResourceLocation(nbt.getString("Dinosaur"))),
                    nbt.getBoolean("Male"),
                    nbt.getInteger("Growth"),
                    nbt.getUniqueId("UUID")
            );
        }
    }
}
