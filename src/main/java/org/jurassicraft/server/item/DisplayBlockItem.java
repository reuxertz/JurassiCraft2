package org.jurassicraft.server.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.api.DinosaurProvider;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.entity.DisplayBlockEntity;
import org.jurassicraft.server.entity.Dinosaur;
import org.jurassicraft.server.registries.JurassicraftRegisteries;
import org.jurassicraft.server.util.LangHelper;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DisplayBlockItem extends Item implements DinosaurProvider {

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        pos = pos.offset(side);
        ItemStack stack = player.getHeldItem(hand);
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack)) {
            Block block = BlockHandler.DISPLAY_BLOCK;

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState state = block.getDefaultState();
                world.setBlockState(pos, block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, 0, player));
                block.onBlockPlacedBy(world, pos, state, player, stack);

                DisplayBlockProperties properties = getProperties(stack);

                DisplayBlockEntity tile = (DisplayBlockEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setDinosaur(properties.getDinosaur(), properties.getType() == DisplayBlockProperties.Type.RANDOM ? world.rand.nextBoolean() : properties.getType() == DisplayBlockProperties.Type.MALE, properties.getType().isSkeleton());
                    tile.setRot(180 - (int) player.getRotationYawHead());
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    if (!player.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                }
            }
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Dinosaur dinosaur = getValue(stack);
        String dinoName = dinosaur.name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        DisplayBlockProperties properties = getProperties(stack);
        if (!properties.getType().isSkeleton()) {
            return new LangHelper("item.action_figure.name").withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
        }
        return new LangHelper("item.skeleton." + (properties.getType() == DisplayBlockProperties.Type.SKELETON ? "fossil" : "fresh") + ".name").withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subtypes) {
        if(this.isInCreativeTab(tab)) {
            List<Dinosaur> list = Lists.newArrayList(JurassicraftRegisteries.DINOSAUR_REGISTRY.getValuesCollection());
            Collections.sort(list);
            for(DisplayBlockProperties.Type type : DisplayBlockProperties.Type.values()) {
                if(type.isCreativeTab()) {
                    for(Dinosaur dino : list) {
                        subtypes.add(writeToStack(new ItemStack(this), new DisplayBlockProperties(dino, type)));
                    }
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> lore, ITooltipFlag tooltipFlag) {
        if (!getProperties(stack).getType().isSkeleton()) {
            lore.add(TextFormatting.BLUE + I18n.format("lore.change_gender.name"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    	ItemStack stack = player.getHeldItem(hand);
    	DisplayBlockProperties properties = getProperties(stack);
        if (!properties.getType().isSkeleton()) {
            properties = properties.cycleIgnore(DisplayBlockProperties.Type.SKELETON, DisplayBlockProperties.Type.SKELETON_FRESH);
            if (world.isRemote) {
                player.sendMessage(new TextComponentString(new LangHelper("actionfigure.genderchange.name").withProperty("mode", I18n.format("gender." + properties.getType().toString().toLowerCase(Locale.ENGLISH) + ".name")).build()));
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    public static DisplayBlockProperties getProperties(ItemStack stack) {
        NBTTagCompound nbt = stack.getOrCreateSubCompound("jurassicraft").getCompoundTag("Display Block");
        return new DisplayBlockProperties(JurassicraftRegisteries.DINOSAUR_REGISTRY.getValue(new ResourceLocation(nbt.getString("Dinosaur"))), DisplayBlockProperties.Type.getEnum(nbt.getString("Type")));
    }

    public static ItemStack writeToStack(ItemStack stack, DisplayBlockProperties properties) {
        NBTTagCompound compound = stack.getOrCreateSubCompound("jurassicraft");
        compound.setTag("Display Block", properties.writeToNBT(compound.getCompoundTag("Display Block")));
        return stack;
    }

    @Override
    public Dinosaur getValue(ItemStack stack) {
        return getProperties(stack).getDinosaur();
    }

    @Override
    public ItemStack getItemStack(Dinosaur dinosaur) {
        return writeToStack(new ItemStack(this), new DisplayBlockProperties(dinosaur, DisplayBlockProperties.Type.RANDOM));
    }

    @Override
    public Map<Object, ResourceLocation> getModelResourceLocations(Dinosaur dinosaur) {
        Map<Object, ResourceLocation> map = Maps.newHashMap();
        for (DisplayBlockProperties.Type type : DisplayBlockProperties.Type.values()) {
            map.put(type, new ResourceLocation(dinosaur.getRegistryName().getResourceDomain(), "item/" + type.modelPrefix + dinosaur.getRegistryName().getResourcePath()));
        }
        return map;
    }

    @Override
    public Object getVarient(ItemStack stack) {
        return getProperties(stack).getType();
    }

    public static class DisplayBlockProperties {
        private final Dinosaur dinosaur;
        private final Type type;

        public DisplayBlockProperties(Dinosaur dinosaur, Type type) {
            this.dinosaur = dinosaur;
            this.type = type;
        }

        public Dinosaur getDinosaur() {
            return dinosaur;
        }

        public Type getType() {
            return type;
        }

        DisplayBlockProperties cycle() {
            return new DisplayBlockProperties(this.dinosaur, Type.values()[(this.type.ordinal() + 1) % Type.values().length]);
        }

        DisplayBlockProperties cycleIgnore(Type... types) {
            List<Type> typeList = Lists.newArrayList(types);
            DisplayBlockProperties ret = this;
            do {
                ret = ret.cycle();
            } while (typeList.contains(ret.type));
            return ret;
        }

        public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
            nbt.setString("Dinosaur", this.dinosaur.getRegistryName().toString());
            nbt.setString("Type", this.type.toString());
            return nbt;
        }

        public enum Type {
            RANDOM("action_figure/", false, true),
            MALE("action_figure/", false, false),
            FEMALE("action_figure/", false, false),
            SKELETON("skeleton/fossil/", true, false),
            SKELETON_FRESH("skeleton/fresh/", true, true);

            private final String modelPrefix;
            private final boolean skeleton;
            private final boolean creativeTab;

            Type(String modelPrefix, boolean skeleton, boolean creativeTab) {
                this.modelPrefix = modelPrefix;
                this.skeleton = skeleton;
                this.creativeTab = creativeTab;
            }

            public boolean isSkeleton() {
                return skeleton;
            }

            public boolean isCreativeTab() {
                return creativeTab;
            }

            public static Type getEnum(String type) {
                try {
                    return valueOf(type);
                } catch (IllegalArgumentException e) {
                    return RANDOM;
                }
            }

        }
    }
}
