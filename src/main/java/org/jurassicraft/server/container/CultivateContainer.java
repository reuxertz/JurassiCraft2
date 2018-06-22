package org.jurassicraft.server.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import org.jurassicraft.server.block.entity.CultivatorBlockEntity;
import org.jurassicraft.server.container.slot.CultivatorSyringeSlot;
import org.jurassicraft.server.container.slot.NutrientSlot;
import org.jurassicraft.server.container.slot.OutputSlot;
import org.jurassicraft.server.container.slot.WaterBucketSlot;

public class CultivateContainer extends MachineContainer {
    private CultivatorBlockEntity cultivator;

    public CultivateContainer(InventoryPlayer playerInventory, CultivatorBlockEntity tileEntity) {
        super(tileEntity);
        this.cultivator = tileEntity;
        this.addSlotToContainer(new CultivatorSyringeSlot(this.cultivator.getInventory(), 0, 122, 44));
        this.addSlotToContainer(new NutrientSlot(this.cultivator.getInventory(), 1, 208, 20));
        this.addSlotToContainer(new WaterBucketSlot(this.cultivator.getInventory(), 2, 12, 20));
        this.addSlotToContainer(new OutputSlot(this.cultivator.getInventory(), 3, 12, 68));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 106 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 164));
        }
    }
}
