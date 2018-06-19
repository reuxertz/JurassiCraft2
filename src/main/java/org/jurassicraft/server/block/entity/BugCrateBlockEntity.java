package org.jurassicraft.server.block.entity;

import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.api.BreedableBug;
import org.jurassicraft.server.container.BugCrateContainer;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BugCrateBlockEntity extends MachineBaseBlockEntity {
    private static final int[] INPUTS = new int[] { 0, 1, 2, 3, 4, 5 };
    private static final int[] OUTPUTS = new int[] { 6, 7, 8 };

    @Override
    protected int getProcessFromSlot(int slot) {
        return 0;
    }

    @Override
    protected boolean canProcess(int process) {
        for (int i = 0; i < 3; i++) {
            ItemStack stack = this.inventory.getStackInSlot(i);
            BreedableBug bug = BreedableBug.getBug(stack);
            if (bug != null && !this.getBestFood(bug).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void processItem(int process) {
        for (int i = 0; i < 3; i++) {
            ItemStack stack = this.inventory.getStackInSlot(i);
            BreedableBug bug = BreedableBug.getBug(stack);
            if (bug != null) {
                ItemStack food = this.getBestFood(bug);
                if (!food.isEmpty()) {
                    ItemStack output = new ItemStack((Item) bug, bug.getBreedings(food));
                    for (int slot = 0; slot < this.inventory.getSlots(); slot++) {
                        if (this.inventory.getStackInSlot(slot) == food) {
                            this.decreaseStackSize(slot);
                            break;
                        }
                    }
                    int outputSlot = this.getOutputSlot(output, 0);
                    if (outputSlot != -1) {
                        this.mergeStack(outputSlot, output);
                    } else {
                        EntityItem item = new EntityItem(this.world, this.pos.getX() + 0.5, this.pos.getY() + 1.0, this.pos.getZ(), output);
                        this.world.spawnEntity(item);
                    }
                    return;
                }
            }
        }
    }

    private ItemStack getBestFood(BreedableBug bug) {
        ItemStack best = ItemStack.EMPTY;
        int highestBreedings = Integer.MIN_VALUE;
        for (int i = 3; i < 6; i++) {
            ItemStack food = this.inventory.getStackInSlot(i);
            if (!food.isEmpty()) {
                int breedings = bug.getBreedings(food);
                if (breedings > 0 && breedings > highestBreedings) {
                    highestBreedings = breedings;
                    best = food;
                }
            }
        }
        return best;
    }

    @Override
    protected int getStackProcessTime(ItemStack stack) {
        BreedableBug bug = BreedableBug.getBug(stack);
        if (bug != null) {
            ItemStack food = this.getBestFood(bug);
            if (food != null) {
                return Math.max(1, bug.getBreedings(food) / 3) * 400;
            }
        }
        return 0;
    }

    @Override
    protected int getProcessCount() {
        return 1;
    }

    @Override
    protected int[] getInputs(int process) {
        return INPUTS;
    }

    @Override
    protected int[] getOutputs(int process) {
        return OUTPUTS;
    }

    @Override
    protected int getInventorySize() {
        return 9;
    }
}
