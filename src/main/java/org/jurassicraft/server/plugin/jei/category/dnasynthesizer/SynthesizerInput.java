package org.jurassicraft.server.plugin.jei.category.dnasynthesizer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.api.SynthesizableItem;

public class SynthesizerInput {
    public final ItemStack stack;
    public final SynthesizableItem item;

    public SynthesizerInput(ItemStack stack) {
        this.stack = stack;
        Item item = stack.getItem();
        if(item instanceof ItemBlock) {
            this.item = (SynthesizableItem)((ItemBlock)stack.getItem()).getBlock();
        } else {
            this.item = (SynthesizableItem)stack.getItem();
        }
    }
}
