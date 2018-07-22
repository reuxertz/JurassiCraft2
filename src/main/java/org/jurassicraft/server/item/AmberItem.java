package org.jurassicraft.server.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.util.LangHelper;

import java.util.Locale;

public class AmberItem extends Item {
    private final AmberStorageType type;

    AmberItem(AmberStorageType type) {
        this.type = type;
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
