package org.jurassicraft.server.item;

import java.util.Locale;

import org.jurassicraft.server.api.DinosaurProvider;
import org.jurassicraft.server.entity.Dinosaur;
import org.jurassicraft.server.util.LangHelper;

import net.minecraft.item.ItemStack;

public class DinosaurEggItem extends DNAContainerItem implements DinosaurProvider {//TODO not let direct birth animals
    DinosaurEggItem() {
        this.setMaxStackSize(1);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String dinoName = this.getValue(stack).name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

        return new LangHelper("item.egg.name").withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
    }

    @Override
    public boolean shouldOverrideModel(Dinosaur dinosaur) {
        return dinosaur.birthType == Dinosaur.BirthType.EGG_LAYING;
    }
}
