package org.jurassicraft.server.block.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class MachineBaseItemHandler extends ItemStackHandler {

    private final MachineBaseBlockEntity blockEntity;

    public MachineBaseItemHandler(MachineBaseBlockEntity blockEntity, int size) {
        super(size);
        this.blockEntity = blockEntity;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(!blockEntity.isItemValidForSlot(slot, stack)) {
            return stack;
        }
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    protected void onContentsChanged(int slot) {
        ItemStack stack = this.getStackInSlot(slot);
        int process = blockEntity.getProcessFromSlot(slot);
        loop:
        {
            for (int i : blockEntity.getInputs(process)) {
                if(i == slot) {
                    break loop;
                }
            }
            return;
        }
        if (!blockEntity.isProcessing(process) && blockEntity.canProcess(process) && process >= 0 && process < blockEntity.getProcessCount()) {
            blockEntity.totalProcessTime[process] = blockEntity.getStackProcessTime(stack);
            if (!blockEntity.canProcess(process)) {
                blockEntity.processTime[process] = 0;
            }
            blockEntity.markDirty();
        }
        super.onContentsChanged(slot);
    }
}
