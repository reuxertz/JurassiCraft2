package org.jurassicraft.client.render;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.MultipartStateMap;
import org.jurassicraft.client.model.animation.EntityAnimator;
import org.jurassicraft.client.model.animation.entity.BrachiosaurusAnimator;
import org.jurassicraft.client.model.animation.entity.CoelacanthAnimator;
import org.jurassicraft.client.model.animation.entity.DilophosaurusAnimator;
import org.jurassicraft.client.model.animation.entity.GallimimusAnimator;
import org.jurassicraft.client.model.animation.entity.MicroraptorAnimator;
import org.jurassicraft.client.model.animation.entity.MussaurusAnimator;
import org.jurassicraft.client.model.animation.entity.ParasaurolophusAnimator;
import org.jurassicraft.client.model.animation.entity.TriceratopsAnimator;
import org.jurassicraft.client.model.animation.entity.TyrannosaurusAnimator;
import org.jurassicraft.client.model.animation.entity.VelociraptorAnimator;
import org.jurassicraft.client.render.block.CleaningStationRenderer;
import org.jurassicraft.client.render.block.DNAExtractorRenderer;
import org.jurassicraft.client.render.block.DNASequencerRenderer;
import org.jurassicraft.client.render.block.DisplayBlockRenderer;
import org.jurassicraft.client.render.block.ElectricFencePoleRenderer;
import org.jurassicraft.client.render.block.FeederRenderer;
import org.jurassicraft.client.render.block.IncubatorRenderer;
import org.jurassicraft.client.render.entity.AttractionSignRenderer;
import org.jurassicraft.client.render.entity.DinosaurEggRenderer;
import org.jurassicraft.client.render.entity.FordExplorerRenderer;
import org.jurassicraft.client.render.entity.GoatRenderer;
import org.jurassicraft.client.render.entity.HelicopterRenderer;
import org.jurassicraft.client.render.entity.JeepWranglerRenderer;
import org.jurassicraft.client.render.entity.MuralRenderer;
import org.jurassicraft.client.render.entity.NullRenderer;
import org.jurassicraft.client.render.entity.PaddockSignRenderer;
import org.jurassicraft.client.render.entity.VenomRenderer;
import org.jurassicraft.client.render.entity.dinosaur.DinosaurRenderInfo;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.EncasedFossilBlock;
import org.jurassicraft.server.block.FossilBlock;
import org.jurassicraft.server.block.FossilizedTrackwayBlock;
import org.jurassicraft.server.block.NestFossilBlock;
import org.jurassicraft.server.block.entity.CleaningStationBlockEntity;
import org.jurassicraft.server.block.entity.DNAExtractorBlockEntity;
import org.jurassicraft.server.block.entity.DNASequencerBlockEntity;
import org.jurassicraft.server.block.entity.DisplayBlockEntity;
import org.jurassicraft.server.block.entity.ElectricFencePoleBlockEntity;
import org.jurassicraft.server.block.entity.FeederBlockEntity;
import org.jurassicraft.server.block.entity.IncubatorBlockEntity;
import org.jurassicraft.server.block.plant.AncientCoralBlock;
import org.jurassicraft.server.block.tree.AncientLeavesBlock;
import org.jurassicraft.server.block.tree.TreeType;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.entity.GoatEntity;
import org.jurassicraft.server.entity.TranquilizerDartEntity;
import org.jurassicraft.server.entity.VenomEntity;
import org.jurassicraft.server.entity.item.AttractionSignEntity;
import org.jurassicraft.server.entity.item.DinosaurEggEntity;
import org.jurassicraft.server.entity.item.MuralEntity;
import org.jurassicraft.server.entity.item.PaddockSignEntity;
import org.jurassicraft.server.entity.vehicle.FordExplorerEntity;
import org.jurassicraft.server.entity.vehicle.HelicopterBaseEntity;
import org.jurassicraft.server.entity.vehicle.JeepWranglerEntity;
import org.jurassicraft.server.item.DinosaurSpawnEggItem;
import org.jurassicraft.server.item.FossilItem;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.item.JournalItem;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.plant.PlantHandler;

import com.google.common.collect.Maps;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid=JurassiCraft.MODID)
public enum RenderingHandler {
    INSTANCE;

    private final Minecraft mc = Minecraft.getMinecraft();
    private static Map<Dinosaur, DinosaurRenderInfo> renderInfos = Maps.newHashMap();

    //TODO: CLEAN THIS UP OMG PLZ
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelEvent(final ModelRegistryEvent event)
    {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            registerItemRenderer(Item.getItemFromBlock(BlockHandler.CULTIVATOR_BOTTOM), color.getMetadata(), "cultivate/cultivate_bottom_" + color.getName().toLowerCase(Locale.ENGLISH));
        }

        for (TreeType type : TreeType.values()) {
            ModelLoader.setCustomStateMapper(BlockHandler.ANCIENT_FENCES.get(type), new MultipartStateMap());
            ModelLoader.setCustomStateMapper(BlockHandler.ANCIENT_FENCE_GATES.get(type), new StateMap.Builder().ignore(BlockFenceGate.POWERED).build());
            ModelLoader.setCustomStateMapper(BlockHandler.ANCIENT_DOORS.get(type), new StateMap.Builder().ignore(BlockDoor.POWERED).build());
        }
        ModelLoader.setCustomStateMapper(BlockHandler.ENALLHELIA, new StateMap.Builder().ignore(AncientCoralBlock.LEVEL).build());
        ModelLoader.setCustomStateMapper(BlockHandler.AULOPORA, new StateMap.Builder().ignore(AncientCoralBlock.LEVEL).build());
        ModelLoader.setCustomStateMapper(BlockHandler.CLADOCHONUS, new StateMap.Builder().ignore(AncientCoralBlock.LEVEL).build());
        ModelLoader.setCustomStateMapper(BlockHandler.LITHOSTROTION, new StateMap.Builder().ignore(AncientCoralBlock.LEVEL).build());
        ModelLoader.setCustomStateMapper(BlockHandler.STYLOPHYLLOPSIS, new StateMap.Builder().ignore(AncientCoralBlock.LEVEL).build());
        ModelLoader.setCustomStateMapper(BlockHandler.HIPPURITES_RADIOSUS, new StateMap.Builder().ignore(AncientCoralBlock.LEVEL).build());

        ModelLoader.setCustomStateMapper(BlockHandler.LOW_SECURITY_FENCE_BASE, new MultipartStateMap());
        ModelLoader.setCustomStateMapper(BlockHandler.LOW_SECURITY_FENCE_POLE, new MultipartStateMap());
        ModelLoader.setCustomStateMapper(BlockHandler.LOW_SECURITY_FENCE_WIRE, new MultipartStateMap());

        int i = 0;

        for (EncasedFossilBlock fossil : BlockHandler.ENCASED_FOSSILS) {
            for (int meta = 0; meta < 16; meta++) {
                registerBlockRenderer(fossil, meta, "encased_fossil_" + i);
            }

            i++;
        }

        i = 0;

        for (FossilBlock fossil : BlockHandler.FOSSILS) {
            for (int meta = 0; meta < 16; meta++) {
                registerBlockRenderer(fossil, meta, "fossil_block_" + i);
            }

            i++;
        }

        registerBlockRenderer(BlockHandler.PLANT_FOSSIL, "plant_fossil_block");

        for (TreeType type : TreeType.values()) {
            String name = type.name().toLowerCase(Locale.ENGLISH);
            registerBlockRenderer(BlockHandler.ANCIENT_LEAVES.get(type), name + "_leaves");
            registerBlockRenderer(BlockHandler.ANCIENT_SAPLINGS.get(type), name + "_sapling");
            registerBlockRenderer(BlockHandler.ANCIENT_PLANKS.get(type), name + "_planks");
            registerBlockRenderer(BlockHandler.ANCIENT_LOGS.get(type), name + "_log");
            registerBlockRenderer(BlockHandler.ANCIENT_STAIRS.get(type), name + "_stairs");
            registerBlockRenderer(BlockHandler.ANCIENT_SLABS.get(type), name + "_slab");
            registerBlockRenderer(BlockHandler.ANCIENT_DOUBLE_SLABS.get(type), name + "_double_slab");
            registerBlockRenderer(BlockHandler.ANCIENT_FENCES.get(type), name + "_fence");
            registerBlockRenderer(BlockHandler.ANCIENT_FENCE_GATES.get(type), name + "_fence_gate");
            registerBlockRenderer(BlockHandler.PETRIFIED_LOGS.get(type), name + "_log_petrified");
        }

        /*for (EnumDyeColor color : EnumDyeColor.values()) {
            registerBlockRenderer(BlockHandler.CULTIVATOR_BOTTOM, color.ordinal(), "cultivate/cultivate_bottom_" + color.getName().toLowerCase(Locale.ENGLISH));
        }*/

        registerBlockRenderer(BlockHandler.SCALY_TREE_FERN, "scaly_tree_fern");
        registerBlockRenderer(BlockHandler.SMALL_ROYAL_FERN, "small_royal_fern");
        registerBlockRenderer(BlockHandler.SMALL_CHAIN_FERN, "small_chain_fern");
        registerBlockRenderer(BlockHandler.SMALL_CYCAD, "small_cycad");
        registerBlockRenderer(BlockHandler.CYCADEOIDEA, "bennettitalean_cycadeoidea");
        registerBlockRenderer(BlockHandler.CRY_PANSY, "cry_pansy");
        registerBlockRenderer(BlockHandler.ZAMITES, "cycad_zamites");
        registerBlockRenderer(BlockHandler.DICKSONIA, "dicksonia");
        registerBlockRenderer(BlockHandler.WOOLLY_STALKED_BEGONIA, "woolly_stalked_begonia");
        registerBlockRenderer(BlockHandler.LARGESTIPULE_LEATHER_ROOT, "largestipule_leather_root");
        registerBlockRenderer(BlockHandler.RHACOPHYTON, "rhacophyton");
        registerBlockRenderer(BlockHandler.GRAMINIDITES_BAMBUSOIDES, "graminidites_bambusoides");
        registerBlockRenderer(BlockHandler.ENALLHELIA, "enallhelia");
        registerBlockRenderer(BlockHandler.AULOPORA, "aulopora");
        registerBlockRenderer(BlockHandler.CLADOCHONUS, "cladochonus");
        registerBlockRenderer(BlockHandler.LITHOSTROTION, "lithostrotion");
        registerBlockRenderer(BlockHandler.STYLOPHYLLOPSIS, "stylophyllopsis");
        registerBlockRenderer(BlockHandler.HIPPURITES_RADIOSUS, "hippurites_radiosus");
        registerBlockRenderer(BlockHandler.HELICONIA, "heliconia");

        registerBlockRenderer(BlockHandler.REINFORCED_STONE, "reinforced_stone");
        registerBlockRenderer(BlockHandler.REINFORCED_BRICKS, "reinforced_bricks");

        registerBlockRenderer(BlockHandler.CULTIVATOR_BOTTOM, "cultivate_bottom");
        registerBlockRenderer(BlockHandler.CULTIVATOR_TOP, "cultivate_top");

        registerBlockRenderer(BlockHandler.AMBER_ORE, "amber_ore");
        registerBlockRenderer(BlockHandler.ICE_SHARD, "ice_shard");
        registerBlockRenderer(BlockHandler.CLEANING_STATION, "cleaning_station");
        registerBlockRenderer(BlockHandler.FOSSIL_GRINDER, "fossil_grinder");
        registerBlockRenderer(BlockHandler.DNA_SEQUENCER, "dna_sequencer");
        registerBlockRenderer(BlockHandler.DNA_COMBINATOR_HYBRIDIZER, "dna_combinator_hybridizer");
        registerBlockRenderer(BlockHandler.DNA_SYNTHESIZER, "dna_synthesizer");
        registerBlockRenderer(BlockHandler.EMBRYONIC_MACHINE, "embryonic_machine");
        registerBlockRenderer(BlockHandler.EMBRYO_CALCIFICATION_MACHINE, "embryo_calcification_machine");
        registerBlockRenderer(BlockHandler.INCUBATOR, "incubator");
        registerBlockRenderer(BlockHandler.DNA_EXTRACTOR, "dna_extractor");
        registerBlockRenderer(BlockHandler.FEEDER, "feeder");
        registerBlockRenderer(BlockHandler.SKELETON_ASSEMBLY, "skeleton_assembly");
        registerBlockRenderer(BlockHandler.GYPSUM_STONE, "gypsum_stone");
        registerBlockRenderer(BlockHandler.GYPSUM_COBBLESTONE, "gypsum_cobblestone");
        registerBlockRenderer(BlockHandler.GYPSUM_BRICKS, "gypsum_bricks");
        registerBlockRenderer(BlockHandler.DISPLAY_BLOCK, "display_block");

        registerBlockRenderer(BlockHandler.MOSS, "moss");
        registerBlockRenderer(BlockHandler.CLEAR_GLASS, "clear_glass");

        registerBlockRenderer(BlockHandler.WILD_ONION, "wild_onion_plant");
        registerBlockRenderer(BlockHandler.GRACILARIA, "gracilaria_seaweed");
        registerBlockRenderer(BlockHandler.PEAT, "peat");
        registerBlockRenderer(BlockHandler.PEAT_MOSS, "peat_moss");
        registerBlockRenderer(BlockHandler.DICROIDIUM_ZUBERI, "dicroidium_zuberi");
        registerBlockRenderer(BlockHandler.WEST_INDIAN_LILAC, "west_indian_lilac");
        registerBlockRenderer(BlockHandler.DICTYOPHYLLUM, "dictyophyllum");
        registerBlockRenderer(BlockHandler.SERENNA_VERIFORMANS, "serenna_veriformans");
        registerBlockRenderer(BlockHandler.LADINIA_SIMPLEX, "ladinia_simplex");
        registerBlockRenderer(BlockHandler.ORONTIUM_MACKII, "orontium_mackii");
        registerBlockRenderer(BlockHandler.UMALTOLEPIS, "umaltolepis");
        registerBlockRenderer(BlockHandler.LIRIODENDRITES, "liriodendrites");
        registerBlockRenderer(BlockHandler.RAPHAELIA, "raphaelia");
        registerBlockRenderer(BlockHandler.ENCEPHALARTOS, "encephalartos");

        for (FossilizedTrackwayBlock.TrackwayType trackwayType : FossilizedTrackwayBlock.TrackwayType.values()) {
            registerBlockRenderer(BlockHandler.FOSSILIZED_TRACKWAY, trackwayType.ordinal(), "fossilized_trackway_" + trackwayType.getName());
        }

        for (NestFossilBlock.Variant variant : NestFossilBlock.Variant.values()) {
            registerBlockRenderer(BlockHandler.NEST_FOSSIL, variant.ordinal(), "nest_fossil_" + (variant.ordinal() + 1));
            registerBlockRenderer(BlockHandler.ENCASED_NEST_FOSSIL, variant.ordinal(), "encased_nest_fossil");
        }

        registerBlockRenderer(BlockHandler.PALEO_BALE_CYCADEOIDEA, "paleo_bale_cycadeoidea");
        registerBlockRenderer(BlockHandler.PALEO_BALE_CYCAD, "paleo_bale_cycad");
        registerBlockRenderer(BlockHandler.PALEO_BALE_FERN, "paleo_bale_fern");
        registerBlockRenderer(BlockHandler.PALEO_BALE_LEAVES, "paleo_bale_leaves");
        registerBlockRenderer(BlockHandler.PALEO_BALE_OTHER, "paleo_bale_other");

        registerBlockRenderer(BlockHandler.AJUGINUCULA_SMITHII);
        registerBlockRenderer(BlockHandler.BUG_CRATE);

        registerBlockRenderer(BlockHandler.PLANKTON_SWARM);
        registerBlockRenderer(BlockHandler.KRILL_SWARM);

        registerBlockRenderer(BlockHandler.LOW_SECURITY_FENCE_POLE);
        registerBlockRenderer(BlockHandler.LOW_SECURITY_FENCE_BASE);
        registerBlockRenderer(BlockHandler.LOW_SECURITY_FENCE_WIRE);

        registerBlockRenderer(BlockHandler.WILD_POTATO_PLANT);

        registerBlockRenderer(BlockHandler.RHAMNUS_SALICIFOLIUS_PLANT);

        registerBlockRenderer(BlockHandler.TEMPSKYA);
        registerBlockRenderer(BlockHandler.CINNAMON_FERN);
        registerBlockRenderer(BlockHandler.BRISTLE_FERN);

//        registerBlockRenderer(BlockHandler.TOUR_RAIL);
//        registerBlockRenderer(BlockHandler.TOUR_RAIL_POWERED);

        registerItemRenderer(ItemHandler.TRACKER);
        registerItemRenderer(ItemHandler.PLANT_CELLS_PETRI_DISH);
        registerItemRenderer(ItemHandler.PLANT_CELLS);
        registerItemRenderer(ItemHandler.GROWTH_SERUM);
        registerItemRenderer(ItemHandler.IRON_ROD);
        registerItemRenderer(ItemHandler.IRON_BLADES);
        registerItemRenderer(ItemHandler.PETRI_DISH);
        registerItemRenderer(ItemHandler.PETRI_DISH_AGAR);
        registerItemRenderer(ItemHandler.PLASTER_AND_BANDAGE);

        registerItemRenderer(ItemHandler.FUN_FRIES);
        registerItemRenderer(ItemHandler.OILED_POTATO_STRIPS);
        registerItemRenderer(ItemHandler.LUNCH_BOX);
        registerItemRenderer(ItemHandler.STAMP_SET);

        registerItemRenderer(ItemHandler.INGEN_JOURNAL);

        for (Entry<Integer, Dinosaur> entry : EntityHandler.getDinosaurs().entrySet()) {
            registerItemRenderer(ItemHandler.SPAWN_EGG, entry.getKey(), "dino_spawn_egg");
        }

        registerItemRenderer(ItemHandler.PADDOCK_SIGN);

        for (AttractionSignEntity.AttractionSignType type : AttractionSignEntity.AttractionSignType.values()) {
            registerItemRenderer(ItemHandler.ATTRACTION_SIGN, type.ordinal(), "attraction_sign_" + type.name().toLowerCase(Locale.ENGLISH));
        }

        registerItemRenderer(ItemHandler.EMPTY_TEST_TUBE);
        registerItemRenderer(ItemHandler.EMPTY_SYRINGE);
        registerItemRenderer(ItemHandler.STORAGE_DISC);
        registerItemRenderer(ItemHandler.DISC_DRIVE, "disc_reader");
        registerItemRenderer(ItemHandler.LASER);
        registerItemRenderer(ItemHandler.DNA_NUCLEOTIDES, "dna_base_material");
        registerItemRenderer(ItemHandler.SEA_LAMPREY);

        registerItemRenderer(ItemHandler.AMBER, 0, "amber_mosquito");
        registerItemRenderer(ItemHandler.AMBER, 1, "amber_aphid");

        registerItemRenderer(ItemHandler.HELICOPTER, "helicopter_spawner");

        registerItemRenderer(ItemHandler.JURASSICRAFT_THEME_DISC, "disc_jurassicraft_theme");
        registerItemRenderer(ItemHandler.DONT_MOVE_A_MUSCLE_DISC, "disc_dont_move_a_muscle");
        registerItemRenderer(ItemHandler.TROODONS_AND_RAPTORS_DISC, "disc_troodons_and_raptors");

        registerItemRenderer(ItemHandler.AMBER_KEYCHAIN, "amber_keychain");
        registerItemRenderer(ItemHandler.AMBER_CANE, "amber_cane");
        registerItemRenderer(ItemHandler.MR_DNA_KEYCHAIN, "mr_dna_keychain");

        registerItemRenderer(ItemHandler.DINO_SCANNER, "dino_scanner");

        registerItemRenderer(ItemHandler.BASIC_CIRCUIT, "basic_circuit");
        registerItemRenderer(ItemHandler.ADVANCED_CIRCUIT, "advanced_circuit");
        registerItemRenderer(ItemHandler.IRON_NUGGET, "iron_nugget");

        registerItemRenderer(ItemHandler.GYPSUM_POWDER, "gypsum_powder");

        registerItemRenderer(ItemHandler.AJUGINUCULA_SMITHII_SEEDS, "ajuginucula_smithii_seeds");
        registerItemRenderer(ItemHandler.AJUGINUCULA_SMITHII_LEAVES, "ajuginucula_smithii_leaves");
        registerItemRenderer(ItemHandler.AJUGINUCULA_SMITHII_OIL, "ajuginucula_smithii_oil");

        registerItemRenderer(ItemHandler.WILD_ONION, "wild_onion");
        registerItemRenderer(ItemHandler.GRACILARIA);
        registerItemRenderer(ItemHandler.LIQUID_AGAR, "liquid_agar");

        registerItemRenderer(ItemHandler.PLANT_FOSSIL, "plant_fossil");
        registerItemRenderer(ItemHandler.TWIG_FOSSIL, "twig_fossil");

        registerItemRenderer(ItemHandler.KEYBOARD, "keyboard");
        registerItemRenderer(ItemHandler.COMPUTER_SCREEN, "computer_screen");
        registerItemRenderer(ItemHandler.DNA_ANALYZER, "dna_analyzer");

        registerItemRenderer(ItemHandler.CHILEAN_SEA_BASS, "chilean_sea_bass");
        registerItemRenderer(ItemHandler.FIELD_GUIDE, "field_guide");

        registerItemRenderer(ItemHandler.CAR_CHASSIS, "car_chassis");
        registerItemRenderer(ItemHandler.CAR_ENGINE_SYSTEM, "car_engine_system");
        registerItemRenderer(ItemHandler.CAR_SEATS, "car_seats");
        registerItemRenderer(ItemHandler.CAR_TIRE, "car_tire");
        registerItemRenderer(ItemHandler.CAR_WINDSCREEN, "car_windscreen");
        registerItemRenderer(ItemHandler.UNFINISHED_CAR, "unfinished_car");
        registerItemRenderer(ItemHandler.JEEP_WRANGLER, "jeep_wrangler");
//        registerItemRenderer(ItemHandler.FORD_EXPLORER, "ford_explorer");

        registerItemRenderer(ItemHandler.MURAL, "mural");

        for (Dinosaur dinosaur : EntityHandler.getDinosaurs().values()) {
            int meta = EntityHandler.getDinosaurId(dinosaur);

            String formattedName = dinosaur.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

            for (Map.Entry<String, FossilItem> entry : ItemHandler.FOSSILS.entrySet()) {
                List<Dinosaur> dinosaursForType = FossilItem.fossilDinosaurs.get(entry.getKey());
                if (dinosaursForType.contains(dinosaur)) {
                    registerItemRenderer(entry.getValue(), meta, "bones/" + formattedName + "_" + entry.getKey());
                }
            }

            for (Map.Entry<String, FossilItem> entry : ItemHandler.FRESH_FOSSILS.entrySet()) {
                List<Dinosaur> dinosaursForType = FossilItem.fossilDinosaurs.get(entry.getKey());
                if (dinosaursForType.contains(dinosaur)) {
                    registerItemRenderer(entry.getValue(), meta, "fresh_bones/" + formattedName + "_" + entry.getKey());
                }
            }

            registerItemRenderer(ItemHandler.DNA, meta, "dna/dna_" + formattedName);
            registerItemRenderer(ItemHandler.DINOSAUR_MEAT, meta, "meat/meat_" + formattedName);
            registerItemRenderer(ItemHandler.DINOSAUR_STEAK, meta, "meat/steak_" + formattedName);
            registerItemRenderer(ItemHandler.SOFT_TISSUE, meta, "soft_tissue/soft_tissue_" + formattedName);
            registerItemRenderer(ItemHandler.SYRINGE, meta, "syringe/syringe_" + formattedName);
            //registerItemRenderer(ItemHandler.ACTION_FIGURE, meta, "action_figure/action_figure_" + formattedName);

            if (!dinosaur.givesDirectBirth()) {
                registerItemRenderer(ItemHandler.EGG, meta, "egg/egg_" + formattedName);
            }

            registerItemRenderer(ItemHandler.HATCHED_EGG, meta, "hatched_egg/egg_" + formattedName);
        }

        for (Plant plant : PlantHandler.getPrehistoricPlantsAndTrees()) {
            int meta = PlantHandler.getPlantId(plant);

            String name = plant.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

            registerItemRenderer(ItemHandler.PLANT_DNA, meta, "dna/plants/dna_" + name);
            registerItemRenderer(ItemHandler.PLANT_SOFT_TISSUE, meta, "soft_tissue/plants/soft_tissue_" + name);
            registerItemRenderer(ItemHandler.PLANT_CALLUS, meta, "plant_callus");
        }

        for (JournalItem.JournalType type : JournalItem.JournalType.values()) {
            registerItemRenderer(ItemHandler.INGEN_JOURNAL, type.getMetadata(), "ingen_journal");
        }

        for (NestFossilBlock.Variant variant : NestFossilBlock.Variant.values()) {
            registerItemRenderer(ItemHandler.FOSSILIZED_EGG, variant.ordinal(), "fossilized_egg_" + (variant.ordinal() + 1));
        }

        for (TreeType type : TreeType.values()) {
            String name = type.name().toLowerCase(Locale.ENGLISH);
            registerItemRenderer(ItemHandler.ANCIENT_DOORS.get(type), name + "_door_item");
        }

        registerItemRenderer(ItemHandler.PHOENIX_SEEDS);
        registerItemRenderer(ItemHandler.PHOENIX_FRUIT);

        registerItemRenderer(ItemHandler.CRICKETS);
        registerItemRenderer(ItemHandler.COCKROACHES);
        registerItemRenderer(ItemHandler.MEALWORM_BEETLES);

        registerItemRenderer(ItemHandler.FINE_NET);
        registerItemRenderer(ItemHandler.PLANKTON);
        registerItemRenderer(ItemHandler.KRILL);

        registerItemRenderer(ItemHandler.WILD_POTATO_SEEDS);
        registerItemRenderer(ItemHandler.WILD_POTATO);
        registerItemRenderer(ItemHandler.WILD_POTATO_COOKED);

        registerItemRenderer(ItemHandler.RHAMNUS_SEEDS);
        registerItemRenderer(ItemHandler.RHAMNUS_BERRIES);

        registerItemRenderer(ItemHandler.GOAT_RAW);
        registerItemRenderer(ItemHandler.GOAT_COOKED);
        
        registerItemRenderer(ItemHandler.DART_GUN);
        registerItemRenderer(ItemHandler.DART_TRANQUILIZER);
    }

    public void preInit() {
        TabulaModelHandler.INSTANCE.addDomain(JurassiCraft.MODID);
        ItemHandler.DISPLAY_BLOCK.initModels(EntityHandler.getDinosaurs().values(), this);

        registerRenderInfo(EntityHandler.BRACHIOSAURUS, new BrachiosaurusAnimator(), 1.5F);
        registerRenderInfo(EntityHandler.COELACANTH, new CoelacanthAnimator(), 0.0F);
        registerRenderInfo(EntityHandler.DILOPHOSAURUS, new DilophosaurusAnimator(), 0.65F);
        registerRenderInfo(EntityHandler.GALLIMIMUS, new GallimimusAnimator(), 0.65F);
        registerRenderInfo(EntityHandler.PARASAUROLOPHUS, new ParasaurolophusAnimator(), 0.65F);
        registerRenderInfo(EntityHandler.MICRORAPTOR, new MicroraptorAnimator(), 0.45F);
        registerRenderInfo(EntityHandler.MUSSAURUS, new MussaurusAnimator(), 0.8F);
        registerRenderInfo(EntityHandler.TRICERATOPS, new TriceratopsAnimator(), 0.65F);
        registerRenderInfo(EntityHandler.TYRANNOSAURUS, new TyrannosaurusAnimator(), 0.65F);
        registerRenderInfo(EntityHandler.VELOCIRAPTOR, new VelociraptorAnimator(), 0.45F);
        //registerRenderInfo(EntityHandler.STEGOSAURUS, new StegosaurusAnimator(), 0.65F);

        RenderingRegistry.registerEntityRenderingHandler(PaddockSignEntity.class, new PaddockSignRenderer());
        RenderingRegistry.registerEntityRenderingHandler(AttractionSignEntity.class, new AttractionSignRenderer());
        RenderingRegistry.registerEntityRenderingHandler(HelicopterBaseEntity.class, new HelicopterRenderer());
        RenderingRegistry.registerEntityRenderingHandler(DinosaurEggEntity.class, new DinosaurEggRenderer());
        RenderingRegistry.registerEntityRenderingHandler(VenomEntity.class, new VenomRenderer());
        RenderingRegistry.registerEntityRenderingHandler(JeepWranglerEntity.class, JeepWranglerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(FordExplorerEntity.class, FordExplorerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(HelicopterBaseEntity.class, new HelicopterRenderer());
        RenderingRegistry.registerEntityRenderingHandler(MuralEntity.class, MuralRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(GoatEntity.class, GoatRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TranquilizerDartEntity.class, NullRenderer::new);
        
    }

    public void init() {
        BlockColors blockColors = mc.getBlockColors();
        blockColors.registerBlockColorHandler((state, access, pos, tintIndex) -> pos != null ? BiomeColorHelper.getGrassColorAtPos(access, pos) : 0xFFFFFF, BlockHandler.MOSS);

        for (Map.Entry<TreeType, AncientLeavesBlock> entry : BlockHandler.ANCIENT_LEAVES.entrySet()) {
            blockColors.registerBlockColorHandler((state, access, pos, tintIndex) -> pos == null ? ColorizerFoliage.getFoliageColorBasic() : BiomeColorHelper.getFoliageColorAtPos(access, pos), entry.getValue());
        }

        blockColors.registerBlockColorHandler((state, access, pos, tintIndex) -> pos == null ? ColorizerFoliage.getFoliageColorBasic() : BiomeColorHelper.getFoliageColorAtPos(access, pos), BlockHandler.MOSS);

        ItemColors itemColors = mc.getItemColors();

        for (Map.Entry<TreeType, AncientLeavesBlock> entry : BlockHandler.ANCIENT_LEAVES.entrySet()) {
            itemColors.registerItemColorHandler((stack, tintIndex) -> ColorizerFoliage.getFoliageColorBasic(), entry.getValue());
        }

        itemColors.registerItemColorHandler((stack, tintIndex) -> {
            DinosaurSpawnEggItem item = (DinosaurSpawnEggItem) stack.getItem();
            Dinosaur dino = item.getDinosaur(stack);
            if (dino != null) {
                int mode = item.getMode(stack);
                if (mode == 0) {
                    mode = JurassiCraft.timerTicks % 64 > 32 ? 1 : 2;
                }
                if (mode == 1) {
                    return tintIndex == 0 ? dino.getEggPrimaryColorMale() : dino.getEggSecondaryColorMale();
                } else {
                    return tintIndex == 0 ? dino.getEggPrimaryColorFemale() : dino.getEggSecondaryColorFemale();
                }
            }
            return 0xFFFFFF;
        }, ItemHandler.SPAWN_EGG);
    }

    public void postInit() {
        ClientRegistry.bindTileEntitySpecialRenderer(DNAExtractorBlockEntity.class, new DNAExtractorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(DisplayBlockEntity.class, new DisplayBlockRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(DNASequencerBlockEntity.class, new DNASequencerRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(IncubatorBlockEntity.class, new IncubatorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(FeederBlockEntity.class, new FeederRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(ElectricFencePoleBlockEntity.class, new ElectricFencePoleRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(CleaningStationBlockEntity.class, new CleaningStationRenderer());
    }

    public static void registerItemRenderer(Item item) {
        registerItemRenderer(item, item.getUnlocalizedName().substring("item.".length()));
    }

    public static void registerBlockRenderer(Block block) {
        registerBlockRenderer(block, block.getUnlocalizedName().substring("tile.".length()));
    }

    public static void registerItemRenderer(Item item, int meta, String path) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(JurassiCraft.MODID + ":" + path, "inventory"));
    }

    public static void registerItemRenderer(Item item, String path) {
        registerItemRenderer(item, 0, path);
    }

    public static void registerBlockRenderer(Block block, int meta, String path) {
        registerItemRenderer(Item.getItemFromBlock(block), meta, path);
    }

    public static void registerBlockRenderer(Block block, String path) {
        registerBlockRenderer(block, 0, path);
    }

    private static void registerRenderInfo(Dinosaur dinosaur, EntityAnimator<?> animator, float shadowSize) {
        registerRenderInfo(new DinosaurRenderInfo(dinosaur, animator, shadowSize));
    }

    private static void registerRenderInfo(DinosaurRenderInfo renderDef) {
        renderInfos.put(renderDef.getDinosaur(), renderDef);
        RenderingRegistry.registerEntityRenderingHandler(renderDef.getDinosaur().getDinosaurClass(), renderDef);
    }

    public DinosaurRenderInfo getRenderInfo(Dinosaur dino) {
        return this.renderInfos.get(dino);
    }
}