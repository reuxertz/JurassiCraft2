package org.jurassicraft.server.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class CustomSlotOLD extends Slot {
    private Predicate<ItemStack> item;

    public CustomSlotOLD(IInventory inventory, int slotIndex, int xPosition, int yPosition, Predicate<ItemStack> item) {
        super(inventory, slotIndex, xPosition, yPosition);
        this.item = item;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return this.item.test(stack);
    }
}
