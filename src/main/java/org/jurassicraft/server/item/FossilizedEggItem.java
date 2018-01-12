package org.jurassicraft.server.item;

import java.util.List;
import java.util.Random;

import org.jurassicraft.server.api.GrindableItem;
import org.jurassicraft.server.block.NestFossilBlock;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.util.LangHelper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FossilizedEggItem extends Item implements GrindableItem {
    public FossilizedEggItem() {
        super();
        this.setCreativeTab(TabHandler.FOSSILS);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return new LangHelper("item.fossilized_egg.name").build();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        for (NestFossilBlock.Variant variant : NestFossilBlock.Variant.values()) {
            subItems.add(new ItemStack(item, 1, variant.ordinal()));
        }
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
            List<Dinosaur> dinosaurs = EntityHandler.getPrehistoricDinosaurs();

            ItemStack output = new ItemStack(ItemHandler.SOFT_TISSUE, 1, EntityHandler.getDinosaurId(dinosaurs.get(random.nextInt(dinosaurs.size()))));
            output.setTagCompound(tag);
            return output;
        } else if (outputType == 1) {
            return new ItemStack(Items.DYE, 1, 15);
        }

        return new ItemStack(Items.FLINT);
    }
}
