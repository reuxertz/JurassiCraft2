package org.jurassicraft.server.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import org.jurassicraft.server.block.entity.DNASequencerBlockEntity;
import org.jurassicraft.server.container.slot.SequencableItemSlot;
import org.jurassicraft.server.container.slot.StorageSlot;

public class DNASequencerContainer extends MachineContainer {
    private DNASequencerBlockEntity dnaSequencer;

    public DNASequencerContainer(InventoryPlayer playerInventory, DNASequencerBlockEntity tileEntity) {
        super(tileEntity);

        this.dnaSequencer = tileEntity;

        this.addSlotToContainer(new SequencableItemSlot(this.inventory, 0, 44, 16));
        this.addSlotToContainer(new StorageSlot(this.inventory, 1, 66, 16, false, 1));
        this.addSlotToContainer(new SequencableItemSlot(this.inventory, 2, 44, 36));
        this.addSlotToContainer(new StorageSlot(this.inventory, 3, 66, 36, false, 1));
        this.addSlotToContainer(new SequencableItemSlot(this.inventory, 4, 44, 56));
        this.addSlotToContainer(new StorageSlot(this.inventory, 5, 66, 56, false, 1));

        this.addSlotToContainer(new StorageSlot(this.inventory, 6, 113, 16, true, 1));
        this.addSlotToContainer(new StorageSlot(this.inventory, 7, 113, 36, true, 1));
        this.addSlotToContainer(new StorageSlot(this.inventory, 8, 113, 56, true, 1));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public DNASequencerBlockEntity getDnaSequencer() {
        return dnaSequencer;
    }
}
