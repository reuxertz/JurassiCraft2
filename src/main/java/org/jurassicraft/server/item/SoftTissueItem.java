package org.jurassicraft.server.item;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.jurassicraft.server.api.SequencableItem;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
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

public class SoftTissueItem extends Item implements SequencableItem, DinosaurProvider{
    public SoftTissueItem() {
        this.setHasSubtypes(true);

        this.setCreativeTab(TabHandler.DNA);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String dinoName = this.getDinosaur(stack).getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

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
    public boolean isSequencable(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getSequenceOutput(ItemStack stack, Random random) {
        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt == null) {
            nbt = new NBTTagCompound();
            int quality = SequencableItem.randomQuality(random);
            DinoDNA dna = new DinoDNA(DinosaurProvider.getFromStack(stack).getDinosaur(stack), quality, GeneticsHelper.randomGenetics(random));
            dna.writeToNBT(nbt);
        } else if (!nbt.hasKey("Dinosaur")) {
            nbt.setInteger("Dinosaur", stack.getItemDamage());
        }

        ItemStack output = new ItemStack(ItemHandler.STORAGE_DISC);
        output.setTagCompound(nbt);

        return output;
    }

    @Override
    public List<ItemStack> getJEIRecipeTypes() {
        return getItemSubtypes(this);
    }

    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        List<Pair<Float, ItemStack>> list = Lists.newArrayList();
        NBTTagCompound nbt = new NBTTagCompound();
        DinoDNA dna = new DinoDNA(DinosaurProvider.getFromStack(inputItem).getDinosaur(inputItem), -1, "");
        dna.writeToNBT(nbt);
        ItemStack output = new ItemStack(ItemHandler.STORAGE_DISC);
        output.setTagCompound(nbt);
        list.add(Pair.of(100F, output));
        return list;
    }
}
