package org.jurassicraft.server.item;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.util.LangHelper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DNAItem extends DNAContainerItem {
    public DNAItem() {
        super();
        this.setCreativeTab(TabHandler.DNA);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String dinoName = this.getDinosaur(stack).getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

        return new LangHelper("item.dna.name").withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
    }

//    @Override
//    public int getContainerId(ItemStack stack) {
//        return EntityHandler.getDinosaurId(this.getDinosaur(stack));
//    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subtypes) {
        if(this.isInCreativeTab(tab)) {
            subtypes.addAll(this.getAllStacksOrdered());
        }
    }
}
