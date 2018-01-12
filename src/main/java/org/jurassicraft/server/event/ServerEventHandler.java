package org.jurassicraft.server.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.achievements.AchievementHandler;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.FossilizedTrackwayBlock;
import org.jurassicraft.server.block.plant.DoublePlantBlock;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.util.GameRuleHandler;
import org.jurassicraft.server.world.WorldGenCoal;
import org.jurassicraft.server.world.loot.Loot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServerEventHandler {

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        GameRuleHandler.register(event.getWorld());
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        if (event.pickedUp.getItem().getItem() == ItemHandler.AMBER) {
            event.player.addStat(AchievementHandler.AMBER, 1);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onCraft(PlayerEvent.ItemCraftedEvent event) {
        Item item = event.crafting.getItem();

        if (item == ItemHandler.PLASTER_AND_BANDAGE) {
            event.player.addStat(AchievementHandler.PALEONTOLOGY, 1);
        } else if (item == Item.getItemFromBlock(BlockHandler.CLEANING_STATION)) {
            event.player.addStat(AchievementHandler.CLEANING_STATION, 1);
        } else if (item == Item.getItemFromBlock(BlockHandler.FOSSIL_GRINDER)) {
            event.player.addStat(AchievementHandler.FOSSIL_GRINDER, 1);
        } else if (item == Item.getItemFromBlock(BlockHandler.REINFORCED_STONE)) {
            event.player.addStat(AchievementHandler.REINFORCED_STONE, 1);
        } else if (item == Item.getItemFromBlock(BlockHandler.REINFORCED_BRICKS)) {
            event.player.addStat(AchievementHandler.REINFORCED_STONE, 1);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void decorate(DecorateBiomeEvent.Pre event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        Random rand = event.getRand();

        Biome biome = world.getBiome(pos);

        BiomeDecorator decorator = biome.decorator;

        if (JurassiCraft.CONFIG.plantFossilGeneration) {
            if (decorator != null && decorator.chunkProviderSettings != null && !(decorator.coalGen instanceof WorldGenCoal)) {
                decorator.coalGen = new WorldGenCoal(Blocks.COAL_ORE.getDefaultState(), decorator.chunkProviderSettings.coalSize);
            }
        }

        if (JurassiCraft.CONFIG.mossGeneration) {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.CONIFEROUS) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
                if (rand.nextInt(8) == 0) {
                    BlockPos topBlock = world.getTopSolidOrLiquidBlock(pos);

                    if (world.getBlockState(topBlock.down()).isOpaqueCube() && !world.getBlockState(topBlock).getMaterial().isLiquid()) {
                        world.setBlockState(topBlock, BlockHandler.MOSS.getDefaultState(), 2);
                    }
                }
            }
        }

        if (JurassiCraft.CONFIG.flowerGeneration) {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
                if (rand.nextInt(8) == 0) {
                    BlockPos topBlock = world.getTopSolidOrLiquidBlock(pos);
                    if (world.getBlockState(topBlock.down()).isOpaqueCube() && !world.getBlockState(topBlock).getMaterial().isLiquid()) {
                        world.setBlockState(topBlock.up(), BlockHandler.WEST_INDIAN_LILAC.getDefaultState(), 2);
                        world.setBlockState(topBlock, BlockHandler.WEST_INDIAN_LILAC.getDefaultState().withProperty(DoublePlantBlock.HALF, DoublePlantBlock.BlockHalf.LOWER), 2);
                    }
                }
            }
        }

        if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
            if (rand.nextInt(8) == 0) {
                BlockPos topBlock = world.getTopSolidOrLiquidBlock(pos);
                if (world.getBlockState(topBlock.down()).isOpaqueCube() && !world.getBlockState(topBlock).getMaterial().isLiquid()) {
                    world.setBlockState(topBlock.up(), BlockHandler.HELICONIA.getDefaultState(), 2);
                    world.setBlockState(topBlock, BlockHandler.HELICONIA.getDefaultState().withProperty(DoublePlantBlock.HALF, DoublePlantBlock.BlockHalf.LOWER), 2);
                }
            }
        }

        if (JurassiCraft.CONFIG.gracilariaGeneration) {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN)) {
                if (rand.nextInt(8) == 0) {
                    BlockPos topBlock = world.getTopSolidOrLiquidBlock(pos);

                    if (topBlock.getY() < 62) {
                        IBlockState state = world.getBlockState(topBlock.down());

                        if (state.isOpaqueCube()) {
                            world.setBlockState(topBlock, BlockHandler.GRACILARIA.getDefaultState(), 2);
                        }
                    }
                }
            }
        }

        if (JurassiCraft.CONFIG.peatGeneration) {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
                if (rand.nextInt(2) == 0) {
                    new WorldGenMinable(BlockHandler.PEAT.getDefaultState(), 5, input -> input == Blocks.DIRT.getDefaultState() || input == Blocks.GRASS.getDefaultState()).generate(world, rand, world.getTopSolidOrLiquidBlock(pos));
                }
            }
        }

        if (JurassiCraft.CONFIG.trackwayGeneration) {
            int footprintChance = 20;

            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.RIVER)) {
                footprintChance = 10;
            }

            if (rand.nextInt(footprintChance) == 0) {
                int y = rand.nextInt(20) + 30;

                FossilizedTrackwayBlock.TrackwayType type = FossilizedTrackwayBlock.TrackwayType.values()[rand.nextInt(FossilizedTrackwayBlock.TrackwayType.values().length)];

                for (int i = 0; i < rand.nextInt(2) + 1; i++) {
                    BlockPos basePos = new BlockPos(pos.getX() + rand.nextInt(10) - 5, y, pos.getZ() + rand.nextInt(10) - 5);

                    float angle = (float) (rand.nextDouble() * 360.0F);

                    IBlockState trackway = BlockHandler.FOSSILIZED_TRACKWAY.getDefaultState().withProperty(FossilizedTrackwayBlock.FACING, EnumFacing.fromAngle(angle)).withProperty(FossilizedTrackwayBlock.VARIANT, type);

                    float xOffset = -MathHelper.sin((float) Math.toRadians(angle));
                    float zOffset = MathHelper.cos((float) Math.toRadians(angle));

                    for (int l = 0; l < rand.nextInt(2) + 3; l++) {
                        BlockPos trackwayPos = basePos.add(xOffset * l, 0, zOffset * l);

                        if (world.getBlockState(trackwayPos).getBlock() == Blocks.STONE) {
                            world.setBlockState(trackwayPos, trackway);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation name = event.getName();

        LootTable table = event.getTable();

        Loot.handleTable(table, name);
    }

    @SubscribeEvent
    public void onHarvest(BlockEvent.HarvestDropsEvent event) {
        IBlockState state = event.getState();
        Random rand = event.getWorld().rand;
        if (rand.nextInt(2) == 0) {
            List<Item> bugs = new ArrayList<>();
            if (state.getBlock() == Blocks.HAY_BLOCK) {
                bugs.add(ItemHandler.COCKROACHES);
                bugs.add(ItemHandler.MEALWORM_BEETLES);
            } else if (state.getBlock() == Blocks.GRASS) {
                if (rand.nextInt(3) == 0) {
                    bugs.add(ItemHandler.CRICKETS);
                }
            } else if (state.getBlock() == Blocks.TALLGRASS) {
                if (rand.nextInt(4) == 0) {
                    bugs.add(ItemHandler.CRICKETS);
                }
            } else if (state.getBlock() == Blocks.PUMPKIN || state.getBlock() == Blocks.MELON_BLOCK) {
                bugs.add(ItemHandler.COCKROACHES);
                bugs.add(ItemHandler.MEALWORM_BEETLES);
            } else if (state.getBlock() == Blocks.COCOA) {
                bugs.add(ItemHandler.COCKROACHES);
                bugs.add(ItemHandler.MEALWORM_BEETLES);
            }
            if (bugs.size() > 0) {
                event.getDrops().add(new ItemStack(bugs.get(rand.nextInt(bugs.size()))));
            }
        }
    }
}
