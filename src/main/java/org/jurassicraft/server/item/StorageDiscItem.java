package org.jurassicraft.server.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.api.SynthesizableItem;
import org.jurassicraft.server.genetics.StorageType;
import org.jurassicraft.server.genetics.StorageTypeRegistry;
import org.jurassicraft.server.tab.TabHandler;

import java.util.List;
import java.util.Random;

public class StorageDiscItem extends Item implements SynthesizableItem {
    public StorageDiscItem() {
        super();
        this.setCreativeTab(TabHandler.ITEMS);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> toolTip, ITooltipFlag flagIn)
    {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            String storageId = tag.getString("StorageId");
            StorageType type = StorageTypeRegistry.getStorageType(storageId);
            if (type != null) {
                type.readFromNBT(tag);
                type.addInformation(stack, toolTip);
            }
        } else {
            toolTip.add(TextFormatting.RED + I18n.format("cage.empty.name"));
        }
    }

    @Override
    public boolean isSynthesizable(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        return tagCompound != null && tagCompound.hasKey("DNAQuality") && tagCompound.getInteger("DNAQuality") == 100;
    }

    @Override
    public ItemStack getSynthesizedItem(ItemStack stack, Random random) {
        NBTTagCompound tag = stack.getTagCompound();
        StorageType type = StorageTypeRegistry.getStorageType(tag.getString("StorageId"));
        type.readFromNBT(tag);
        return type.createItem();
    }
}
