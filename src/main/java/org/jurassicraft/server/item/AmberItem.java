package org.jurassicraft.server.item;

import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.util.LangHelper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class AmberItem extends Item {
    private final AmberStorageType type;

    public AmberItem(AmberStorageType type) {
        this.type = type;
        this.setCreativeTab(TabHandler.ITEMS);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return new LangHelper("item.amber.name").withProperty("stored", "amber." + type.getName() + ".name").build();
    }

    public enum AmberStorageType {
        MOSQUITO, APHID;

        public String getName() {
            return this.name().toLowerCase(Locale.ENGLISH);
        }
    }
}
