package org.jurassicraft.server.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jurassicraft.server.util.LangHelper;

import java.util.Locale;

public class DNAItem extends DNAContainerItem {

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String dinoName = this.getValue(stack).name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        return new LangHelper("item.dna.name").withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
    }

    @Override
    public String getFolderLocation(ResourceLocation res) {
        return "item/dna/dinosaurs";
    }
}
