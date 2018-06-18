package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.tree.TreeType;
import org.jurassicraft.server.food.FoodHelper;

public class Plant extends IForgeRegistryEntry.Impl<Plant> implements Comparable<Plant> {
    @GameRegistry.ObjectHolder(JurassiCraft.MODID + ":ajuginucula_smithii")
    public static Plant MISSING = null;

    private final Block block;
    private final int healAmount;

    private FoodHelper.FoodEffect[] foodEffects = new FoodHelper.FoodEffect[0];
    private boolean isPrehistoric = true;
    private TreeType treeType;

    public Plant(Block block, int healAmount) {
        this.block = block;
        this.healAmount = healAmount;
    }

    public Plant withTreeType(TreeType treeType) {
        this.treeType = treeType;
        return this;
    }

    public Plant withIsPrehistoric(boolean isPrehistoric) {
        this.isPrehistoric = isPrehistoric;
        return this;
    }

    public Plant withFoodEffects(FoodHelper.FoodEffect... foodEffects) {
        this.foodEffects = foodEffects;
        return this;
    }

    public Block getBlock() {
        return this.block;
    }

    public TreeType getTreeType() {
        return treeType;
    }

    @Override
    public int compareTo(Plant plant) {
        return this.getRegistryName().toString().compareTo(plant.getRegistryName().toString());
    }

    public int getHealAmount() {
        return this.healAmount;
    }

    public FoodHelper.FoodEffect[] getEffects() {
        return foodEffects;
    }

    public boolean isPrehistoric() {
        return this.isPrehistoric;
    }

    public boolean isTree() {
        return this.treeType != null;
    }
}
