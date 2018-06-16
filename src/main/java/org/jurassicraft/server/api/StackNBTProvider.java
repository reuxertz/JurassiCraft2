package org.jurassicraft.server.api;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import org.jurassicraft.client.event.DinosaurModelHandler;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface StackNBTProvider<T extends Comparable<T>> {

    List<T> getAllValues();

    String getRegistryNameFor(T type);

    T getValueFromName(String string);

    String getKey();

    default Object getDefault() {
        return "__default";
    }

    default ItemStack getItemStack(T value) {
        ItemStack stack;
        if(this instanceof Item) {
            stack = new ItemStack((Item)this);
        } else if(this instanceof Block) {
            stack = new ItemStack((Block)this);
        } else {
            throw new RuntimeException("DinosaurProvider was applied on a non item/block");
        }
        return this.putValue(stack, value);
    }

    default ItemStack putValue(ItemStack stack, T value) {
        stack.getOrCreateSubCompound("jurassicraft").setString(getKey(), getRegistryNameFor(value));
        return stack;
    }

    default T getValue(ItemStack stack) {
        return this.getValueFromName(stack.getOrCreateSubCompound("jurassicraft").getString(getKey()));
    }

    default List<ItemStack> getAllStacks() {
        return getAllValues().stream().map(this::getItemStack).collect(Collectors.toList());
    }

    default List<ItemStack> getAllStacksOrdered() {
        List<T> list = getAllValues().stream().filter(this::canBeInCreativeTab).collect(Collectors.toList());
        Collections.sort(list);
        return list.stream().map(this::getItemStack).collect(Collectors.toList());
    }

    Map<Object, ResourceLocation> getModelResourceLocations(T value);

    @Nullable
    static StackNBTProvider getFromStack(ItemStack stack) {
        Item item = stack.getItem();
        if(item instanceof StackNBTProvider) {
            return (StackNBTProvider)item;
        } else if((item instanceof ItemBlock && ((ItemBlock)item).getBlock() instanceof StackNBTProvider)) {
            return (StackNBTProvider)((ItemBlock)item).getBlock();
        } else {
            return null;
        }
    }

    default Map<T, Map<Object, IBakedModel>> produceMap(TextureMap map) {
        Map<T, Map<Object, IBakedModel>> modelMap = Maps.newHashMap();
        this.getAllValues().stream().filter(this::shouldOverrideModel).forEach(t ->
                this.getModelResourceLocations(t).forEach((s, location) -> {
                    try {
                        modelMap.computeIfAbsent(t, dino -> Maps.newHashMap()).put(s, ModelLoaderRegistry.getModel(location).bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, map::registerSprite));
                    } catch (Exception e) {
                        e.printStackTrace(DinosaurModelHandler.NonIndentedPrintStream.INSTANCE);
                    }
                })
        );
        return modelMap;
    }

    default Object getVarient(ItemStack stack) {
        return getDefault();
    }

    default boolean shouldOverrideModel(T value) {
        return true;
    }

    default boolean canBeInCreativeTab(T value) {
        return shouldOverrideModel(value);
    }
}
