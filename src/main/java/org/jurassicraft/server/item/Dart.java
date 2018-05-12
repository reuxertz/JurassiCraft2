package org.jurassicraft.server.item;

import java.util.function.BiConsumer;

import org.jurassicraft.server.entity.DinosaurEntity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Dart extends Item {
    public final BiConsumer<DinosaurEntity, ItemStack> consumer;
    
    public Dart(BiConsumer<DinosaurEntity, ItemStack> consumer) {
	this.consumer = consumer;
    }
}
