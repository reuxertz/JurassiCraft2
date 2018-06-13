package org.jurassicraft.server.plugin.jei.category.embroyonicmachine;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.genetics.DinoDNA;
import org.jurassicraft.server.genetics.GeneticsHelper;
import org.jurassicraft.server.genetics.PlantDNA;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.plant.PlantHandler;

import java.util.Random;

public interface EmbryoInput {
    boolean isValid();

    NBTTagCompound getTag();

    ItemStack getInputItemStack();

    ItemStack getOutputItemStack();

    Item getPetriDishItem();

    class DinosaurInput implements EmbryoInput {
        public final Dinosaur dinosaur;

        public DinosaurInput(Dinosaur dinosaur) {
            this.dinosaur = dinosaur;
        }

        @Override
        public boolean isValid() {
            return true;
        }


        @Override
        public NBTTagCompound getTag() {
            DinoDNA dna = new DinoDNA(this.dinosaur, 100, GeneticsHelper.randomGenetics(new Random()));
            NBTTagCompound tag = new NBTTagCompound();
            dna.writeToNBT(tag);
            return tag;
        }

        @Override
        public ItemStack getInputItemStack() {
            return ItemHandler.DNA.getItemStack(this.dinosaur);
        }

        @Override
        public ItemStack getOutputItemStack() {
            return ItemHandler.SYRINGE.getItemStack(this.dinosaur);
        }

        @Override
        public Item getPetriDishItem() {
            return ItemHandler.PETRI_DISH;
        }
    }

    class PlantInput implements EmbryoInput {
        public final Plant plant;

        public PlantInput(Plant plant) {
            this.plant = plant;
        }

        @Override
        public boolean isValid() {
            return this.plant.shouldRegister();
        }

        @Override
        public NBTTagCompound getTag() {//TODO
            PlantDNA dna = new PlantDNA(plant.getHealAmount(), 100);
            NBTTagCompound tag = new NBTTagCompound();
            dna.writeToNBT(tag);
            return tag;
        }

        @Override
        public ItemStack getInputItemStack() { //TODO:
            return new ItemStack(ItemHandler.PLANT_DNA);
        }

        @Override
        public ItemStack getOutputItemStack() {
            return new ItemStack(ItemHandler.PLANT_CALLUS);
        }

        @Override
        public Item getPetriDishItem() {
            return ItemHandler.PLANT_CELLS_PETRI_DISH;
        }
    }
}
