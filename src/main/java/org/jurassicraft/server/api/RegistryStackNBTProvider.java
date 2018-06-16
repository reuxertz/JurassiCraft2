package org.jurassicraft.server.api;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//TODO: maybe merge with `StackNBTProvider` seeing as it wont be used on its own
public interface RegistryStackNBTProvider<T extends IForgeRegistryEntry.Impl<T> & Comparable<T>> extends StackNBTProvider<T> {

    IForgeRegistry<T> getRegistry();

    @Override
    default List<T> getAllValues() {
        return Lists.newArrayList(getRegistry().getValuesCollection());
    }

    @Override
    default String getRegistryNameFor(T type) {
        return type.getRegistryName().toString();
    }

    @Override
    default T getValueFromName(String string) {
        return getRegistry().getValue(new ResourceLocation(string));
    }

    default Map<Object, ResourceLocation> getModelResourceLocations(T value) {
        ResourceLocation res = ((IForgeRegistryEntry.Impl)this).getRegistryName(); //A bit of a hack, but whatever
        ResourceLocation reg = value.getRegistryName();
        return new HashMap<Object, ResourceLocation>(){{
            put(getDefault(), new ResourceLocation(reg.getResourceDomain(), getFolderLocation(res) + "/" + reg.getResourcePath()));
        }};
    }

    default String getFolderLocation(ResourceLocation res) {
        return "item/" + res.getResourcePath();
    }

}
