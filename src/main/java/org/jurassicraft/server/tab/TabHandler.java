package org.jurassicraft.server.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.entity.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.item.AmberItem;
import org.jurassicraft.server.item.FossilItem;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.registries.JurassicraftRegisteries;

import java.util.Collection;

public class TabHandler {

    public static final JurassiCraftDNATab DNA = new JurassiCraftDNATab("jurassicraft.dna");

    public static final CreativeTabs ITEMS = new CreativeTabs("jurassicraft.items") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ItemHandler.AMBER.get(AmberItem.AmberStorageType.MOSQUITO));
        }
    };

    public static final CreativeTabs BLOCKS = new CreativeTabs("jurassicraft.blocks") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Item.getItemFromBlock(BlockHandler.GYPSUM_BRICKS));
        }
    };

    public static final CreativeTabs PLANTS = new CreativeTabs("jurassicraft.plants") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Item.getItemFromBlock(BlockHandler.SMALL_ROYAL_FERN));
        }
    };

    public static final CreativeTabs FOSSILS = new CreativeTabs("jurassicraft.fossils") {
        @Override
        public ItemStack getTabIconItem() {
            return ItemHandler.FOSSIL.createNewStack(new FossilItem.FossilInfomation(EntityHandler.VELOCIRAPTOR, "skull"));
        }
    };

    public static final CreativeTabs FOODS = new CreativeTabs("jurassicraft.FOODS") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ItemHandler.DINOSAUR_MEAT);
        }
    };

    public static final CreativeTabs DECORATIONS = new CreativeTabs("jurassicraft.fossils") {
        @Override
        public ItemStack getTabIconItem() {
                return new ItemStack(ItemHandler.DISPLAY_BLOCK);
        }

    };
}
