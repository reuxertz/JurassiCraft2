package org.jurassicraft.server.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import org.jurassicraft.server.block.entity.DNACombinatorHybridizerBlockEntity;
import org.jurassicraft.server.container.slot.StorageSlot;
import org.jurassicraft.server.container.slot.StorageSlotOLD;

public class DNACombinatorHybridizerContainer extends MachineContainer {
    private DNACombinatorHybridizerBlockEntity dnaHybridizer;
    private InventoryPlayer playerInventory;

    public DNACombinatorHybridizerContainer(InventoryPlayer playerInventory, DNACombinatorHybridizerBlockEntity tileEntity) {
        super(tileEntity);
        this.dnaHybridizer = tileEntity;
        this.playerInventory = playerInventory;
        this.updateSlots(this.dnaHybridizer.getMode());
    }

    public void updateSlots(boolean mode) {
        this.inventorySlots.clear();
        this.inventoryItemStacks.clear();

        if (mode) {
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer.getInventory(), 0, 10, 17, true));
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer.getInventory(), 1, 30, 17, true));
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer.getInventory(), 2, 50, 17, true));
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer.getInventory(), 3, 70, 17, true));
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer.getInventory(), 4, 90, 17, true));
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer.getInventory(), 5, 110, 17, true));
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer.getInventory(), 6, 130, 17, true));
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer.getInventory(), 7, 150, 17, true));
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer.getInventory(), 10, 80, 56, true));
        } else {
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer.getInventory(), 8, 55, 13, true));
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer.getInventory(), 9, 105, 13, true));
            this.addSlotToContainer(new StorageSlot(this.dnaHybridizer.getInventory(), 11, 81, 60, true));
        }

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(this.playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(this.playerInventory, i, 8 + i * 18, 142));
        }
    }
}
