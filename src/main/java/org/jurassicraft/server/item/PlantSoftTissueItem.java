package org.jurassicraft.server.item;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.jurassicraft.server.api.SequencableItem;
import org.jurassicraft.server.genetics.PlantDNA;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.plant.PlantHandler;
import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.util.LangHelper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlantSoftTissueItem extends Item implements SequencableItem {
    public PlantSoftTissueItem() {
        super();
        this.setHasSubtypes(true);
        this.setCreativeTab(TabHandler.PLANTS);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String plantName = this.getPlant(stack).getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

        return new LangHelper("item.plant_soft_tissue.name").withProperty("plant", "plants." + plantName + ".name").build();
    }

    public Plant getPlant(ItemStack stack) {
        Plant plant = PlantHandler.getPlantById(stack.getItemDamage());

        if (plant == null) {
            plant = PlantHandler.SMALL_ROYAL_FERN;
        }

        return plant;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> subtypes) {
        List<Plant> plants = new LinkedList<>(PlantHandler.getPrehistoricPlantsAndTrees());

        Map<Plant, Integer> ids = new HashMap<>();

        for (Plant plant : plants) {
            ids.put(plant, PlantHandler.getPlantId(plant));
        }

        Collections.sort(plants);
        if(this.getCreativeTab().equals(tab))
        for (Plant plant : plants) {
            subtypes.add(new ItemStack(item, 1, ids.get(plant)));
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
            PlantDNA dna = new PlantDNA(stack.getItemDamage(), SequencableItem.randomQuality(random));
            dna.writeToNBT(nbt);
        }

        ItemStack output = new ItemStack(ItemHandler.STORAGE_DISC);
        output.setTagCompound(nbt);

        return output;
    }
}
