package org.jurassicraft.server.genetics;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import org.jurassicraft.server.plant.PlantHandler;
import org.jurassicraft.server.util.LangHelper;

import java.util.List;
import java.util.Locale;

public class PlantDNA {
    private int plant;
    private int quality;

    public PlantDNA(int plant, int quality) {
        this.plant = plant;
        this.quality = quality;
    }

    public static PlantDNA fromStack(ItemStack stack) {
        return readFromNBT(stack.getTagCompound());
    }

    public static PlantDNA readFromNBT(NBTTagCompound nbt) {
        return new PlantDNA(nbt.getInteger("Plant"), nbt.getInteger("DNAQuality"));
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("DNAQuality", this.quality);
        nbt.setInteger("Plant", this.plant);
        nbt.setString("StorageId", "PlantDNA");
    }

    public int getDNAQuality() {
        return this.quality;
    }

    public int getPlant() {
        return this.plant;
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        tooltip.add(TextFormatting.DARK_AQUA + new LangHelper("lore.plant.name").withProperty("plant", "plants." + PlantHandler.getPlantById(this.plant).getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_") + ".name").build());

        TextFormatting formatting;

        if (this.quality > 75) {
            formatting = TextFormatting.GREEN;
        } else if (this.quality > 50) {
            formatting = TextFormatting.YELLOW;
        } else if (this.quality > 25) {
            formatting = TextFormatting.GOLD;
        } else if (this.quality == -1) {
            formatting = TextFormatting.AQUA;
        } else {
            formatting = TextFormatting.RED;
        }

        tooltip.add(formatting + new LangHelper("lore.dna_quality.name").withProperty("quality", (this.quality == -1 ? TextFormatting.OBFUSCATED.toString() : "") + this.quality + "").build());
    }

    public int getMetadata() {
        return this.plant;
    }
}
