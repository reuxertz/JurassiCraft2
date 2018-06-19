package org.jurassicraft.server.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import org.jurassicraft.server.block.entity.FeederBlockEntity;
import org.jurassicraft.server.container.slot.CustomSlotOLD;
import org.jurassicraft.server.food.FoodHelper;
import org.jurassicraft.server.food.FoodType;

public class FeederContainer extends MachineContainerOLD {
    private FeederBlockEntity tile;

    public FeederContainer(InventoryPlayer inventory, FeederBlockEntity tile) {
        super(tile);

        this.tile = tile;

        int id = 0;

        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(inventory, id, 8 + x * 18, 142));
            id++;
        }

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlotToContainer(new Slot(inventory, id, 8 + x * 18, 84 + y * 18));
                id++;
            }
        }

        id = 0;

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                this.addSlotToContainer(new CustomSlotOLD(tile, id, 26 + x * 18, 18 + y * 18, stack -> FoodHelper.isFoodType(stack.getItem(), FoodType.MEAT) || FoodHelper.isFoodType(stack.getItem(), FoodType.FISH)));
                id++;
            }
        }

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                this.addSlotToContainer(new CustomSlotOLD(tile, id, 98 + x * 18, 18 + y * 18, stack -> FoodHelper.isFoodType(stack.getItem(), FoodType.PLANT)));
                id++;
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tile.isUsableByPlayer(player);
    }
}
