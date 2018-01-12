package org.jurassicraft.server.block.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class MachineBaseBlockEntity extends TileEntityLockable implements ITickable, ISidedInventory {
    protected String customName;

    protected int[] processTime = new int[this.getProcessCount()];
    protected int[] totalProcessTime = new int[this.getProcessCount()];

    @SideOnly(Side.CLIENT)
    public static boolean isProcessing(IInventory inventory, int index) {
        return inventory.getField(index) > 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        NBTTagList itemList = compound.getTagList("Items", 10);
        ItemStack[] slots = new ItemStack[this.getSlots().length];

        for (int i = 0; i < itemList.tagCount(); ++i) {
            NBTTagCompound item = itemList.getCompoundTagAt(i);
            ItemStack stack = new ItemStack(item);

            byte slot = item.getByte("Slot");

            if (slot >= 0 && slot < slots.length) {
                slots[slot] = stack;
            }
        }

        for (int i = 0; i < this.getProcessCount(); i++) {
            this.processTime[i] = compound.getShort("ProcessTime" + i);
            this.totalProcessTime[i] = compound.getShort("ProcessTimeTotal" + i);
        }

        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }

        this.setSlots(slots);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        for (int i = 0; i < this.getProcessCount(); i++) {
            compound.setShort("ProcessTime" + i, (short) this.processTime[i]);
            compound.setShort("ProcessTimeTotal" + i, (short) this.totalProcessTime[i]);
        }

        ItemStack[] slots = this.getSlots();

        NBTTagList itemList = new NBTTagList();

        for (int slot = 0; slot < this.getSizeInventory(); ++slot) {
            if (slots[slot] != null) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("Slot", (byte) slot);

                slots[slot].writeToNBT(itemTag);
                itemList.appendTag(itemTag);
            }
        }

        compound.setTag("Items", itemList);

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.getSlots()[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack split = ItemStackHelper.getAndSplit(this.getSlots(), index, count);
        this.onSlotUpdate();
        return split;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack removed = ItemStackHelper.getAndRemove(this.getSlots(), index);
        this.onSlotUpdate();
        return removed;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack[] slots = this.getSlots();

        boolean stacksEqual = stack != null && stack.isItemEqual(slots[index]) && ItemStack.areItemStackTagsEqual(stack, slots[index]);
        slots[index] = stack;

        if (stack != null && stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (!stacksEqual) {
            int process = this.getProcess(index);
            if (process >= 0 && process < this.getProcessCount()) {
                this.totalProcessTime[process] = this.getStackProcessTime(stack);
                if (!this.canProcess(process)) {
                    this.processTime[process] = 0;
                }
                this.markDirty();
            }
            this.onSlotUpdate();
        }
    }

    private boolean isInput(int slot) {
        int[] inputs = this.getInputs();

        for (int input : inputs) {
            if (input == slot) {
                return true;
            }
        }

        return false;
    }

    private boolean isOutput(int slot) {
        int[] outputs = this.getOutputs();

        for (int output : outputs) {
            if (output == slot) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && this.customName.length() > 0;
    }

    public void setCustomInventoryName(String customName) {
        this.customName = customName;
    }

    @Override
    public int getSizeInventory() {
        return this.getSlots().length;
    }

    public boolean isProcessing(int index) {
        return this.processTime[index] > 0;
    }

    @Override
    public void update() {
        ItemStack[] slots = this.getSlots();

        for (int process = 0; process < this.getProcessCount(); process++) {
            boolean flag = this.isProcessing(process);
            boolean dirty = false;

            if (!this.world.isRemote) {
                boolean hasInput = false;

                for (int input : this.getInputs(process)) {
                    if (slots[input] != null) {
                        hasInput = true;
                        break;
                    }
                }

                if (hasInput && this.canProcess(process)) {
                    this.processTime[process]++;

                    if (this.processTime[process] >= this.totalProcessTime[process]) {
                        this.processTime[process] = 0;
                        int total = 0;
                        for (int input : this.getInputs()) {
                            ItemStack stack = slots[input];
                            if (stack != null) {
                                total = this.getStackProcessTime(stack);
                                break;
                            }
                        }
                        this.totalProcessTime[process] = total;
                        this.processItem(process);
                        this.onSlotUpdate();
                    }

                    dirty = true;
                } else if (this.isProcessing(process)) {
                    if (this.shouldResetProgress()) {
                        this.processTime[process] = 0;
                    } else if (this.processTime[process] > 0) {
                        this.processTime[process]--;
                    }

                    dirty = true;
                }

                if (flag != this.isProcessing(process)) {
                    dirty = true;
                }

                if (dirty) {
                    this.markDirty();
                }
            }
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return !this.isOutput(index);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.DOWN ? this.getOutputs() : this.getInputs();
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
        return this.isItemValidForSlot(index, stack);
    }

    protected abstract int getProcess(int slot);

    protected abstract boolean canProcess(int process);

    protected abstract void processItem(int process);

    protected abstract int getMainOutput(int process);

    protected abstract int getStackProcessTime(ItemStack stack);

    protected abstract int getProcessCount();

    protected abstract int[] getInputs();

    protected abstract int[] getInputs(int process);

    protected abstract int[] getOutputs();

    protected abstract ItemStack[] getSlots();

    protected abstract void setSlots(ItemStack[] slots);

    public boolean hasOutputSlot(ItemStack output) {
        return this.getOutputSlot(output) != -1;
    }

    public int getOutputSlot(ItemStack output) {
        ItemStack[] slots = this.getSlots();
        int[] outputs = this.getOutputs();
        for (int slot : outputs) {
            ItemStack stack = slots[slot];
            if (stack == null || ((ItemStack.areItemStackTagsEqual(stack, output) && stack.getCount() + output.getCount() <= stack.getMaxStackSize()) && stack.getItem() == output.getItem() && stack.getItemDamage() == output.getItemDamage())) {
                return slot;
            }
        }
        return -1;
    }

    @Override
    public int getField(int id) {
        int processCount = this.getProcessCount();

        if (id < processCount) {
            return this.processTime[id];
        } else if (id < processCount * 2) {
            return this.totalProcessTime[id - processCount];
        }

        return 0;
    }

    @Override
    public void setField(int id, int value) {
        int processCount = this.getProcessCount();

        if (id < processCount) {
            this.processTime[id] = value;
        } else if (id < processCount * 2) {
            this.totalProcessTime[id - processCount] = value;
        }
    }

    @Override
    public int getFieldCount() {
        return this.getProcessCount() * 2;
    }

    @Override
    public void clear() {
        ItemStack[] slots = this.getSlots();

        for (int i = 0; i < slots.length; ++i) {
            slots[i] = null;
        }
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return true;
    }

    protected void mergeStack(int slot, ItemStack stack) {
        ItemStack[] slots = this.getSlots();

        ItemStack previous = slots[slot];
        if (previous == null) {
            slots[slot] = stack;
        } else if (ItemStack.areItemsEqual(previous, stack) && ItemStack.areItemStackTagsEqual(previous, stack)) {
        	int sizePrevious = previous.getCount();
        	sizePrevious += stack.getCount();
        }
    }

    protected void decreaseStackSize(int slot) {
        ItemStack[] slots = this.getSlots();

        slots[slot].shrink(1);

        if (slots[slot].getCount() <= 0) {
            slots[slot] = null;
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
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

    protected boolean shouldResetProgress() {
        return true;
    }

    protected void onSlotUpdate() {
    }
}
