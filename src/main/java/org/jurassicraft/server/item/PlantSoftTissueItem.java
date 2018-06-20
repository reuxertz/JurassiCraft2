package org.jurassicraft.server.item;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.jurassicraft.server.api.PlantProvider;
import org.jurassicraft.server.api.SequencableItem;
import org.jurassicraft.server.genetics.PlantDNA;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.util.LangHelper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public class PlantSoftTissueItem extends Item implements SequencableItem, PlantProvider {
    public PlantSoftTissueItem() {
        super();
        this.setHasSubtypes(true);
        this.setCreativeTab(TabHandler.PLANTS);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        ResourceLocation registryName = this.getValue(stack).getRegistryName();
        return new LangHelper("item.plant_soft_tissue.name").withProperty("plant", "plants." + registryName.getResourceDomain() + "." + registryName.getResourcePath() + ".name").build();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if(this.isInCreativeTab(tab)) {
            subItems.addAll(this.getAllStacksOrdered());
        }
    }

    @Override
    public boolean shouldOverrideModel(Plant value) {
        return value.isPrehistoric();
    }

    @Override
    public String getFolderLocation(ResourceLocation res) {
        return "item/soft_tissue/plants";
    }

    @Override
    public boolean isSequencable(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getSequenceOutput(ItemStack stack, Random random) {
        NBTTagCompound nbt = stack.getOrCreateSubCompound("jurassicraft").getCompoundTag("dna");
        PlantDNA dna = new PlantDNA(this.getValue(stack), SequencableItem.randomQuality(random));
        dna.writeToNBT(nbt);
        ItemStack output = new ItemStack(ItemHandler.STORAGE_DISC);
        output.getOrCreateSubCompound("jurassicraft").setTag("dna", nbt);
        return output;
    }

    @Override
    public List<ItemStack> getJEIRecipeTypes() {
        return getItemSubtypes(this);
    }

    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        NBTTagCompound nbt = inputItem.getOrCreateSubCompound("jurassicraft").getCompoundTag("dna");
        PlantDNA dna = new PlantDNA(this.getValue(inputItem), -1);
        dna.writeToNBT(nbt);
        ItemStack output = new ItemStack(ItemHandler.STORAGE_DISC);
        output.getOrCreateSubCompound("jurassicraft").setTag("dna", nbt);
        return Lists.newArrayList(Pair.of(100F, output));
    }
}
