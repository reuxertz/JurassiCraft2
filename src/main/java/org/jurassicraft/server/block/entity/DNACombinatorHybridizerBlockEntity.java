package org.jurassicraft.server.block.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jurassicraft.server.api.Hybrid;
import org.jurassicraft.server.entity.Dinosaur;
import org.jurassicraft.server.registries.JurassicraftRegisteries;
import org.jurassicraft.server.genetics.DinoDNA;
import org.jurassicraft.server.genetics.PlantDNA;
import org.jurassicraft.server.item.ItemHandler;

import java.util.ArrayList;
import java.util.List;

public class DNACombinatorHybridizerBlockEntity extends MachineBaseBlockEntity {
    private static final int[] HYBRIDIZER_INPUTS = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
    private static final int[] COMBINATOR_INPUTS = new int[] { 8, 9 };
    private static final int[] HYBRIDIZER_OUTPUTS = new int[] { 10 };
    private static final int[] COMBINATOR_OUTPUTS = new int[] { 11 };
    
    private boolean hybridizerMode;

    @Override
    protected int getProcessFromSlot(int slot) {
        return 0;
    }

    @Override
    protected int getInventorySize() {
        return 12;
    }

    private Dinosaur getHybrid() {
        return this.getHybrid(this.inventory.getStackInSlot(0), this.inventory.getStackInSlot(1), this.inventory.getStackInSlot(2), this.inventory.getStackInSlot(3), this.inventory.getStackInSlot(4), this.inventory.getStackInSlot(5), this.inventory.getStackInSlot(6), this.inventory.getStackInSlot(7));
    }

    private Dinosaur getHybrid(ItemStack... discs) {
        Dinosaur hybrid = null;

        Dinosaur[] dinosaurs = new Dinosaur[discs.length];

        for (int i = 0; i < dinosaurs.length; i++) {
            dinosaurs[i] = this.getDino(discs[i]);
        }

        for (Dinosaur dino : JurassicraftRegisteries.DINOSAUR_REGISTRY.getValuesCollection()) {
            if (dino instanceof Hybrid) {
                Hybrid dinoHybrid = (Hybrid) dino;

                int count = 0;
                boolean extra = false;

                List<Class<? extends Dinosaur>> usedGenes = new ArrayList<>();

                for (Dinosaur discDinosaur : dinosaurs) {
                    Class match = null;

                    for (Class clazz : dinoHybrid.getDinosaurs()) {
                        if (clazz.isInstance(discDinosaur) && !usedGenes.contains(clazz)) {
                            match = clazz;
                        }
                    }

                    if (match != null && match.isInstance(discDinosaur)) {
                        usedGenes.add(match);
                        count++;
                    } else if (discDinosaur != null) {
                        extra = true;
                    }
                }

                if (!extra && count == dinoHybrid.getDinosaurs().length) {
                    hybrid = dino;

                    break;
                }
            }
        }
        return hybrid;
    }

    private Dinosaur getDino(ItemStack disc) {
        if (!disc.isEmpty() && disc.hasTagCompound()) {
            DinoDNA data = DinoDNA.readFromNBT(disc.getTagCompound());

            return data.getDNAQuality() == 100 ? data.getDinosaur() : null;
        } else {
            return null;
        }
    }

    @Override
    protected boolean canProcess(int process) {
        if (this.hybridizerMode) {
            return this.inventory.getStackInSlot(10).isEmpty() && this.getHybrid() != null;
        } else {
            if (!this.inventory.getStackInSlot(8).isEmpty() && this.inventory.getStackInSlot(8).getItem() == ItemHandler.STORAGE_DISC && !this.inventory.getStackInSlot(9).isEmpty() && this.inventory.getStackInSlot(9).getItem() == ItemHandler.STORAGE_DISC) {
                if (this.inventory.getStackInSlot(8).getTagCompound() != null && this.inventory.getStackInSlot(9).getTagCompound() != null && this.inventory.getStackInSlot(11).isEmpty() && this.inventory.getStackInSlot(8).getItemDamage() == this.inventory.getStackInSlot(9).getItemDamage() && this.inventory.getStackInSlot(8).getTagCompound().getString("StorageId").equals(this.inventory.getStackInSlot(9).getTagCompound().getString("StorageId"))) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    protected void processItem(int process) {
        if (this.hybridizerMode) {
            Dinosaur hybrid = this.getHybrid();

            NBTTagCompound nbt = new NBTTagCompound();

            DinoDNA dna = new DinoDNA(hybrid, 100, this.inventory.getStackInSlot(0).getOrCreateSubCompound("jurassicraft").getCompoundTag("dna").getString("Genetics"));
            dna.writeToNBT(nbt);

            ItemStack output = new ItemStack(ItemHandler.STORAGE_DISC);
            output.getOrCreateSubCompound("jurassicraft").setTag("dna", nbt);

            this.mergeStack(this.getOutputSlot(output, process), output);

            for (int i = 0; i < 8; i++) {
                this.decreaseStackSize(i);
            }
        } else {
            ItemStack output = new ItemStack(ItemHandler.STORAGE_DISC);

            String storageId = this.inventory.getStackInSlot(8).getOrCreateSubCompound("jurassicraft").getCompoundTag("dna").getString("StorageId");

            if (storageId.equals("DinoDNA")) { //TODO: generilize DinoDNA and PlantDNA
                DinoDNA dna1 = DinoDNA.readFromNBT(this.inventory.getStackInSlot(8).getOrCreateSubCompound("jurassicraft").getCompoundTag("dna"));
                DinoDNA dna2 = DinoDNA.readFromNBT(this.inventory.getStackInSlot(9).getOrCreateSubCompound("jurassicraft").getCompoundTag("dna"));

                int newQuality = dna1.getDNAQuality() + dna2.getDNAQuality();

                if (newQuality > 100) {
                    newQuality = 100;
                }

                DinoDNA newDNA = new DinoDNA(dna1.getDinosaur(), newQuality, dna1.getGenetics());

                NBTTagCompound outputTag = new NBTTagCompound();
                newDNA.writeToNBT(outputTag);
                output.getOrCreateSubCompound("jurassicraft").setTag("dna", outputTag);
            } else if (storageId.equals("PlantDNA")) {
                PlantDNA dna1 = PlantDNA.readFromNBT(this.inventory.getStackInSlot(8).getOrCreateSubCompound("jurassicraft").getCompoundTag("dna"));
                PlantDNA dna2 = PlantDNA.readFromNBT(this.inventory.getStackInSlot(9).getOrCreateSubCompound("jurassicraft").getCompoundTag("dna"));

                int newQuality = dna1.getDNAQuality() + dna2.getDNAQuality();

                if (newQuality > 100) {
                    newQuality = 100;
                }

                PlantDNA newDNA = new PlantDNA(dna1.getPlant(), newQuality);

                NBTTagCompound outputTag = new NBTTagCompound();
                newDNA.writeToNBT(outputTag);
                output.setTagCompound(outputTag);
            }

            this.mergeStack(11, output);

            this.decreaseStackSize(8);
            this.decreaseStackSize(9);
        }
    }

    @Override
    protected int getStackProcessTime(ItemStack stack) {
        return 1000;
    }

    @Override
    protected int getProcessCount() {
        return 1;
    }

    @Override
    protected int[] getInputs(int process) {
        return this.hybridizerMode ? HYBRIDIZER_INPUTS : COMBINATOR_INPUTS;
    }

    @Override
    protected int[] getOutputs(int process) {
        return this.hybridizerMode ? HYBRIDIZER_OUTPUTS : COMBINATOR_OUTPUTS;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.hybridizerMode = nbt.getBoolean("HybridizerMode");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        nbt.setBoolean("HybridizerMode", this.hybridizerMode);
        return nbt;
    }

    public boolean getMode() {
        return this.hybridizerMode;
    }

    public void setMode(boolean mode) {
        this.hybridizerMode = mode;
        this.processTime[0] = 0;
        this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
    }

}
