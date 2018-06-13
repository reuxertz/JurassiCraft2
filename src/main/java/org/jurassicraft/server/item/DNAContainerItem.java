package org.jurassicraft.server.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.jurassicraft.server.genetics.GeneticsHelper;
import org.jurassicraft.server.util.LangHelper;

import javax.annotation.Nullable;
import java.util.List;

public class DNAContainerItem extends Item implements DinosaurProvider {

    public int getDNAQuality(EntityPlayer player, ItemStack stack) {
        int quality = player.capabilities.isCreativeMode ? 100 : 0;

        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt == null) {
            nbt = new NBTTagCompound();
        }

        if (nbt.hasKey("DNAQuality")) {
            quality = nbt.getInteger("DNAQuality");
        } else {
            nbt.setInteger("DNAQuality", quality);
        }

        stack.setTagCompound(nbt);

        return quality;
    }

    public String getGeneticCode(EntityPlayer player, ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();

        String genetics = GeneticsHelper.randomGenetics(player.world.rand);

        if (nbt == null) {
            nbt = new NBTTagCompound();
        }

        if (nbt.hasKey("Genetics")) {
            genetics = nbt.getString("Genetics");
        } else {
            nbt.setString("Genetics", genetics);
        }

        stack.setTagCompound(nbt);

        return genetics;
    }

//    @Override
//    public void addInformation(ItemStack stack, World worldIn, List<String> lore, ITooltipFlag flagIn) {
//      /* TODO: this.getDNAQuality(stack., stack); */  int quality = 100;
//        TextFormatting colour;
//
//        if (quality > 75) {
//            colour = TextFormatting.GREEN;
//        } else if (quality > 50) {
//            colour = TextFormatting.YELLOW;
//        } else if (quality > 25) {
//            colour = TextFormatting.GOLD;
//        } else {
//            colour = TextFormatting.RED;
//        }
//        lore.add(colour + new LangHelper("lore.dna_quality.name").withProperty("quality", quality + "").build());
//        lore.add(TextFormatting.BLUE + new LangHelper("lore.genetic_code.name").withProperty("code", this.getGeneticCode(player, stack)).build());
//    }
}
