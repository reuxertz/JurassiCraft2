package org.jurassicraft.server.container.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jurassicraft.server.api.CleanableItem;

public class CleanableItemSlot extends SlotItemHandler {
    public CleanableItemSlot(IItemHandler inventory, int slotIndex, int xPosition, int yPosition) {
        super(inventory, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        CleanableItem cleanableItem = CleanableItem.getCleanableItem(stack);
        return cleanableItem != null && cleanableItem.isCleanable(stack);
    }
}
