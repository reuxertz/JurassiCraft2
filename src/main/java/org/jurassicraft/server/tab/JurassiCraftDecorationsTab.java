package org.jurassicraft.server.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.item.ItemHandler;

public class JurassiCraftDecorationsTab extends CreativeTabs {

    public JurassiCraftDecorationsTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ItemHandler.DISPLAY_BLOCK);
    }
}
