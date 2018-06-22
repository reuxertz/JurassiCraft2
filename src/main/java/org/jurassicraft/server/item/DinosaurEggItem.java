package org.jurassicraft.server.item;

import java.util.Locale;

import org.jurassicraft.server.api.DinosaurProvider;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.util.LangHelper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DinosaurEggItem extends DNAContainerItem implements DinosaurProvider {//TODO not let direct birth animals
    public DinosaurEggItem() {
        this.setMaxStackSize(1);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String dinoName = this.getValue(stack).getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

        return new LangHelper("item.egg.name").withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
    }

    @Override
    public boolean shouldOverrideModel(Dinosaur dinosaur) {
        return dinosaur.getBirthType() == Dinosaur.BirthType.EGG_LAYING;
    }
}
