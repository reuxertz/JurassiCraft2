package org.jurassicraft.server.container.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jurassicraft.server.entity.Dinosaur;
import org.jurassicraft.server.api.DinosaurProvider;
import org.jurassicraft.server.item.SyringeItem;

public class CultivatorSyringeSlot extends SlotItemHandler {
    public CultivatorSyringeSlot(IItemHandler inventory, int slotIndex, int xPosition, int yPosition) {
        super(inventory, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof SyringeItem && DinosaurProvider.getFromStack(stack).getValue(stack).birthType == Dinosaur.BirthType.LIVE_BIRTH;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }
}
