package org.jurassicraft.server.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.registries.JurassicraftRegisteries;

import javax.annotation.Nonnull;

public interface PlantProvider extends RegistryStackNBTProvider<Plant> {

    @Override
    default IForgeRegistry<Plant> getRegistry() {
        return JurassicraftRegisteries.PLANT_REGISTRY;
    }

    @Override
    default String getKey() {
        return "dinosaur";
    }

    @Nonnull
    static PlantProvider getFromStack(ItemStack stack) {
        Item item = stack.getItem();
        if(item instanceof PlantProvider) {
            return (PlantProvider)item;
        } else if((item instanceof ItemBlock && ((ItemBlock)item).getBlock() instanceof PlantProvider)) {
            return (PlantProvider)((ItemBlock)item).getBlock();
        } else {
            return MISSING_PROVIDER;
        }
    }

    default boolean isMissing() {
        return this == MISSING_PROVIDER;
    }

    PlantProvider MISSING_PROVIDER = new PlantProvider() {
        @Override
        public ItemStack getItemStack(Plant dinosaur) {
            return ItemStack.EMPTY;
        }

        @Override
        public Plant getValue(ItemStack stack) {
            return Plant.MISSING;
        }
    };
}