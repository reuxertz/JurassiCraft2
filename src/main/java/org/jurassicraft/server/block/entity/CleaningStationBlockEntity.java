package org.jurassicraft.server.block.entity;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jurassicraft.server.api.CleanableItem;

public class CleaningStationBlockEntity extends MachineBaseBlockEntity {

    private static final int[] INPUTS = {0, 1};
    private static final int[] OUTPUTS = {2, 3, 4, 5, 6, 7};

    private int waterLevel;
    private int startWaterLevel = -1;
    private ItemStack cleanedItemResult = ItemStack.EMPTY;

    @Override
    protected boolean canProcess(int process) {
        ItemStack stack = this.inventory.getStackInSlot(0);
        CleanableItem cleanableItem = CleanableItem.getCleanableItem(stack);
        if (cleanableItem != null && cleanableItem.isCleanable(stack)) {
            if((this.waterLevel != 200 && this.startWaterLevel == -1) || this.waterLevel == 0) {
                return false;
            }
            if(this.startWaterLevel == -1) {
                this.startWaterLevel = this.waterLevel;
            }
            if(this.cleanedItemResult.isEmpty() ) {
                this.cleanedItemResult = cleanableItem.getCleanedItem(stack, this.world.rand);
            }
            for (int i = 2; i < 8; i++) {
                if (isStackable(this.inventory.getStackInSlot(i), this.cleanedItemResult)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void update() {
        super.update();
        if(this.isProcessing(0)) {
            this.waterLevel--;
        } else {
            if(isItemFuel(this.inventory.getStackInSlot(1)) && this.waterLevel != 200) {
                this.waterLevel = 200; //TODO: change water level ?
                this.inventory.setStackInSlot(1, new ItemStack(Items.BUCKET));
            }
        }
    }

    public static boolean isItemFuel(ItemStack stack) {//TODO: capabilities ?
        return !stack.isEmpty() && stack.getItem() == Items.WATER_BUCKET;
    }

    @Override
    protected int getStackProcessTime(ItemStack stack) {
        CleanableItem item = CleanableItem.getCleanableItem(stack);
        return item != null ? item.getCleanTime(stack) : 0;
    }

    @Override
    protected void processItem(int process) {
        for (int i = 2; i < 8; i++) {
            ItemStack slot = this.inventory.getStackInSlot(i);

            if ( isStackable(slot, this.cleanedItemResult) ) {
                if (slot.isEmpty()) {
                    this.inventory.setStackInSlot(i, this.cleanedItemResult.copy());
                } else {
                    slot.grow(this.cleanedItemResult.getCount());
                }
                this.inventory.getStackInSlot(0).shrink(1);
                this.cleanedItemResult = ItemStack.EMPTY;
                this.startWaterLevel = -1;
                this.world.markChunkDirty(this.pos, this);
                break;
            }
        }
    }

    @Override
    protected int getProcessCount() {
        return 1;
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.waterLevel = compound.getInteger("WaterLevel");
        this.startWaterLevel = compound.getInteger("StartWaterLevel");
        this.cleanedItemResult = new ItemStack(compound.getCompoundTag("OutputStack"));
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("WaterLevel", this.waterLevel);
        compound.setInteger("StartWaterLevel", this.startWaterLevel);
        compound.setTag("OutputStack", this.cleanedItemResult.serializeNBT());

        return super.writeToNBT(compound);
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
    protected int getProcessFromSlot(int slot) {
        return 0;
    }

    @Override
    protected int getInventorySize() {
        return 8;
    }
}
