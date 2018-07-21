package org.jurassicraft.server.item;

import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.jurassicraft.server.api.SynthesizableItem;
import org.jurassicraft.server.registries.JurassicraftRegisteries;
import org.jurassicraft.server.genetics.DinoDNA;
import org.jurassicraft.server.genetics.PlantDNA;
import org.jurassicraft.server.genetics.StorageType;
import org.jurassicraft.server.genetics.StorageTypeRegistry;
import org.jurassicraft.server.tab.TabHandler;

import java.util.List;
import java.util.Random;

public class StorageDiscItem extends Item implements SynthesizableItem { //TODO: make a plant storage disc
    public StorageDiscItem() {
        this.setCreativeTab(TabHandler.ITEMS);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> toolTip, ITooltipFlag flagIn)
    {
        NBTTagCompound tag = stack.getOrCreateSubCompound("jurassicraft").getCompoundTag("dna");
        String storageId = tag.getString("StorageId");
        StorageType type = StorageTypeRegistry.getStorageType(storageId);
        if (type != null) {
            type.readFromNBT(tag);
            type.addInformation(stack, toolTip);
        }
    }

    @Override
    public boolean isSynthesizable(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        return tagCompound != null && tagCompound.hasKey("DNAQuality") && tagCompound.getInteger("DNAQuality") == 100;
    }

    @Override
    public ItemStack getSynthesizedItem(ItemStack stack, Random random) {
        NBTTagCompound tag = stack.getOrCreateSubCompound("jurassicraft").getCompoundTag("dna");
        StorageType type = StorageTypeRegistry.getStorageType(tag.getString("StorageId"));
        type.readFromNBT(tag);
        return type.createItem();
    }

    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        NBTTagCompound tag = inputItem.getOrCreateSubCompound("jurassicraft").getCompoundTag("dna");
        StorageType type = StorageTypeRegistry.getStorageType(tag.getString("StorageId"));
        type.readFromNBT(tag);
        return Lists.newArrayList(Pair.of(100F, type.createItem()));
    }

    @Override
    public List<ItemStack> getJEIRecipeTypes() {
        List<ItemStack> list = Lists.newArrayList();

        JurassicraftRegisteries.DINOSAUR_REGISTRY.forEach(dino -> {
            DinoDNA dna = new DinoDNA(dino, -1, "");
            ItemStack stack = new ItemStack(this);
            NBTTagCompound nbt = new NBTTagCompound();
            dna.writeToNBT(nbt);
            stack.getOrCreateSubCompound("jurassicraft").setTag("dna", nbt);
            list.add(stack);
        });

        JurassicraftRegisteries.PLANT_REGISTRY.forEach(plant -> {
            PlantDNA dna = new PlantDNA(plant, -1);
            ItemStack stack = new ItemStack(this);
            NBTTagCompound nbt = new NBTTagCompound();
            dna.writeToNBT(nbt);
            stack.getOrCreateSubCompound("jurassicraft").setTag("dna", nbt);
            list.add(stack);
        });
        return list;
    }
}
