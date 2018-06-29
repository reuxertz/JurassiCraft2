package org.jurassicraft.server.item;

import com.google.common.collect.Lists;
import lombok.Data;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
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

import java.util.List;

public class TrackingTablet extends Item {

    public static final int DISTANCE = 128; //Maybe have diffrent tiers of tracking tablets with diffrent ranges?

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if(worldIn.isRemote) {
            this.openGui(playerIn.getHeldItem(handIn));
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

        @SideOnly(Side.CLIENT)
        public DynamicTexture createDynamicTexture(EntityPlayer player) {
            DynamicTexture texture = new DynamicTexture(128, 128);
            BlockPos playerPos = player.getPosition();
            World world = player.world;
            int scale = DISTANCE / 64;
            int i = 0;
            for (int z = -DISTANCE; z < DISTANCE; z+=scale) {
                for (int x = -DISTANCE; x < DISTANCE; x+=scale) {
                    int r = 0;
                    int g = 0;
                    int b = 0;
                    for (int scaleX = 0; scaleX < scale; scaleX++) {
                        for (int scaleZ = 0; scaleZ < scale; scaleZ++) {
                            int y = 256;
                            while(y >= 0) {
                                BlockPos pos = new BlockPos(playerPos.getX() + x + scaleX, y, playerPos.getZ() + z + scaleZ);
                                IBlockState state = world.getBlockState(pos);
                                int c = state.getMapColor(world, pos).getMapColor(  0);
//                                BlockColors bc = Minecraft.getMinecraft().getBlockColors();
//                                if(state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.LEAVES || state.getBlock() == Blocks.LEAVES2) {
//                                    c = bc.colorMultiplier(state, world, pos, 0);
//                                }
                                if(c != -16777216) {
                                    r += (c >> 16) & 0xFF;
                                    g += (c >> 8) & 0xFF;
                                    b += c & 0xFF;
                                    break;
                                }
                                y--;
                            }
                        }
                    }
                    int s = (scale * scale);
                    texture.getTextureData()[i++] = (((r / s) << 16) | ((g / s) << 8) | (b / s));
                }
            }
            texture.updateDynamicTexture();
            return texture;
        }

    }

    @Data
    public static class DinosaurInfo {
        private final BlockPos pos;
        private final Dinosaur dinosaur;
        private final boolean male;
        private final int growthPercentage;

        public static DinosaurInfo fromEntity(DinosaurEntity entity) {
            return new DinosaurInfo(entity.getPosition(), entity.getDinosaur(), entity.isMale(), entity.getAgePercentage());
        }
    }
}
