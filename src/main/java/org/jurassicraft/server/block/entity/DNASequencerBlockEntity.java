package org.jurassicraft.server.block.entity;

import net.minecraft.item.ItemStack;
import org.jurassicraft.server.api.SequencableItem;
import org.jurassicraft.server.item.ItemHandler;

import java.util.Random;

public class DNASequencerBlockEntity extends MachineBaseBlockEntity {
    private static final int[] INPUTS_PROCESS_1 = new int[] { 0, 1 };
    private static final int[] INPUTS_PROCESS_2 = new int[] { 2, 3 };
    private static final int[] INPUTS_PROCESS_3 = new int[] { 4, 5 };

    private static final int[] OUTPUTS = new int[] { 6, 7, 8 };

    @Override
    protected int getProcessFromSlot(int slot) {
        return Math.min(5, (int) Math.floor(slot / 2));
    }

    @Override
    protected boolean canProcess(int process) {
        int tissue = process * 2;

        ItemStack input = this.inventory.getStackInSlot(tissue);
        ItemStack storage = this.inventory.getStackInSlot(tissue + 1);

        SequencableItem sequencableItem = SequencableItem.getSequencableItem(input);

        if (sequencableItem != null && sequencableItem.isSequencable(input)) {
            if (!storage.isEmpty() && storage.getItem() == ItemHandler.STORAGE_DISC) {
                if (this.inventory.getStackInSlot(process + 6).isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected void processItem(int process) {
        Random rand = new Random();

        int tissue = process * 2;

        ItemStack sequencableStack = this.inventory.getStackInSlot(tissue);

        this.mergeStack(process + 6, SequencableItem.getSequencableItem(sequencableStack).getSequenceOutput(sequencableStack, rand));

        this.decreaseStackSize(tissue);
        this.decreaseStackSize(tissue + 1);
    }

    @Override
    protected int getStackProcessTime(ItemStack stack) {
        return 2000;
    }

    @Override
    protected int getProcessCount() {
        return 3;
    }

    @Override
    protected int[] getInputs(int process) {
        if (process == 0) {
            return INPUTS_PROCESS_1;
        } else if (process == 1) {
            return INPUTS_PROCESS_2;
        }

        return INPUTS_PROCESS_3;
    }

    @Override
    protected int[] getOutputs(int process) {
        return new int[]{OUTPUTS[process]};
    }

    @Override
    protected int getInventorySize() {
        return 9;
    }
}
