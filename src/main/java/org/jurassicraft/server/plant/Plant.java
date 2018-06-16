package org.jurassicraft.server.plant;

import net.minecraft.block.Block;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jurassicraft.server.food.FoodHelper;

public class Plant extends IForgeRegistryEntry.Impl<Plant> implements Comparable<Plant> {

    public static Plant MISSING = PlantHandler.AJUGINUCULA_SMITHII;

    private final Block block;
    private final int healAmount;

    private FoodHelper.FoodEffect[] foodEffects = new FoodHelper.FoodEffect[0];
    private boolean isTree = false;
    private boolean isPrehistoric = true;

    @Deprecated
    public Plant(String name, Block block, int healAmount) {
        this(block, healAmount);
        this.setRegistryName(name);
    }

    public Plant(Block block, int healAmount) {
        this.block = block;
        this.healAmount = healAmount;
    }

    public Plant withTree(boolean isTree) {
        this.isTree = isTree;
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

    public boolean shouldRegister() {
        return true;
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
        return this.isTree;
    }
}
