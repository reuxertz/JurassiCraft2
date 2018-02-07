package org.jurassicraft.server.world.structure;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.jurassicraft.JurassiCraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StructureGenerationHandler implements IWorldGenerator {
    private static final Map<Biome, List<GeneratorEntry>> GENERATORS = new HashMap<>();

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() == 0) {
            if (random.nextInt(80) == 0) {
                int blockX = (chunkX << 4) + random.nextInt(16);
                int blockZ = (chunkZ << 4) + random.nextInt(16);
                BlockPos pos = new BlockPos(blockX, 0, blockZ);
                Biome biome = world.getBiomeForCoordsBody(pos);
                List<GeneratorEntry> entries = GENERATORS.get(biome);
                if (entries != null && !entries.isEmpty()) {
                    GeneratorEntry generatorEntry = entries.get(random.nextInt(entries.size()));
                    if (generatorEntry != null) {
                        if (random.nextInt(generatorEntry.chance) == 0) {
                            try {
                                StructureGenerator generator = generatorEntry.generator.getConstructor(Random.class).newInstance(random);
                                generator.generate(world, random, pos);
                            } catch (Exception e) {
                                JurassiCraft.INSTANCE.getLogger().error("Failed to generate structure", e);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void register() {
        GameRegistry.registerWorldGenerator(new StructureGenerationHandler(), 0);
        StructureGenerationHandler.registerGenerator(RaptorPaddockGenerator.class, 15, Biomes.JUNGLE, Biomes.MUTATED_JUNGLE, Biomes.JUNGLE_EDGE, Biomes.MUTATED_JUNGLE_EDGE, Biomes.SAVANNA, Biomes.MUTATED_SAVANNA);
        StructureGenerationHandler.registerGenerator(VisitorCentreGenerator.class, 15, Biomes.JUNGLE, Biomes.MUTATED_JUNGLE, Biomes.JUNGLE_EDGE, Biomes.MUTATED_JUNGLE_EDGE, Biomes.SAVANNA, Biomes.MUTATED_SAVANNA);
    }

    public static void registerGenerator(Class<? extends StructureGenerator> generator, int weight, Biome... validBiomes) {
        GeneratorEntry entry = new GeneratorEntry(generator, validBiomes, weight);
        for (Biome biome : validBiomes) {
            StructureGenerationHandler.addEntry(biome, entry);
        }
    }

    private static void addEntry(Biome biome, GeneratorEntry generator) {
        List<GeneratorEntry> entries = GENERATORS.get(biome);
        if (entries == null) {
            entries = new ArrayList<>();
            GENERATORS.put(biome, entries);
        }
        entries.add(generator);
    }

    private static class GeneratorEntry {
        private Class<? extends StructureGenerator> generator;
        private int chance;
        private Biome[] validBiomes;

        public GeneratorEntry(Class<? extends StructureGenerator> generator, Biome[] biomes, int chance) {
            this.generator = generator;
            this.validBiomes = biomes;
            this.chance = chance;
        }
    }
}
