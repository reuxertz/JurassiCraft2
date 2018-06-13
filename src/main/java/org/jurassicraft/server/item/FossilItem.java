package org.jurassicraft.server.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.tuple.Pair;
import org.jurassicraft.server.api.GrindableItem;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.entity.JurassicraftRegisteries;
import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.util.LangHelper;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class FossilItem extends Item implements GrindableItem, DinosaurProvider {

    private final boolean fresh;

    public FossilItem(boolean fresh) {
        this.fresh = fresh;
        this.setHasSubtypes(true);
        this.setCreativeTab(TabHandler.FOSSILS);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Dinosaur dinosaur = this.getDinosaur(stack);

        if (dinosaur != null) {
            return new LangHelper("item." + this.getVarient(stack) + ".name").withProperty("dino", "entity.jurassicraft." + dinosaur.getName().replace(" ", "_").toLowerCase(Locale.ENGLISH) + ".name").build();
        }

        return super.getItemStackDisplayName(stack);
    }

    @Override
    public Dinosaur getDinosaur(ItemStack stack) {
        return getFossilInfomation(stack).getDinosaur();
    }

    @Override
    public ItemStack getItemStack(Dinosaur dinosaur) {
        return createNewStack(new FossilInfomation(dinosaur, dinosaur.getBones()[0]));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subtypes) {
        if(this.isInCreativeTab(tab)) {
            FossilInfomation.getAllInfomation().stream().map(this::createNewStack).forEach(subtypes::add);
        }
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> lore, ITooltipFlag flagIn) {
        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt != null && nbt.hasKey("Genetics") && nbt.hasKey("DNAQuality")) {
            int quality = nbt.getInteger("DNAQuality");

            TextFormatting colour;

            if (quality > 75) {
                colour = TextFormatting.GREEN;
            } else if (quality > 50) {
                colour = TextFormatting.YELLOW;
            } else if (quality > 25) {
                colour = TextFormatting.GOLD;
            } else {
                colour = TextFormatting.RED;
            }

            lore.add(colour + new LangHelper("lore.dna_quality.name").withProperty("quality", quality + "").build());
            lore.add(TextFormatting.BLUE + new LangHelper("lore.genetic_code.name").withProperty("code", nbt.getString("Genetics")).build());
        }
    }

    @Override
    public Map<String, ResourceLocation> getModelResourceLocations(Dinosaur dinosaur) {
        Map<String, ResourceLocation> map = Maps.newHashMap();
        ResourceLocation dinoreg = dinosaur.getRegistryName();
        for(String bone : dinosaur.getBones()) {
            map.put(bone, new ResourceLocation(dinoreg.getResourceDomain(), "item/" + (fresh ? "fresh_" : "") + "bones/" + dinoreg.getResourcePath()+ "/" + bone));
        }
        return map;
    }

    @Override
    public String getVarient(ItemStack stack) {
        return getFossilInfomation(stack).getType();
    }

    @Override
    public boolean isGrindable(ItemStack stack) {
        return true;
    }
    
    public boolean isFresh() {
        return fresh;
    }
    
    public String getBoneType(ItemStack stack){
        return getFossilInfomation(stack).getType();
    }
    
    @Override
    public ItemStack getGroundItem(ItemStack stack, Random random) {
        NBTTagCompound tag = stack.getTagCompound();

        int outputType = random.nextInt(6);

        if (outputType == 5 || this.isFresh()) {
            ItemStack output = new ItemStack(ItemHandler.SOFT_TISSUE, 1, stack.getItemDamage());
            output.setTagCompound(tag);
            return output;
        } else if (outputType < 3) {
            return new ItemStack(Items.DYE, 1, 15);
        }

        return new ItemStack(Items.FLINT);
    }

    @Override
    public List<ItemStack> getJEIRecipeTypes() {
        return FossilInfomation.getAllInfomation().stream().map(this::createNewStack).collect(Collectors.toList());
    }

    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        float single = 100F/6F;
        NBTTagCompound tag = inputItem.getTagCompound();
        ItemStack output = new ItemStack(ItemHandler.SOFT_TISSUE, 1, inputItem.getItemDamage());
        output.setTagCompound(tag);
        if(this.isFresh()) {
            return Lists.newArrayList(Pair.of(100F, output));
        }
        return Lists.newArrayList(Pair.of(single, output), Pair.of(50f, new ItemStack(Items.DYE, 1, 15)), Pair.of(single*2f, new ItemStack(Items.FLINT)));
    }

    public static FossilInfomation getFossilInfomation(ItemStack stack) {
        NBTTagCompound nbt = stack.getOrCreateSubCompound("jurassicraft").getCompoundTag("Fossil Info");
        return new FossilInfomation(JurassicraftRegisteries.DINOSAUR_REGISTRY.getValue(new ResourceLocation(nbt.getString("Dinosaur"))), nbt.getString("Bone Type"));
    }

    public static ItemStack putFossilInfomation(ItemStack stack, FossilInfomation infomation) {
        NBTTagCompound compound = stack.getOrCreateSubCompound("jurassicraft");
        NBTTagCompound nbt = compound.getCompoundTag("Fossil Info");
        nbt.setString("Dinosaur", infomation.getDinosaur().getRegistryName().toString());
        nbt.setString("Bone Type", infomation.getType());
        compound.setTag("Fossil Info", nbt);
        return stack;
    }

    public ItemStack createNewStack(FossilInfomation fossilInfomation) {
        return putFossilInfomation(new ItemStack(this), fossilInfomation);
    }

    public static class FossilInfomation {

        public static List<FossilInfomation> getAllInfomation() {
            List<FossilInfomation> list = Lists.newArrayList();
            JurassicraftRegisteries.DINOSAUR_REGISTRY.forEach(dino -> Lists.newArrayList(dino.getBones()).forEach(bone -> list.add(new FossilInfomation(dino, bone))));
            return list;
        }

        private final Dinosaur dinosaur;
        private final String type;

        public FossilInfomation(Dinosaur dinosaur, String string) {
            this.dinosaur = dinosaur;
            this.type = string;
        }

        public Dinosaur getDinosaur() {
            return dinosaur;
        }

        public String getType() {
            return type;
        }
    }
}
