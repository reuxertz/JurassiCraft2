package org.jurassicraft.server.item;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.Minecraft;
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
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.gui.TrackingTabletGui;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.registries.JurassicraftRegisteries;
import org.jurassicraft.server.util.TrackingMapUploader;

import java.util.List;

public class TrackingTablet extends Item {

    public static final int DISTANCE = 1024; //Maybe have diffrent tiers of tracking tablets with diffrent ranges?

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if(worldIn.isRemote) {
            this.openGui(playerIn.getHeldItem(handIn));
        } else {
            new TrackingMapUploader(playerIn, playerIn.getPosition());
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
    public void openGui(ItemStack stack) {
        Minecraft.getMinecraft().displayGuiScreen(new TrackingTabletGui(stack));
    }

    public static class TrackingInfo {
        private final List<DinosaurInfo> dinosaurInfos = Lists.newArrayList();

        public TrackingInfo(ItemStack stack){
            this(stack.getOrCreateSubCompound("jurassicraft").getTagList("tracking_info", Constants.NBT.TAG_COMPOUND));
        }

        public TrackingInfo(NBTTagList list) {
            for (NBTBase nbtBase : list) {
                NBTTagCompound compound = (NBTTagCompound)nbtBase;
                dinosaurInfos.add(new DinosaurInfo(
                        BlockPos.fromLong(compound.getLong("Position")),
                        JurassicraftRegisteries.DINOSAUR_REGISTRY.getValue(new ResourceLocation(compound.getString("Dinosaur"))),
                        compound.getBoolean("Male"),
                        compound.getInteger("Growth")
                ));
            }
        }

        public List<DinosaurInfo> getDinosaurInfos() {
            return dinosaurInfos;
        }

        public NBTTagList serialize() {
            NBTTagList list = new NBTTagList();
            for (DinosaurInfo dinosaurInfo : dinosaurInfos) {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setLong("Position", dinosaurInfo.getPos().toLong());
                compound.setString("Dinosaur", dinosaurInfo.getDinosaur().getRegistryName().toString());
                compound.setBoolean("Male", dinosaurInfo.isMale());
                compound.setInteger("Growth", dinosaurInfo.getGrowthPercentage());
                list.appendTag(compound);
            }
            return list;
        }

        public void putInStack(ItemStack stack) {
            stack.getOrCreateSubCompound("jurassicraft").setTag("tracking_info", this.serialize());
        }

        public void update(World world, BlockPos playerPos) { //TODO: maybe dont clear the list and just simply add / remove stuff that dosnt exist
            this.dinosaurInfos.clear();
            for (DinosaurEntity entity : world.getEntitiesWithinAABB(DinosaurEntity.class, new AxisAlignedBB(playerPos.add(DISTANCE, DISTANCE, DISTANCE), playerPos.add(-DISTANCE, -DISTANCE, -DISTANCE)))) { // DinosaurEntity::hasTracker
                this.dinosaurInfos.add(DinosaurInfo.fromEntity(entity));
            }
        }
    }

    @Data
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class DinosaurInfo {
       BlockPos pos;
       Dinosaur dinosaur;
       boolean male;
       int growthPercentage;

        public static DinosaurInfo fromEntity(DinosaurEntity entity) {
            return new DinosaurInfo(entity.getPosition(), entity.getDinosaur(), entity.isMale(), entity.getAgePercentage());
        }
    }
}
