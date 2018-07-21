package org.jurassicraft.server.item;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.jurassicraft.server.api.DinosaurProvider;
import org.jurassicraft.server.api.SequencableItem;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.genetics.DinoDNA;
import org.jurassicraft.server.genetics.GeneticsHelper;
import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.util.LangHelper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SoftTissueItem extends Item implements SequencableItem, DinosaurProvider {
    public SoftTissueItem() {
        this.setCreativeTab(TabHandler.DNA);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String dinoName = this.getValue(stack).name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

        return new LangHelper("item.soft_tissue.name").withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subtypes) {
        if(this.isInCreativeTab(tab)) {
            subtypes.addAll(this.getAllStacksOrdered());
        }
    }

    @Override
    public String getFolderLocation(ResourceLocation res) {
        return "item/soft_tissue/dinosaurs";
    }

    @Override
    public boolean isSequencable(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getSequenceOutput(ItemStack stack, Random random) {
        NBTTagCompound nbt = stack.getOrCreateSubCompound("jurassicraft").getCompoundTag("dna");
        Dinosaur dinosaur = DinosaurProvider.getFromStack(stack).getValue(stack);
        int quality = SequencableItem.randomQuality(random);
        DinoDNA dna = new DinoDNA(dinosaur, quality, GeneticsHelper.randomGenetics(random));
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
        List<Pair<Float, ItemStack>> list = Lists.newArrayList();
        NBTTagCompound nbt = inputItem.getOrCreateSubCompound("jurassicraft").getCompoundTag("dna");
        DinoDNA dna = new DinoDNA(this.getValue(inputItem), -1, "");
        dna.writeToNBT(nbt);
        ItemStack output = new ItemStack(ItemHandler.STORAGE_DISC);
        output.getOrCreateSubCompound("jurassicraft").setTag("dna", nbt);
        list.add(Pair.of(100F, output));
        return list;
    }
}
