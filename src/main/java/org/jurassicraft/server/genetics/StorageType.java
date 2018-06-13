package org.jurassicraft.server.genetics;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jurassicraft.server.dinosaur.Dinosaur;

import java.util.List;

public interface StorageType {
    ItemStack createItem();

    void writeToNBT(NBTTagCompound nbt);

    void readFromNBT(NBTTagCompound nbt);

    void addInformation(ItemStack stack, List<String> tooltip);
}
