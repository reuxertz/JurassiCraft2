package org.jurassicraft.server.item;

import net.minecraft.item.ItemStack;
import org.jurassicraft.server.util.LangHelper;

import java.util.Locale;

public class SyringeItem extends DNAContainerItem {
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String dinoName = getValue(stack).getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        return new LangHelper("item.syringe.name").withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
    }

}