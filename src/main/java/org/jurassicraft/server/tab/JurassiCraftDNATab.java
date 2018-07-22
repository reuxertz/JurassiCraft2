package org.jurassicraft.server.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.entity.Dinosaur;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.registries.JurassicraftRegisteries;

import java.util.Collection;

public class JurassiCraftDNATab extends CreativeTabs {
    private ItemStack[] stacks = null;

    public JurassiCraftDNATab(String label) {
        super(label);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        if (this.stacks == null) {
            Collection<Dinosaur> registeredDinosaurs = JurassicraftRegisteries.DINOSAUR_REGISTRY.getValuesCollection();

            int dinosaurs = registeredDinosaurs.size();
            this.stacks = new ItemStack[dinosaurs * 3];

            int i = 0;

            for (Dinosaur dino : registeredDinosaurs) {
                this.stacks[i] = ItemHandler.DNA.getItemStack(dino);
                this.stacks[i + dinosaurs] = ItemHandler.SOFT_TISSUE.getItemStack(dino);
                this.stacks[i + (dinosaurs * 2)] = ItemHandler.SYRINGE.getItemStack(dino);

                i++;
            }
        }

        return this.stacks[(int) ((JurassiCraft.timerTicks / 20) % this.stacks.length)];
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ItemHandler.DNA);
    }
}
