package org.jurassicraft.server.genetics;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.util.LangHelper;

import java.util.List;
import java.util.Locale;

public class/* Bingo! */ DinoDNA {
    private int quality;
    private String genetics;
    private Dinosaur dinosaur;

    public DinoDNA(Dinosaur dinosaur, int quality, String genetics) {
        this.quality = quality;
        this.genetics = genetics;
        this.dinosaur = dinosaur;
    }

    public static DinoDNA fromStack(ItemStack stack) {
        return readFromNBT(stack.getTagCompound());
    }

    public static DinoDNA readFromNBT(NBTTagCompound nbt) {
        return new DinoDNA(EntityHandler.getDinosaurById(nbt.getInteger("Dinosaur")), nbt.getInteger("DNAQuality"), nbt.getString("Genetics"));
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("DNAQuality", this.quality);
        nbt.setString("Genetics", this.genetics);
        nbt.setString("StorageId", "DinoDNA");
        nbt.setInteger("Dinosaur", EntityHandler.getDinosaurId(this.dinosaur));
    }

    public int getDNAQuality() {
        return this.quality;
    }

    public String getGenetics() {
        return this.genetics;
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        tooltip.add(TextFormatting.DARK_AQUA + new LangHelper("lore.dinosaur.name").withProperty("dino", "entity.jurassicraft." + this.dinosaur.getName().toLowerCase(Locale.ENGLISH) + ".name").build());

        TextFormatting colour;

        if (this.quality > 75) {
            colour = TextFormatting.GREEN;
        } else if (this.quality > 50) {
            colour = TextFormatting.YELLOW;
        } else if (this.quality > 25) {
            colour = TextFormatting.GOLD;
        } else if(this.quality == -1) {
            colour = TextFormatting.AQUA;
        } else {
            colour = TextFormatting.RED;
        }
        tooltip.add(colour + new LangHelper("lore.dna_quality.name").withProperty("quality", (this.quality == -1 ? TextFormatting.OBFUSCATED.toString() : "") + this.quality + "").build());
        tooltip.add(TextFormatting.BLUE + new LangHelper("lore.genetic_code.name").withProperty("code", this.genetics.isEmpty() ? TextFormatting.OBFUSCATED.toString() + "aaa" : this.genetics).build());
    }

    public Dinosaur getDinosaur() {
        return this.dinosaur;
    }

    public int getMetadata() {
        return EntityHandler.getDinosaurId(this.dinosaur);
    }
}
