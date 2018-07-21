package org.jurassicraft.server.plugin.jei.category.embroyonicmachine;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.entity.Dinosaur;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.plant.Plant;

public interface EmbryoInput {
    ItemStack getInputItemStack();

    ItemStack getOutputItemStack();

    Item getPetriDishItem();

    class DinosaurInput implements EmbryoInput {
        public final Dinosaur dinosaur;

        public DinosaurInput(Dinosaur dinosaur) {
            this.dinosaur = dinosaur;
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
        public ItemStack getInputItemStack() {
            return ItemHandler.PLANT_DNA.getItemStack(this.plant);
        }

        @Override
        public ItemStack getOutputItemStack() {
            return ItemHandler.PLANT_CALLUS.getItemStack(this.plant);
        }

        @Override
        public Item getPetriDishItem() {
            return ItemHandler.PLANT_CELLS_PETRI_DISH;
        }
    }
}
