package org.jurassicraft.server.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.api.DinosaurProvider;
import org.jurassicraft.server.entity.Dinosaur;
import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.util.LangHelper;

import java.util.Locale;

public class DinosaurSteakItem extends ItemFood implements DinosaurProvider {
    public DinosaurSteakItem() {
        super(8, 0.8F, true);
        this.setCreativeTab(TabHandler.FOODS);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Dinosaur dinosaur = this.getValue(stack);
        return new LangHelper("item.dinosaur_steak.name").withProperty("dino", "entity.jurassicraft." + dinosaur.name.replace(" ", "_").toLowerCase(Locale.ENGLISH) + ".name").build();
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
//            this.getValue(stack).applyMeatEffect(player, true); //TODO:
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subtypes) {
        if(this.isInCreativeTab(tab)) {
            subtypes.addAll(this.getAllStacksOrdered());
        }
    }
}
