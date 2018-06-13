package org.jurassicraft.server.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.item.FossilItem;
import org.jurassicraft.server.item.ItemHandler;

public class JurassiCraftFossilTab extends CreativeTabs {

    public JurassiCraftFossilTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getTabIconItem() {
        return ItemHandler.FOSSIL.createNewStack(new FossilItem.FossilInfomation(EntityHandler.VELOCIRAPTOR, "skull"));
    }
}
