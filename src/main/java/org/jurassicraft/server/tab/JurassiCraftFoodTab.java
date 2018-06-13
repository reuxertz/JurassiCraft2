package org.jurassicraft.server.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.item.ItemHandler;

public class JurassiCraftFoodTab extends CreativeTabs {

    public JurassiCraftFoodTab(String label) {
        super(label);
    }


    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ItemHandler.DINOSAUR_MEAT);
    }
}
