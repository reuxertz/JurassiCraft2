package org.jurassicraft.server.item;

import com.google.common.collect.Lists;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.tuple.Pair;
import org.jurassicraft.server.api.GrindableItem;
import org.jurassicraft.server.block.NestFossilBlock;
import org.jurassicraft.server.entity.Dinosaur;
import org.jurassicraft.server.registries.JurassicraftRegisteries;
import org.jurassicraft.server.util.LangHelper;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class FossilizedEggItem extends Item implements GrindableItem {

    private final NestFossilBlock.Variant variant;

    FossilizedEggItem(NestFossilBlock.Variant variant) {
        this.variant = variant;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return new LangHelper("item.fossilized_egg.name").build();
    }

    @Override
    public boolean isGrindable(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getGroundItem(ItemStack stack, Random random) {
        NBTTagCompound tag = stack.getTagCompound();

        int outputType = random.nextInt(3);

        if (outputType == 0) {
            List<Dinosaur> dinosaurs = JurassicraftRegisteries.DINOSAUR_REGISTRY.getValues();

            ItemStack output = ItemHandler.SOFT_TISSUE.getItemStack(dinosaurs.get(random.nextInt(dinosaurs.size())));
            output.setTagCompound(tag);
            return output;
        } else if (outputType == 1) {
            return new ItemStack(Items.DYE, 1, 15);
        }

        return new ItemStack(Items.FLINT);
    }

    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        List<Pair<Float, ItemStack>> list = Lists.newArrayList();
        NBTTagCompound tag = inputItem.getTagCompound();
        Collection<Dinosaur> dinosaurs = JurassicraftRegisteries.DINOSAUR_REGISTRY.getValuesCollection();
        float single = 100F/3F;
        float dinoSingle = single / dinosaurs.size();
        for(Dinosaur dinosaur : dinosaurs) {
            ItemStack output = new ItemStack(ItemHandler.SOFT_TISSUE);
            output.setTagCompound(tag);
            output.getOrCreateSubCompound("jurassicraft").setString("dinosaur", dinosaur.getRegistryName().toString());
            list.add(Pair.of(dinoSingle, output));
        }
        list.add(Pair.of(single, new ItemStack(Items.DYE, 1, 15)));
        list.add(Pair.of(single, new ItemStack(Items.FLINT)));

        return list;
    }
}
