package org.jurassicraft.server.plugin.jei.category.ingredient;

import net.minecraft.item.Item;
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

public interface SynthesizerInput {
    boolean isValid();

    int getMetadata();

    NBTTagCompound getTag();

    Item getItem();

    class DinosaurInput implements SynthesizerInput {
        public final Dinosaur dinosaur;

        public DinosaurInput(Dinosaur dinosaur) {
            this.dinosaur = dinosaur;
        }

        @Override
        public boolean isValid() {
            return this.dinosaur.shouldRegister();
        }

        @Override
        public int getMetadata() {
            return EntityHandler.getDinosaurId(this.dinosaur);
        }

        @Override
        public NBTTagCompound getTag() {
            DinoDNA dna = new DinoDNA(this.dinosaur, 100, GeneticsHelper.randomGenetics(new Random()));
            NBTTagCompound tag = new NBTTagCompound();
            dna.writeToNBT(tag);
            return tag;
        }

        @Override
        public Item getItem() {
            return ItemHandler.DNA;
        }
    }

    class PlantInput implements SynthesizerInput {
        public final Plant plant;

        public PlantInput(Plant plant) {
            this.plant = plant;
        }

        @Override
        public boolean isValid() {
            return this.plant.shouldRegister();
        }

        @Override
        public int getMetadata() {
            return PlantHandler.getPlantId(this.plant);
        }

        @Override
        public NBTTagCompound getTag() {
            PlantDNA dna = new PlantDNA(this.getMetadata(), 100);
            NBTTagCompound tag = new NBTTagCompound();
            dna.writeToNBT(tag);
            return tag;
        }

        @Override
        public Item getItem() {
            return ItemHandler.PLANT_DNA;
        }
    }
}
