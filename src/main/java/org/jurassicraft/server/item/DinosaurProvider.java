package org.jurassicraft.server.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.registries.JurassicraftRegisteries;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface DinosaurProvider {
    default Dinosaur getDinosaur(ItemStack stack) {
        return JurassicraftRegisteries.DINOSAUR_REGISTRY.getValue(new ResourceLocation(stack.getOrCreateSubCompound("jurassicraft").getString("dinosaur")));
    }

    default ItemStack getItemStack(Dinosaur dinosaur) {
        ItemStack stack;
        if(this instanceof Item) {
            stack = new ItemStack((Item)this);
        } else if(this instanceof Block) {
            stack = new ItemStack((Block)this);
        } else {
            throw new RuntimeException("DinosaurProvider was applied on a non item/block");
        }
        stack.getOrCreateSubCompound("jurassicraft").setString("dinosaur", dinosaur.getRegistryName().toString());
        return stack;
    }

    default List<ItemStack> getAllStacks() {
        return JurassicraftRegisteries.DINOSAUR_REGISTRY.getValuesCollection().stream().map(this::getItemStack).collect(Collectors.toList());
    }

    default List<ItemStack> getAllStacksOrdered() {
        List<Dinosaur> list = JurassicraftRegisteries.DINOSAUR_REGISTRY.getValuesCollection().stream().filter(this::canBeInCreativeTab).collect(Collectors.toList());
        Collections.sort(list);
        return list.stream().map(this::getItemStack).collect(Collectors.toList());
    }

    default Map<String, ResourceLocation> getModelResourceLocations(Dinosaur dinosaur) {
        ResourceLocation res = ((IForgeRegistryEntry.Impl)this).getRegistryName(); //A bit of a hack, but whatever
        ResourceLocation dinoreg = dinosaur.getRegistryName();
        return new HashMap<String, ResourceLocation>(){{
            put("__default", new ResourceLocation(dinoreg.getResourceDomain(), "item/" + res.getResourcePath() + "/" + dinoreg.getResourcePath()));
        }};
    }

    default String getVarient(ItemStack stack) {
        return "__default";
    }

    default boolean shouldOverrideModel(Dinosaur dinosaur) {
        return true;
    }

    default boolean canBeInCreativeTab(Dinosaur dinosaur) {
        return shouldOverrideModel(dinosaur);
    }

    @Nonnull
    static DinosaurProvider getFromStack(ItemStack stack) {
        Item item = stack.getItem();
        if(item instanceof DinosaurProvider) {
            return (DinosaurProvider)item;
        } else if((item instanceof ItemBlock && ((ItemBlock)item).getBlock() instanceof DinosaurProvider)) {
            return (DinosaurProvider)((ItemBlock)item).getBlock();
        } else {
            return MISSING_PROVIDER;
        }
    }

    default boolean isMissing() {
        return this == MISSING_PROVIDER;
    }

    DinosaurProvider MISSING_PROVIDER = new DinosaurProvider() {
        @Override
        public ItemStack getItemStack(Dinosaur dinosaur) {
            return ItemStack.EMPTY;
        }

        @Override
        public Dinosaur getDinosaur(ItemStack stack) {
            return Dinosaur.MISSING;
        }
    };
}
