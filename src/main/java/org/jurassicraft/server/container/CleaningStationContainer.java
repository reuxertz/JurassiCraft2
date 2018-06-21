package org.jurassicraft.server.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import org.jurassicraft.server.block.entity.CleaningStationBlockEntity;
import org.jurassicraft.server.container.slot.CleanableItemSlot;
import org.jurassicraft.server.container.slot.FossilSlot;
import org.jurassicraft.server.container.slot.WaterBucketSlot;
import org.jurassicraft.server.container.slot.WaterBucketSlotOLD;

public class CleaningStationContainer extends MachineContainer {
    private final CleaningStationBlockEntity tileEntity;

    public CleaningStationContainer(InventoryPlayer invPlayer, CleaningStationBlockEntity cleaningStation) {
        super(cleaningStation);
        this.tileEntity = cleaningStation;
        this.addSlotToContainer(new CleanableItemSlot(tileEntity.getInventory(), 0, 56, 17));
        this.addSlotToContainer(new WaterBucketSlot(cleaningStation.getInventory(), 1, 56, 53));

        int i;

        for (i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                this.addSlotToContainer(new FossilSlot(cleaningStation.getInventory(), i + (j * 3) + 2, i * 18 + 93 + 15, j * 18 + 26));
            }
        }

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142));
        }
    }

}
