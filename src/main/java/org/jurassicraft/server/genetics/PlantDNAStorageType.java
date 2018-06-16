package org.jurassicraft.server.genetics;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jurassicraft.server.item.ItemHandler;

import java.util.List;

public class PlantDNAStorageType implements StorageType {
    private PlantDNA dna;

    @Override
    public ItemStack createItem() {
        ItemStack output = new ItemStack(ItemHandler.PLANT_DNA);
        NBTTagCompound compound = new NBTTagCompound();
        this.dna.writeToNBT(compound);
        output.setTagCompound(compound);
        return ItemHandler.PLANT_DNA.putValue(output, this.dna.getPlant());
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        this.dna.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.dna = PlantDNA.readFromNBT(nbt);
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        this.dna.addInformation(stack, tooltip);
    }

}
