	package org.jurassicraft.server.block.entity;

import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.api.CleanableItem;
import org.jurassicraft.server.container.CleaningStationContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CleaningStationBlockEntity extends TileEntityLockable implements ITickable, ISidedInventory {
    private static final int[] SLOTS_TOP = new int[] { 0 };
    private static final int[] SLOTS_BOTTOM = new int[] { 7, 6, 5, 4, 3, 2, 1 };
    private static final int[] SLOTS_SIDES = new int[] { 1 }; // 0 = cleaning 1 = fuel 2 = output

    private NonNullList<ItemStack> slots = NonNullList.<ItemStack>withSize(8,ItemStack.EMPTY);

    private int cleaningStationWaterTime;

    private int currentItemWaterTime;

    private int cleanTime;
    private int totalCleanTime;

    private String customName;

    @SideOnly(Side.CLIENT)
    public static boolean isCleaning(IInventory inventory) {
        return inventory.getField(0) > 0;
    }

    public static boolean isItemFuel(ItemStack stack) {
        return stack != null && stack.getItem() == Items.WATER_BUCKET;
    }

    @Override
    public int getSizeInventory() {
        return this.slots.size();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.slots.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return decrStackSize(index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return removeStackFromSlot(index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        boolean flag = stack != null && stack.isItemEqual(this.slots.get(index)) && ItemStack.areItemStackTagsEqual(stack, this.slots.get(index));
        this.slots.set(index, stack);

        if (stack != null && stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            this.totalCleanTime = this.getStackWashTime(stack);
            this.cleanTime = 0;
            this.markDirty();
        }
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.cleaning_station";
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && this.customName.length() > 0;
    }

    public void setCustomInventoryName(String customName) {
        this.customName = customName;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        NonNullList[] slots = new NonNullList[this.getSizeInventory()];
        super.readFromNBT(compound);
        NBTTagList itemList = compound.getTagList("Items", 10);

        for (int i = 0; i < itemList.tagCount(); ++i) {
            NBTTagCompound item = itemList.getCompoundTagAt(i);
            ItemStack stack = new ItemStack(item);

            byte slot = item.getByte("Slot");

            if (slot >= 0 && slot < this.slots.size()) {
                this.slots.set(slot, stack);
            }
        }

        this.cleaningStationWaterTime = compound.getShort("WaterTime");
        this.cleanTime = compound.getShort("CleanTime");
        this.totalCleanTime = compound.getShort("CleanTimeTotal");
        this.currentItemWaterTime = this.getItemCleanTime(this.slots.get(1));

        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setShort("WaterTime", (short) this.cleaningStationWaterTime);
        compound.setShort("CleanTime", (short) this.cleanTime);
        compound.setShort("CleanTimeTotal", (short) this.totalCleanTime);
        NBTTagList itemList = new NBTTagList();

        for (int slot = 0; slot < this.slots.size(); ++slot) {
            if (this.slots.get(slot) != null) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("Slot", (byte) slot);

                this.slots.get(slot).writeToNBT(itemTag);
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
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isCleaning() {
        return this.cleaningStationWaterTime > 0;
    }

    @Override
    public void update() {
        boolean isCleaning = this.isCleaning();
        boolean dirty = false;

        if (this.isCleaning() && this.canClean()) {
            --this.cleaningStationWaterTime;
        }

        if (!this.world.isRemote) {
            if (!this.isCleaning() && (this.slots.get(1) == null || this.slots.get(0) == null)) {
                if (!this.isCleaning() && this.cleanTime > 0) {
                    this.cleanTime = MathHelper.clamp(this.cleanTime - 2, 0, this.totalCleanTime);
                }
            } else {
                if (!this.isCleaning() && this.canClean() && isItemFuel(this.slots.get(1))) {
                    this.currentItemWaterTime = this.cleaningStationWaterTime = this.getItemCleanTime(this.slots.get(1));

                    if (this.isCleaning()) {
                        dirty = true;

                        if (this.slots.get(1) != null) {
                            this.slots.get(1).setCount(-1);

                            if (this.slots.get(1).getCount() == 0) {
                                this.slots.set(1, this.slots.get(1).getItem().getContainerItem(this.slots.get(1)));
                            }
                        }
                    }
                }

                if (this.isCleaning() && this.canClean() && this.cleaningStationWaterTime > 0) {
                    ++this.cleanTime;

                    if (this.cleanTime == this.totalCleanTime) {
                        this.cleanTime = 0;
                        this.totalCleanTime = this.getStackWashTime(this.slots.get(0));
                        this.cleanItem();
                        dirty = true;
                    }
                } else {
                    this.cleanTime = 0;
                    dirty = true;
                }
            }

            if (isCleaning != this.isCleaning()) {
                dirty = true;
            }
        }

        if (this.cleaningStationWaterTime == 0) {
            this.cleanTime = 0;
        }

        if (dirty) {
            this.markDirty();
        }
    }

    private int getItemCleanTime(ItemStack stack) {
        return 1600;
    }

    public int getStackWashTime(ItemStack stack) {
        return 200;
    }

    private boolean canClean() {
        CleanableItem cleanableItem = CleanableItem.getCleanableItem(this.slots.get(0));

        if (cleanableItem != null && cleanableItem.isCleanable(this.slots.get(0))) {
            for (int i = 2; i < 8; i++) {
                if (this.slots.get(i) == null) {
                    return true;
                }
            }
        }

        return false;
    }

    public void cleanItem() {
        if (this.canClean()) {
            CleanableItem cleanableItem = CleanableItem.getCleanableItem(this.slots.get(0));

            ItemStack output = cleanableItem.getCleanedItem(this.slots.get(0), this.world.rand);

            int emptySlot = -1;

            for (int i = 2; i < 8; i++) {
                ItemStack slot = this.slots.get(i);

                if (slot == null || (ItemStack.areItemsEqual(slot, output) && ItemStack.areItemStackTagsEqual(slot, output) && slot.getItemDamage() == output.getItemDamage())) {
                    emptySlot = i;
                    break;
                }
            }

            if (emptySlot != -1) {
                if (this.slots.get(emptySlot) == null) {
                    this.slots.set(emptySlot, output);
                } else if (this.slots.get(emptySlot).getItem() == output.getItem() && ItemStack.areItemStackTagsEqual(this.slots.get(emptySlot), output)) {
                	int stack = this.slots.get(emptySlot).getCount();
                    stack += output.getCount();
                }

                this.slots.get(0).setCount(-1);

                if (this.slots.get(0).getCount() <= 0) {
                    this.slots.set(0, null);
                }

                this.world.markChunkDirty(this.pos, this);
            }
        }
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index != 2 && (index != 1 || isItemFuel(stack));
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.DOWN ? SLOTS_BOTTOM : (side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
        return this.isItemValidForSlot(index, stack);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        if (direction == EnumFacing.DOWN && index == 1) {
            Item item = stack.getItem();

            if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String getGuiID() {
        return JurassiCraft.MODID + ":cleaning_station";
    }

    @Override
    public Container createContainer(InventoryPlayer inventory, EntityPlayer playerIn) {
        return new CleaningStationContainer(inventory, this);
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return this.cleaningStationWaterTime;
            case 1:
                return this.currentItemWaterTime;
            case 2:
                return this.cleanTime;
            case 3:
                return this.totalCleanTime;
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.cleaningStationWaterTime = value;
                break;
            case 1:
                this.currentItemWaterTime = value;
                break;
            case 2:
                this.cleanTime = value;
                break;
            case 3:
                this.totalCleanTime = value;
        }
    }

    @Override
    public int getFieldCount() {
        return 4;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.slots.size(); ++i) {
            this.slots.set(i, null);
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound compound = new NBTTagCompound();
        compound = this.writeToNBT(compound);
        return new SPacketUpdateTileEntity(this.pos, this.getBlockMetadata(), compound);
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }
    
    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

	@Override
	public boolean isEmpty() {
		return false;
	}
}
