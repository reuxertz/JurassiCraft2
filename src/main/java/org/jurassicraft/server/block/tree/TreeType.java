package org.jurassicraft.server.block.tree;

import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.plant.PlantHandler;
import org.jurassicraft.server.registries.JurassicraftRegisteries;
import org.jurassicraft.server.world.tree.AraucariaTreeGenerator;
import org.jurassicraft.server.world.tree.CalamitesTreeGenerator;
import org.jurassicraft.server.world.tree.GinkgoTreeGenerator;
import org.jurassicraft.server.world.tree.PhoenixTreeGenerator;
import org.jurassicraft.server.world.tree.PsaroniusTreeGenerator;

import java.util.function.Supplier;

public enum TreeType {
    GINKGO(new GinkgoTreeGenerator()),
    CALAMITES(new CalamitesTreeGenerator()),
    PSARONIUS(new PsaroniusTreeGenerator()),
    PHOENIX(new PhoenixTreeGenerator(), 5, () -> new ItemStack(ItemHandler.PHOENIX_FRUIT)),
    ARAUCARIA(new AraucariaTreeGenerator());

    private WorldGenAbstractTree generator;
    private Plant plant;
    private int dropChance;
    private Supplier<ItemStack> drop;

    TreeType(WorldGenAbstractTree generator) {
        this(generator, 10);
    }

    TreeType(WorldGenAbstractTree generator, int dropChance) {
        this(generator, dropChance, null);
        this.setDrop(() -> new ItemStack(BlockHandler.ANCIENT_SAPLINGS.get(this)));
    }

    TreeType(WorldGenAbstractTree generator, int dropChance, Supplier<ItemStack> drop) {
        this.generator = generator;
        this.dropChance = dropChance;
        this.drop = drop;
    }

    public void setDrop(Supplier<ItemStack> drop) {
        this.drop = drop;
    }

    public int getDropChance() {
        return this.dropChance;
    }

    public ItemStack getDrop() {
        return this.drop.get();
    }

    public WorldGenAbstractTree getTreeGenerator() {
        return this.generator;
    }

    public Plant getPlant() {
        if(this.plant == null) {
            JurassicraftRegisteries.PLANT_REGISTRY.getValuesCollection().stream().filter(Plant::isTree).forEach(plant -> plant.getTreeType().setPlant(plant));
        }
        return this.plant;
    }

    private void setPlant(Plant plant) {
        this.plant = plant;
    }

}
