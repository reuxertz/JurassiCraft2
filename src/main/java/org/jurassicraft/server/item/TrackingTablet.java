package org.jurassicraft.server.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
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
import org.jurassicraft.server.api.StackNBTProvider;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.registries.JurassicraftRegisteries;
import org.jurassicraft.server.util.TrackingMapIterator;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

public class TrackingTablet extends Item implements StackNBTProvider<Integer> {

    public static final int MIN_TIER = 1;
    public static final int MAX_TIER = 5;

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        int distnace = new TrackingInfo(playerIn.getHeldItem(handIn)).getDistance();
        if(worldIn.isRemote) {
            this.openGui(playerIn.getPosition(), handIn, distnace);
        } else {
            new TrackingMapIterator(playerIn, distnace).start();
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(player.isSneaking() && !worldIn.isRemote) {
            ItemHandler.TRACKING_TABLET.putValue(player.getHeldItem(hand), ItemHandler.TRACKING_TABLET.getValue(player.getHeldItem(hand)) + 1);
            player.sendMessage(new TextComponentString("Upgraded tier"));
        }
        return EnumActionResult.SUCCESS;
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
    public void openGui(BlockPos pos, EnumHand hand, int distance) {
        Minecraft.getMinecraft().displayGuiScreen(new TrackingTabletGui(pos, hand, distance));
    }


    @Override
    public List<Integer> getAllValues() {
        return IntStream.range(MIN_TIER, MAX_TIER + 1).collect(Lists::newArrayList, List::add, List::addAll);
    }

    @Override
    public String getNameFor(Integer type) {
        return String.valueOf(type);
    }

    @Override
    public Integer getValueFromName(String string) {
        return string.isEmpty() ? 1 : Integer.parseInt(string);
    }

    @Override
    public String getKey() {
        return "tier";
    }

    @Override
    public Map<Object, ResourceLocation> getModelResourceLocations(Integer value) {
        Map<Object, ResourceLocation> map = Maps.newHashMap();
        map.put(getDefault(), new ResourceLocation(JurassiCraft.MODID, "item/tracking_tablet_tier_" + value));
        return map;
    }

    public static class TrackingInfo {
        private final List<DinosaurInfo> dinosaurInfos = Lists.newArrayList();

        private final int tier;

        public TrackingInfo(ItemStack stack){
            for (NBTBase nbtBase : stack.getOrCreateSubCompound("jurassicraft").getCompoundTag("tracking_info").getTagList("tracking_list", Constants.NBT.TAG_COMPOUND)) {
                dinosaurInfos.add(DinosaurInfo.deserializeNBT((NBTTagCompound)nbtBase));
            }
            this.tier = ItemHandler.TRACKING_TABLET.getValue(stack);
        }
        public List<DinosaurInfo> getDinosaurInfos() {
            return dinosaurInfos;
        }

        public NBTTagCompound serialize() {
            NBTTagCompound nbt = new NBTTagCompound();
            NBTTagList list = new NBTTagList();
            for (DinosaurInfo dinosaurInfo : dinosaurInfos) {
                list.appendTag(dinosaurInfo.serializeNBT());
            }
            nbt.setTag("tracking_list", list);
            return nbt;
        }

        public void putInStack(ItemStack stack) {
            stack.getOrCreateSubCompound("jurassicraft").setTag("tracking_info", this.serialize());
        }

        public void update(World world, BlockPos playerPos) { //TODO: maybe dont clear the list and just simply add / remove stuff that dosnt exist
            this.dinosaurInfos.clear();
            int distance = this.getDistance();
            for (DinosaurInfo dinosaurInfo : TrackingSavedData.getData(world).getDinosaurInfos(world)) {
                int disX = Math.abs(dinosaurInfo.pos.getX() - playerPos.getX());
                int disZ = Math.abs(dinosaurInfo.pos.getZ() - playerPos.getZ());
                if(disX > -distance && disX < distance && disZ > -distance && disZ < distance) {
                    this.dinosaurInfos.add(dinosaurInfo);
                }
            }
//            for (DinosaurEntity entity : world.getEntitiesWithinAABB(DinosaurEntity.class, new AxisAlignedBB(playerPos.add(DISTANCE, DISTANCE, DISTANCE), playerPos.add(-DISTANCE, -DISTANCE, -DISTANCE)))) { // DinosaurEntity::hasTracker
//                this.dinosaurInfos.add(DinosaurInfo.fromEntity(entity));
//            }
        }

        public int getDistance() {
            int tier = MathHelper.clamp(this.tier, MIN_TIER, MAX_TIER);
            return 2 << (tier + 6);
        }
    }

    @Mod.EventBusSubscriber(modid = JurassiCraft.MODID)
    public static class TrackingSavedData extends WorldSavedData {

        public static final String ID = JurassiCraft.MODID + "_tracking_saved_data";

        private final Map<Integer, List<DinosaurInfo>> dinosaurInfos = Maps.newHashMap();

        public TrackingSavedData(String name) {
            super(name);
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt) {
            for (int dim : nbt.getIntArray("Dims")) {
                NBTTagList list = nbt.getTagList("List" + dim, Constants.NBT.TAG_COMPOUND);
                for (NBTBase nbtBase : list) {
                    dinosaurInfos.computeIfAbsent(dim, din -> Lists.newArrayList()).add(DinosaurInfo.deserializeNBT((NBTTagCompound)nbtBase));
                }
            }
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound compound) {
            dinosaurInfos.forEach((dim, info) -> {
                NBTTagList list = new NBTTagList();
                for (DinosaurInfo dinosaurInfo : info) {
                    list.appendTag(dinosaurInfo.serializeNBT());
                }
                compound.setTag("List" + dim, list);
            });
            int[] aint = new int[dinosaurInfos.size()];
            int i = 0;
            for (Integer integer : dinosaurInfos.keySet()) {
                aint[i++] = integer;
            }
            compound.setIntArray("Dims", aint);
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
                DinosaurEntity dino = (DinosaurEntity) entity;
                if(dino.hasTracker()) {
                    TrackingSavedData data = getData(world);
                    for (DinosaurInfo dinosaurInfo : data.dinosaurInfos.getOrDefault(world.provider.getDimension(), Lists.newArrayList())) {
                        if(dinosaurInfo.entityUUID.equals(entity.getPersistentID())) {
                            return;
                        }
                    }
                    System.out.println("Added DATA: " + entity.getUniqueID());
                    data.dinosaurInfos.computeIfAbsent(world.provider.getDimension(), dim -> Lists.newArrayList()).add(DinosaurInfo.fromEntity(dino));
                    data.markDirty();
                }
            }
        }

        public List<DinosaurInfo> getDinosaurInfos(World world) {
            return dinosaurInfos.getOrDefault(world.provider.getDimension(), Lists.newArrayList());
        }
    }


    @Value
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
           nbt.setLong("Position", this.pos.toLong());
           nbt.setString("Dinosaur", this.dinosaur.getRegistryName().toString());
           nbt.setBoolean("Male", this.male);
           nbt.setInteger("Growth", this.growthPercentage);
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
