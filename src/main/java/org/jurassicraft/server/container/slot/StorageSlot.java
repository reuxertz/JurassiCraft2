package org.jurassicraft.server.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jurassicraft.server.item.ItemHandler;

public class StorageSlot extends SlotItemHandler {

    private final int stackLimit;
    private boolean stored;

    public StorageSlot(IItemHandler itemHandler, int slotIndex, int xPosition, int yPosition, boolean stored) {
        this(itemHandler, slotIndex, xPosition, yPosition, stored, 64);
    }

    public StorageSlot(IItemHandler itemHandler, int slotIndex, int xPosition, int yPosition, boolean stored, int stackLimit) {
        super(itemHandler, slotIndex, xPosition, yPosition);
        this.stored = stored;
        this.stackLimit = stackLimit;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() == ItemHandler.STORAGE_DISC && stack.getOrCreateSubCompound("jurassicraft").hasKey("dna", 10) == stored;
    }
}
