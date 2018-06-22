package org.jurassicraft.server.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jurassicraft.server.block.entity.CleaningStationBlockEntity;

public class WaterBucketSlotOLD extends SlotItemHandler {
    public WaterBucketSlotOLD(IItemHandler inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return CleaningStationBlockEntity.isItemFuel(stack);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }
}
