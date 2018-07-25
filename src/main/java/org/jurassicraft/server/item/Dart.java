package org.jurassicraft.server.item;

import java.util.function.BiConsumer;

import org.jurassicraft.server.entity.DinosaurEntity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Dart extends Item {
    private final BiConsumer<DinosaurEntity, ItemStack> consumer;
    private final int dartColor;

    public Dart(BiConsumer<DinosaurEntity, ItemStack> consumer) {
        this(consumer, -1);
    }

    public Dart(BiConsumer<DinosaurEntity, ItemStack> consumer, int dartColor) {
	    this.consumer = consumer;
	    this.dartColor = dartColor;
    }

    public int getDartColor(ItemStack stack) {
        return dartColor;
    }

    public BiConsumer<DinosaurEntity, ItemStack> getConsumer() {
        return consumer;
    }
}