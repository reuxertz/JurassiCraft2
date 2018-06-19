package org.jurassicraft.server.container.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jurassicraft.server.api.SequencableItem;

public class SequencableItemSlot extends SlotItemHandler {
    public SequencableItemSlot(IItemHandler itemHandler, int slotIndex, int xPosition, int yPosition) {
        super(itemHandler, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        SequencableItem sequencableItem = SequencableItem.getSequencableItem(stack);
        return sequencableItem != null && sequencableItem.isSequencable(stack);
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }
}
