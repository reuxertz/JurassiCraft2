package org.jurassicraft.server.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.api.DinosaurProvider;
import org.jurassicraft.server.entity.Dinosaur.BirthType;
import org.jurassicraft.server.item.SyringeItem;

public class SyringeSlot extends Slot {
    public SyringeSlot(IInventory inventory, int slotIndex, int xPosition, int yPosition) {
        super(inventory, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof SyringeItem && DinosaurProvider.getFromStack(stack).getValue(stack).birthType == BirthType.EGG_LAYING;
    }
}
