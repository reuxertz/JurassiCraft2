package org.jurassicraft.server.block.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.container.CultivateContainer;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.food.FoodNutrients;
import org.jurassicraft.server.item.ItemHandler;

import java.util.Random;

public class CultivatorBlockEntity extends MachineBaseBlockEntity implements TemperatureControl {
    private static final int[] INPUTS = new int[] { 0, 1, 2, 3 };
    private static final int[] OUTPUTS = new int[] { 4 };
    private static final int MAX_NUTRIENTS = 3000;
    private ItemStack[] slots = new ItemStack[5];
    private int waterLevel;
    private int lipids;
    private int proximates;
    private int minerals;
    private int vitamins;

    private int temperature;

    @Override
    protected int getProcess(int slot) {
        return 0;
    }

    @Override
    protected boolean canProcess(int process) {
        if (this.slots[0] != null && this.slots[0].getItem() == ItemHandler.SYRINGE && this.waterLevel == 3) {
            Dinosaur dino = EntityHandler.getDinosaurById(this.slots[0].getItemDamage());
            if (dino != null && (dino.isMammal() || dino.isMarineCreature())) {
                return this.lipids >= dino.getLipids() && this.minerals >= dino.getMinerals() && this.proximates >= dino.getProximates() && this.vitamins >= dino.getVitamins();
            }
        }

        return false;
    }

    @Override
    protected void processItem(int process) {
        ItemStack syringe = this.slots[0];
        Dinosaur dinosaur = EntityHandler.getDinosaurById(syringe.getItemDamage());

        this.waterLevel = 0;

        if (dinosaur != null) {
            this.lipids -= dinosaur.getLipids();
            this.minerals -= dinosaur.getMinerals();
            this.vitamins -= dinosaur.getVitamins();
            this.proximates -= dinosaur.getProximates();

            ItemStack hatchedEgg = new ItemStack(ItemHandler.HATCHED_EGG, 1, syringe.getItemDamage());

            NBTTagCompound compound = new NBTTagCompound();
            compound.setBoolean("Gender", this.temperature > 50);

            NBTTagCompound syringeTag = syringe.getTagCompound();
            if (syringeTag != null) {
                compound.setString("Genetics", syringeTag.getString("Genetics"));
                compound.setInteger("DNAQuality", syringeTag.getInteger("DNAQuality"));
            }

            hatchedEgg.setTagCompound(compound);

            this.slots[0] = hatchedEgg;
        }
    }

    @Override
    public void update() {
        super.update();

        boolean sync = false;

        if (!this.world.isRemote) {
            if (this.waterLevel < 3 && this.slots[2] != null && this.slots[2].getItem() == Items.WATER_BUCKET) {
                if (this.slots[3] == null || this.slots[3].getCount() < 16) {
                    this.slots[2].shrink(1);

                    if (this.slots[2].getCount() <= 0) {
                        this.slots[2] = null;
                    }

                    this.waterLevel++;

                    if (this.slots[3] == null) {
                        this.slots[3] = new ItemStack(Items.BUCKET);
                    } else if (this.slots[3].getItem() == Items.BUCKET) {
                        this.slots[3].grow(1);
                    }

                    sync = true;
                }
            }

            if (this.slots[1] != null) {
                if ((this.proximates < MAX_NUTRIENTS) || (this.minerals < MAX_NUTRIENTS) || (this.vitamins < MAX_NUTRIENTS) || (this.lipids < MAX_NUTRIENTS)) {
                    this.consumeNutrients();
                    sync = true;
                }
            }
        }

        if (sync) {
            this.markDirty();
        }
    }

    private void consumeNutrients() {
        ItemStack foodStack = this.slots[1];
        FoodNutrients nutrients = FoodNutrients.get(foodStack.getItem());

        if (nutrients != null) {
            if (foodStack.getItem() instanceof ItemBucketMilk) {
                this.slots[1] = new ItemStack(Items.BUCKET);
            } else {
                foodStack.shrink(1);
                if (foodStack.getCount() <= 0) {
                    this.slots[1] = null;
                }
            }

            Random random = this.world.rand;

            if (this.proximates < MAX_NUTRIENTS) {
                this.proximates = (short) (this.proximates + (800 + random.nextInt(201)) * nutrients.getProximate());
                if (this.proximates > MAX_NUTRIENTS) {
                    this.proximates = (short) MAX_NUTRIENTS;
                }
            }

            if (this.minerals < MAX_NUTRIENTS) {
                this.minerals = (short) (this.minerals + (900 + random.nextInt(101)) * nutrients.getMinerals());
                if (this.minerals > MAX_NUTRIENTS) {
                    this.minerals = (short) MAX_NUTRIENTS;
                }
            }

            if (this.vitamins < MAX_NUTRIENTS) {
                this.vitamins = (short) (this.vitamins + (900 + random.nextInt(101)) * nutrients.getVitamins());
                if (this.vitamins > MAX_NUTRIENTS) {
                    this.vitamins = (short) MAX_NUTRIENTS;
                }
            }

            if (this.lipids < MAX_NUTRIENTS) {
                this.lipids = (short) (this.lipids + (980 + random.nextInt(101)) * nutrients.getLipids());
                if (this.lipids > MAX_NUTRIENTS) {
                    this.lipids = (short) MAX_NUTRIENTS;
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.waterLevel = compound.getShort("WaterLevel");
        this.lipids = compound.getInteger("Lipids");
        this.minerals = compound.getInteger("Minerals");
        this.vitamins = compound.getInteger("Vitamins");
        this.proximates = compound.getInteger("Proximates");
        this.temperature = compound.getInteger("Temperature");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        compound.setShort("WaterLevel", (short) this.waterLevel);
        compound.setInteger("Lipids", this.lipids);
        compound.setInteger("Minerals", this.minerals);
        compound.setInteger("Vitamins", this.vitamins);
        compound.setInteger("Proximates", this.proximates);
        compound.setInteger("Temperature", this.temperature);

        return compound;
    }

    @Override
    protected int getMainOutput(int process) {
        return 4;
    }

    @Override
    protected int getStackProcessTime(ItemStack stack) {
        return 2000;
    }

    @Override
    protected int getProcessCount() {
        return 1;
    }

    @Override
    protected int[] getInputs() {
        return INPUTS;
    }

    @Override
    protected int[] getInputs(int process) {
        return INPUTS;
    }

    @Override
    protected int[] getOutputs() {
        return OUTPUTS;
    }

    @Override
    protected ItemStack[] getSlots() {
        return this.slots;
    }

    @Override
    protected void setSlots(ItemStack[] slots) {
        this.slots = slots;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new CultivateContainer(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return JurassiCraft.MODID + ":cultivator";
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.cultivator";
    }

    public int getWaterLevel() {
        return this.waterLevel;
    }

    public int getMaxNutrients() {
        return MAX_NUTRIENTS;
    }

    public int getProximates() {
        return this.proximates;
    }

    public int getMinerals() {
        return this.minerals;
    }

    public int getVitamins() {
        return this.vitamins;
    }

    public int getLipids() {
        return this.lipids;
    }

    @Override
    public int getField(int id) {
        int processCount = this.getProcessCount();

        if (id < processCount) {
            return this.processTime[id];
        } else if (id < processCount * 2) {
            return this.totalProcessTime[id - processCount];
        } else {
            int type = id - (processCount * 2);
            switch (type) {
                case 0:
                    return this.waterLevel;
                case 1:
                    return this.proximates;
                case 2:
                    return this.minerals;
                case 3:
                    return this.vitamins;
                case 4:
                    return this.lipids;
                case 5:
                    return this.temperature;
            }
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
        } else {
            int type = id - (processCount * 2);
            switch (type) {
                case 0:
                    this.waterLevel = value;
                    break;
                case 1:
                    this.proximates = value;
                    break;
                case 2:
                    this.minerals = value;
                    break;
                case 3:
                    this.vitamins = value;
                    break;
                case 4:
                    this.lipids = value;
                    break;
                case 5:
                    this.temperature = value;
                    break;
            }
        }
    }

    @Override
    public int getFieldCount() {
        return this.getProcessCount() * 2 + 6;
    }

    public Dinosaur getDinosaur() {
        if (this.slots[0] != null) {
            return EntityHandler.getDinosaurById(this.slots[0].getItemDamage());
        }

        return null;
    }

    @Override
    public void setTemperature(int index, int value) {
        this.temperature = value;
    }

    @Override
    public int getTemperature(int index) {
        return this.temperature;
    }

    @Override
    public int getTemperatureCount() {
        return 1;
    }

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
}
