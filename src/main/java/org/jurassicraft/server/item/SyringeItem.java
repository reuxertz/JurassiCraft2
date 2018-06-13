package org.jurassicraft.server.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.util.LangHelper;

import java.util.Locale;

public class SyringeItem extends DNAContainerItem {
    public SyringeItem() {
        super();

        this.setCreativeTab(TabHandler.DNA);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String dinoName = getDinosaur(stack).getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

        return new LangHelper("item.syringe.name").withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
    }

//    @Override
//    public int getContainerId(ItemStack stack) {
//        return EntityHandler.getDinosaurId(getDinosaur(stack));
//    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subtypes) {
        if (this.isInCreativeTab(tab)) {
            subtypes.addAll(this.getAllStacksOrdered());
        }
    }
}