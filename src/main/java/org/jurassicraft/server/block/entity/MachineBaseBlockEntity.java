package org.jurassicraft.server.block.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public abstract class MachineBaseBlockEntity extends TileEntity implements ITickable {

    protected MachineBaseItemHandler inventory = new MachineBaseItemHandler(this, this.getInventorySize());

    protected int[] processTime = new int[this.getProcessCount()];
    protected int[] totalProcessTime = new int[this.getProcessCount()];

    protected int getProcessCount() {
        return 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        for (int i = 0; i < this.getProcessCount(); i++) {
            this.processTime[i] = compound.getShort("ProcessTime" + i);
            this.totalProcessTime[i] = compound.getShort("ProcessTimeTotal" + i);
        }
        this.inventory.deserializeNBT(compound.getCompoundTag("Inventory"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        for (int i = 0; i < this.getProcessCount(); i++) {
            compound.setShort("ProcessTime" + i, (short) this.processTime[i]);
            compound.setShort("ProcessTimeTotal" + i, (short) this.totalProcessTime[i]);
        }
        compound.setTag("Inventory", this.inventory.serializeNBT());
        return compound;
    }

    @Override
    public void update() {
        for (int process = 0; process < this.getProcessCount(); process++) {
            boolean flag = this.isProcessing(process);
            boolean dirty = false;

            boolean hasInput = false;

            for (int input : this.getInputs(process)) {
                if (!inventory.getStackInSlot(input).isEmpty()) {
                    hasInput = true;
                    break;
                }
            }

            if (hasInput && this.canProcess(process)) {
                this.processTime[process]++;

                if (this.processTime[process] >= this.totalProcessTime[process]) {
                    this.processTime[process] = 0;
                    int total = 0;
                    int input = 0;
                    for (int i : this.getInputs(process)) {
                        ItemStack stack = this.inventory.getStackInSlot(i);
                        if (!stack.isEmpty()) {
                            total = this.getStackProcessTime(stack);
                            input = i;
                            break;
                        }
                    }
                    this.totalProcessTime[process] = total;
                    this.processItem(process);
                    if(input != 0) {
                        this.onSlotUpdate(input);
                    }
                }

                dirty = true;
            } else if (this.isProcessing(process)) {
                if (this.shouldResetProgress(process)) {
                    this.processTime[process] = 0;
                } else if (this.processTime[process] > 0) {
                    this.processTime[process]--;
                }

                dirty = true;
            }

            if (flag != this.isProcessing(process)) {
                dirty = true;
            }

            if (dirty && !this.world.isRemote) {
                this.markDirty();
            }
        }
    }

    protected void mergeStack(int slot, ItemStack stack) {
        ItemStack previous = this.inventory.getStackInSlot(slot);
        if (previous.isEmpty()) {
            this.inventory.setStackInSlot(slot, stack);
        } else if (ItemStack.areItemsEqual(previous, stack) && ItemStack.areItemStackTagsEqual(previous, stack)) {
            previous.grow(stack.getCount());
        }
    }

    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true;
    }

    protected void decreaseStackSize(int slot) {

        this.inventory.getStackInSlot(slot).shrink(1);

        if (this.inventory.getStackInSlot(slot).getCount() <= 0) {
            this.inventory.setStackInSlot(slot, ItemStack.EMPTY);
        }
    }

    public int getField(int id) {
        int processCount = this.getProcessCount();
        if (id < processCount) {
            return this.processTime[id];
        } else if (id < processCount * 2) {
            return this.totalProcessTime[id - processCount];
        }

        return 0;
    }

    public void setField(int id, int value) {
        int processCount = this.getProcessCount();

        if (id < processCount) {
            this.processTime[id] = value;
        } else if (id < processCount * 2) {
            this.totalProcessTime[id - processCount] = value;
        }
    }

    public int getFieldCount() {
        return this.getProcessCount() * 2;
    }

    public int getOutputSlot(ItemStack output, int process) {
        int[] outputs = this.getOutputs(process);
        for (int slot : outputs) {
            ItemStack stack = this.inventory.getStackInSlot(slot);
            if (stack.isEmpty() || ((ItemStack.areItemStackTagsEqual(stack, output) && stack.getCount() + output.getCount() <= stack.getMaxStackSize()) && stack.getItem() == output.getItem() && stack.getItemDamage() == output.getItemDamage())) {
                return slot;
            }
        }
        return -1;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

    public MachineBaseItemHandler getInventory() {
        return inventory;
    }

    public boolean isProcessing(int index) {
        return this.processTime[index] > 0;
    }

    protected abstract int[] getInputs(int process);

    protected abstract int[] getOutputs(int process);

    protected abstract int getInventorySize();

    protected boolean canProcess(int process) {
        return true;
    }

    protected abstract int getProcessFromSlot(int slot);

    protected int getStackProcessTime(ItemStack stack) {
        return 0;
    }

    protected void processItem(int process) {
    }


    protected boolean shouldResetProgress(int process) {
        return true;
    }

    protected void onSlotUpdate(int slot) {

    }
}
