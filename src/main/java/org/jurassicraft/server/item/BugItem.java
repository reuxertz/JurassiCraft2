package org.jurassicraft.server.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.api.BreedableBug;
import org.jurassicraft.server.tab.TabHandler;

import java.util.function.Function;

public class BugItem extends Item implements BreedableBug {
    private Function<ItemStack, Integer> breedings;

    public BugItem(Function<ItemStack, Integer> breedings) {
        super();
        this.breedings = breedings;
    }

    @Override
    public int getBreedings(ItemStack stack) {
        return this.breedings.apply(stack);
    }
}
