package org.jurassicraft.client.render;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemBlock;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.MultipartStateMap;
import org.jurassicraft.client.model.animation.EntityAnimator;
import org.jurassicraft.client.model.animation.entity.AlligatorGarAnimator;
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
import org.jurassicraft.server.block.*;
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
import org.jurassicraft.server.item.*;
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
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static org.jurassicraft.server.item.ItemHandler.*;
import static org.jurassicraft.server.block.BlockHandler.*;

@SideOnly(Side.CLIENT)
public enum RenderingHandler {
    INSTANCE;

    private final Minecraft mc = Minecraft.getMinecraft();
    private Map<Dinosaur, DinosaurRenderInfo> renderInfos = Maps.newHashMap();

    public void preInit() {
        TabulaModelHandler.INSTANCE.addDomain(JurassiCraft.MODID);

        for (EnumDyeColor color : EnumDyeColor.values()) {
            this.registerItemRenderer(Item.getItemFromBlock(CULTIVATOR_BOTTOM), color.getMetadata(), "cultivate/cultivate_bottom_" + color.getName().toLowerCase(Locale.ENGLISH));
        }

        for (TreeType type : TreeType.values()) {
            ModelLoader.setCustomStateMapper(ANCIENT_FENCES.get(type), new MultipartStateMap());
            ModelLoader.setCustomStateMapper(ANCIENT_FENCE_GATES.get(type), new StateMap.Builder().ignore(BlockFenceGate.POWERED).build());
            ModelLoader.setCustomStateMapper(BlockHandler.ANCIENT_DOORS.get(type), new StateMap.Builder().ignore(BlockDoor.POWERED).build());
        }

        ModelLoader.setCustomStateMapper(ENALLHELIA, new StateMap.Builder().ignore(AncientCoralBlock.LEVEL).build());
        ModelLoader.setCustomStateMapper(AULOPORA, new StateMap.Builder().ignore(AncientCoralBlock.LEVEL).build());
        ModelLoader.setCustomStateMapper(CLADOCHONUS, new StateMap.Builder().ignore(AncientCoralBlock.LEVEL).build());
        ModelLoader.setCustomStateMapper(LITHOSTROTION, new StateMap.Builder().ignore(AncientCoralBlock.LEVEL).build());
        ModelLoader.setCustomStateMapper(STYLOPHYLLOPSIS, new StateMap.Builder().ignore(AncientCoralBlock.LEVEL).build());
        ModelLoader.setCustomStateMapper(HIPPURITES_RADIOSUS, new StateMap.Builder().ignore(AncientCoralBlock.LEVEL).build());

        ModelLoader.setCustomStateMapper(LOW_SECURITY_FENCE_BASE, new MultipartStateMap());
        ModelLoader.setCustomStateMapper(LOW_SECURITY_FENCE_POLE, new MultipartStateMap());
        ModelLoader.setCustomStateMapper(LOW_SECURITY_FENCE_WIRE, new MultipartStateMap());
        ModelLoader.setCustomStateMapper(MED_SECURITY_FENCE_BASE, new MultipartStateMap());
        ModelLoader.setCustomStateMapper(MED_SECURITY_FENCE_POLE, new MultipartStateMap());
        ModelLoader.setCustomStateMapper(MED_SECURITY_FENCE_WIRE, new MultipartStateMap());
        ModelLoader.setCustomStateMapper(HIGH_SECURITY_FENCE_BASE, new MultipartStateMap());
        ModelLoader.setCustomStateMapper(HIGH_SECURITY_FENCE_POLE, new MultipartStateMap());
        ModelLoader.setCustomStateMapper(HIGH_SECURITY_FENCE_WIRE, new MultipartStateMap());

        ItemHandler.DISPLAY_BLOCK.initModels(EntityHandler.getDinosaurs().values(), this);

        int i = 0;

        for (EncasedFossilBlock fossil : ENCASED_FOSSILS) {
            for (int meta = 0; meta < 16; meta++) {
                this.registerBlockRenderer(fossil, meta, "encased_fossil_" + i);
            }

            i++;
        }

        i = 0;

        for (FossilBlock fossil : BlockHandler.FOSSILS) {
            for (int meta = 0; meta < 16; meta++) {
                this.registerBlockRenderer(fossil, meta, "fossil_block_" + i);
            }

            i++;
        }

        this.registerBlockRenderer(BlockHandler.PLANT_FOSSIL, "plant_fossil_block");

        for (TreeType type : TreeType.values()) {
            String name = type.name().toLowerCase(Locale.ENGLISH);
            this.registerBlockRenderer(ANCIENT_LEAVES.get(type), name + "_leaves");
            this.registerBlockRenderer(ANCIENT_SAPLINGS.get(type), name + "_sapling");
            this.registerBlockRenderer(ANCIENT_PLANKS.get(type), name + "_planks");
            this.registerBlockRenderer(ANCIENT_LOGS.get(type), name + "_log");
            this.registerBlockRenderer(ANCIENT_STAIRS.get(type), name + "_stairs");
            this.registerBlockRenderer(ANCIENT_SLABS.get(type), name + "_slab");
            this.registerBlockRenderer(ANCIENT_DOUBLE_SLABS.get(type), name + "_double_slab");
            this.registerBlockRenderer(ANCIENT_FENCES.get(type), name + "_fence");
            this.registerBlockRenderer(ANCIENT_FENCE_GATES.get(type), name + "_fence_gate");
            this.registerBlockRenderer(PETRIFIED_LOGS.get(type), name + "_log_petrified");
        }

        /*for (EnumDyeColor color : EnumDyeColor.values()) {
            this.registerBlockRenderer(CULTIVATOR_BOTTOM, color.ordinal(), "cultivate/cultivate_bottom_" + color.getName().toLowerCase(Locale.ENGLISH));
        }*/

        this.registerBlockRenderer(SCALY_TREE_FERN, "scaly_tree_fern");
        this.registerBlockRenderer(SMALL_ROYAL_FERN, "small_royal_fern");
        this.registerBlockRenderer(SMALL_CHAIN_FERN, "small_chain_fern");
        this.registerBlockRenderer(SMALL_CYCAD, "small_cycad");
        this.registerBlockRenderer(CYCADEOIDEA, "bennettitalean_cycadeoidea");
        this.registerBlockRenderer(CRY_PANSY, "cry_pansy");
        this.registerBlockRenderer(ZAMITES, "cycad_zamites");
        this.registerBlockRenderer(DICKSONIA, "dicksonia");
        this.registerBlockRenderer(WOOLLY_STALKED_BEGONIA, "woolly_stalked_begonia");
        this.registerBlockRenderer(LARGESTIPULE_LEATHER_ROOT, "largestipule_leather_root");
        this.registerBlockRenderer(RHACOPHYTON, "rhacophyton");
        this.registerBlockRenderer(GRAMINIDITES_BAMBUSOIDES, "graminidites_bambusoides");
        this.registerBlockRenderer(ENALLHELIA, "enallhelia");
        this.registerBlockRenderer(AULOPORA, "aulopora");
        this.registerBlockRenderer(CLADOCHONUS, "cladochonus");
        this.registerBlockRenderer(LITHOSTROTION, "lithostrotion");
        this.registerBlockRenderer(STYLOPHYLLOPSIS, "stylophyllopsis");
        this.registerBlockRenderer(HIPPURITES_RADIOSUS, "hippurites_radiosus");
        this.registerBlockRenderer(HELICONIA, "heliconia");

        this.registerBlockRenderer(REINFORCED_STONE, "reinforced_stone");
        this.registerBlockRenderer(REINFORCED_BRICKS, "reinforced_bricks");

        this.registerBlockRenderer(CULTIVATOR_BOTTOM, "cultivate_bottom");
        this.registerBlockRenderer(CULTIVATOR_TOP, "cultivate_top");

        this.registerBlockRenderer(AMBER_ORE, "amber_ore");
        this.registerBlockRenderer(ICE_SHARD, "ice_shard");
        this.registerBlockRenderer(CLEANING_STATION, "cleaning_station");
        this.registerBlockRenderer(FOSSIL_GRINDER, "fossil_grinder");
        this.registerBlockRenderer(DNA_SEQUENCER, "dna_sequencer");
        this.registerBlockRenderer(DNA_COMBINATOR_HYBRIDIZER, "dna_combinator_hybridizer");
        this.registerBlockRenderer(DNA_SYNTHESIZER, "dna_synthesizer");
        this.registerBlockRenderer(EMBRYONIC_MACHINE, "embryonic_machine");
        this.registerBlockRenderer(EMBRYO_CALCIFICATION_MACHINE, "embryo_calcification_machine");
        this.registerBlockRenderer(INCUBATOR, "incubator");
        this.registerBlockRenderer(DNA_EXTRACTOR, "dna_extractor");
        this.registerBlockRenderer(FEEDER, "feeder");
        this.registerBlockRenderer(SKELETON_ASSEMBLY, "skeleton_assembly");
        this.registerBlockRenderer(GYPSUM_STONE, "gypsum_stone");
        this.registerBlockRenderer(GYPSUM_COBBLESTONE, "gypsum_cobblestone");
        this.registerBlockRenderer(GYPSUM_BRICKS, "gypsum_bricks");
        this.registerBlockRenderer(BlockHandler.DISPLAY_BLOCK, "display_block");

        this.registerBlockRenderer(MOSS, "moss");
        this.registerBlockRenderer(CLEAR_GLASS, "clear_glass");

        this.registerBlockRenderer(BlockHandler.WILD_ONION, "wild_onion_plant");
        this.registerBlockRenderer(BlockHandler.GRACILARIA, "gracilaria_seaweed");
        this.registerBlockRenderer(PEAT, "peat");
        this.registerBlockRenderer(PEAT_MOSS, "peat_moss");
        this.registerBlockRenderer(DICROIDIUM_ZUBERI, "dicroidium_zuberi");
        this.registerBlockRenderer(WEST_INDIAN_LILAC, "west_indian_lilac");
        this.registerBlockRenderer(DICTYOPHYLLUM, "dictyophyllum");
        this.registerBlockRenderer(SERENNA_VERIFORMANS, "serenna_veriformans");
        this.registerBlockRenderer(LADINIA_SIMPLEX, "ladinia_simplex");
        this.registerBlockRenderer(ORONTIUM_MACKII, "orontium_mackii");
        this.registerBlockRenderer(UMALTOLEPIS, "umaltolepis");
        this.registerBlockRenderer(LIRIODENDRITES, "liriodendrites");
        this.registerBlockRenderer(RAPHAELIA, "raphaelia");
        this.registerBlockRenderer(ENCEPHALARTOS, "encephalartos");

        for (FossilizedTrackwayBlock.TrackwayType trackwayType : FossilizedTrackwayBlock.TrackwayType.values()) {
            this.registerBlockRenderer(FOSSILIZED_TRACKWAY, trackwayType.ordinal(), "fossilized_trackway_" + trackwayType.getName());
        }

        for (NestFossilBlock.Variant variant : NestFossilBlock.Variant.values()) {
            this.registerBlockRenderer(NEST_FOSSIL, variant.ordinal(), "nest_fossil_" + (variant.ordinal() + 1));
            this.registerBlockRenderer(ENCASED_NEST_FOSSIL, variant.ordinal(), "encased_nest_fossil");
        }

        this.registerBlockRenderer(PALEO_BALE_CYCADEOIDEA, "paleo_bale_cycadeoidea");
        this.registerBlockRenderer(PALEO_BALE_CYCAD, "paleo_bale_cycad");
        this.registerBlockRenderer(PALEO_BALE_FERN, "paleo_bale_fern");
        this.registerBlockRenderer(PALEO_BALE_LEAVES, "paleo_bale_leaves");
        this.registerBlockRenderer(PALEO_BALE_OTHER, "paleo_bale_other");

        this.registerBlockRenderer(AJUGINUCULA_SMITHII);
        this.registerBlockRenderer(BUG_CRATE);

        this.registerBlockRenderer(PLANKTON_SWARM);
        this.registerBlockRenderer(KRILL_SWARM);

        this.registerBlockRenderer(LOW_SECURITY_FENCE_POLE);
        this.registerBlockRenderer(LOW_SECURITY_FENCE_BASE);
        this.registerBlockRenderer(LOW_SECURITY_FENCE_WIRE);
        this.registerBlockRenderer(MED_SECURITY_FENCE_POLE);
        this.registerBlockRenderer(MED_SECURITY_FENCE_BASE);
        this.registerBlockRenderer(MED_SECURITY_FENCE_WIRE);
        this.registerBlockRenderer(HIGH_SECURITY_FENCE_POLE);
        this.registerBlockRenderer(HIGH_SECURITY_FENCE_BASE);
        this.registerBlockRenderer(HIGH_SECURITY_FENCE_WIRE);

        this.registerBlockRenderer(WILD_POTATO_PLANT);

        this.registerBlockRenderer(RHAMNUS_SALICIFOLIUS_PLANT);

        this.registerBlockRenderer(TEMPSKYA);
        this.registerBlockRenderer(CINNAMON_FERN);
        this.registerBlockRenderer(BRISTLE_FERN);

        this.registerBlockRenderer(TOUR_RAIL, "tour_rail.tbl_jurassicraft");
        this.registerBlockRenderer(TOUR_RAIL_SLOW, "tour_rail_stripe.tbl_jurassicraft");
        this.registerBlockRenderer(TOUR_RAIL_MEDIUM, "tour_rail_stripe.tbl_jurassicraft");
        this.registerBlockRenderer(TOUR_RAIL_FAST, "tour_rail_stripe.tbl_jurassicraft");

        this.registerItemRenderer(TRACKER);
        this.registerItemRenderer(PLANT_CELLS_PETRI_DISH);
        this.registerItemRenderer(PLANT_CELLS);
        this.registerItemRenderer(GROWTH_SERUM);
        this.registerItemRenderer(IRON_ROD);
        this.registerItemRenderer(IRON_BLADES);
        this.registerItemRenderer(PETRI_DISH);
        this.registerItemRenderer(PETRI_DISH_AGAR);
        this.registerItemRenderer(PLASTER_AND_BANDAGE);

        this.registerItemRenderer(FUN_FRIES);
        this.registerItemRenderer(OILED_POTATO_STRIPS);
        this.registerItemRenderer(LUNCH_BOX);
        this.registerItemRenderer(STAMP_SET);

        this.registerItemRenderer(INGEN_JOURNAL);

        for (Entry<Integer, Dinosaur> entry : EntityHandler.getDinosaurs().entrySet()) {
            this.registerItemRenderer(SPAWN_EGG, entry.getKey(), "dino_spawn_egg");
        }

        this.registerItemRenderer(PADDOCK_SIGN);

        for (AttractionSignEntity.AttractionSignType type : AttractionSignEntity.AttractionSignType.values()) {
            this.registerItemRenderer(ATTRACTION_SIGN, type.ordinal(), "attraction_sign_" + type.name().toLowerCase(Locale.ENGLISH));
        }

        this.registerItemRenderer(EMPTY_TEST_TUBE);
        this.registerItemRenderer(EMPTY_SYRINGE);
        this.registerItemRenderer(STORAGE_DISC);
        this.registerItemRenderer(DISC_DRIVE, "disc_reader");
        this.registerItemRenderer(LASER);
        this.registerItemRenderer(DNA_NUCLEOTIDES, "dna_base_material");
        this.registerItemRenderer(SEA_LAMPREY);

        this.registerItemRenderer(AMBER, 0, "amber_mosquito");
        this.registerItemRenderer(AMBER, 1, "amber_aphid");

        this.registerItemRenderer(HELICOPTER, "helicopter_spawner");

        this.registerItemRenderer(JURASSICRAFT_THEME_DISC, "disc_jurassicraft_theme");
        this.registerItemRenderer(DONT_MOVE_A_MUSCLE_DISC, "disc_dont_move_a_muscle");
        this.registerItemRenderer(TROODONS_AND_RAPTORS_DISC, "disc_troodons_and_raptors");

        this.registerItemRenderer(AMBER_KEYCHAIN, "amber_keychain");
        this.registerItemRenderer(AMBER_CANE, "amber_cane");
        this.registerItemRenderer(MR_DNA_KEYCHAIN, "mr_dna_keychain");

        this.registerItemRenderer(DINO_SCANNER, "dino_scanner");

        this.registerItemRenderer(BASIC_CIRCUIT, "basic_circuit");
        this.registerItemRenderer(ADVANCED_CIRCUIT, "advanced_circuit");
        this.registerItemRenderer(IRON_NUGGET, "iron_nugget");

        this.registerItemRenderer(GYPSUM_POWDER, "gypsum_powder");

        this.registerItemRenderer(AJUGINUCULA_SMITHII_SEEDS, "ajuginucula_smithii_seeds");
        this.registerItemRenderer(AJUGINUCULA_SMITHII_LEAVES, "ajuginucula_smithii_leaves");
        this.registerItemRenderer(AJUGINUCULA_SMITHII_OIL, "ajuginucula_smithii_oil");

        this.registerItemRenderer(ItemHandler.WILD_ONION, "wild_onion");
        this.registerItemRenderer(ItemHandler.GRACILARIA);
        this.registerItemRenderer(LIQUID_AGAR, "liquid_agar");

        this.registerItemRenderer(ItemHandler.PLANT_FOSSIL, "plant_fossil");
        this.registerItemRenderer(TWIG_FOSSIL, "twig_fossil");

        this.registerItemRenderer(KEYBOARD, "keyboard");
        this.registerItemRenderer(COMPUTER_SCREEN, "computer_screen");
        this.registerItemRenderer(DNA_ANALYZER, "dna_analyzer");

        this.registerItemRenderer(CHILEAN_SEA_BASS, "chilean_sea_bass");
        this.registerItemRenderer(FIELD_GUIDE, "field_guide");

        this.registerItemRenderer(CAR_CHASSIS, "car_chassis");
        this.registerItemRenderer(CAR_ENGINE_SYSTEM, "car_engine_system");
        this.registerItemRenderer(CAR_SEATS, "car_seats");
        this.registerItemRenderer(CAR_TIRE, "car_tire");
        this.registerItemRenderer(CAR_WINDSCREEN, "car_windscreen");
        this.registerItemRenderer(UNFINISHED_CAR, "unfinished_car");
        this.registerItemRenderer(JEEP_WRANGLER, "jeep_wrangler");
        this.registerItemRenderer(FORD_EXPLORER, "ford_explorer");

        this.registerItemRenderer(MURAL, "mural");

        for (Dinosaur dinosaur : EntityHandler.getDinosaurs().values()) {
            int meta = EntityHandler.getDinosaurId(dinosaur);

            String formattedName = dinosaur.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

            for (Map.Entry<String, FossilItem> entry : ItemHandler.FOSSILS.entrySet()) {
                List<Dinosaur> dinosaursForType = FossilItem.fossilDinosaurs.get(entry.getKey());
                if (dinosaursForType.contains(dinosaur)) {
                    this.registerItemRenderer(entry.getValue(), meta, "bones/" + formattedName + "_" + entry.getKey());
                }
            }

            for (Map.Entry<String, FossilItem> entry : FRESH_FOSSILS.entrySet()) {
                List<Dinosaur> dinosaursForType = FossilItem.fossilDinosaurs.get(entry.getKey());
                if (dinosaursForType.contains(dinosaur)) {
                    this.registerItemRenderer(entry.getValue(), meta, "fresh_bones/" + formattedName + "_" + entry.getKey());
                }
            }

            this.registerItemRenderer(DNA, meta, "dna/dna_" + formattedName);
            this.registerItemRenderer(DINOSAUR_MEAT, meta, "meat/meat_" + formattedName);
            this.registerItemRenderer(DINOSAUR_STEAK, meta, "meat/steak_" + formattedName);
            this.registerItemRenderer(SOFT_TISSUE, meta, "soft_tissue/soft_tissue_" + formattedName);
            this.registerItemRenderer(SYRINGE, meta, "syringe/syringe_" + formattedName);
            //this.registerItemRenderer(ACTION_FIGURE, meta, "action_figure/action_figure_" + formattedName);

            if (!dinosaur.givesDirectBirth()) {
                this.registerItemRenderer(EGG, meta, "egg/egg_" + formattedName);
            }

            this.registerItemRenderer(HATCHED_EGG, meta, "hatched_egg/egg_" + formattedName);
        }

        for (Plant plant : PlantHandler.getPrehistoricPlantsAndTrees()) {
            int meta = PlantHandler.getPlantId(plant);

            String name = plant.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

            this.registerItemRenderer(PLANT_DNA, meta, "dna/plants/dna_" + name);
            this.registerItemRenderer(PLANT_SOFT_TISSUE, meta, "soft_tissue/plants/soft_tissue_" + name);
            this.registerItemRenderer(PLANT_CALLUS, meta, "plant_callus");
        }

        for (JournalItem.JournalType type : JournalItem.JournalType.values()) {
            this.registerItemRenderer(INGEN_JOURNAL, type.getMetadata(), "ingen_journal");
        }

        for (NestFossilBlock.Variant variant : NestFossilBlock.Variant.values()) {
            this.registerItemRenderer(FOSSILIZED_EGG, variant.ordinal(), "fossilized_egg_" + (variant.ordinal() + 1));
        }

        for (TreeType type : TreeType.values()) {
            String name = type.name().toLowerCase(Locale.ENGLISH);
            this.registerItemRenderer(ItemHandler.ANCIENT_DOORS.get(type), name + "_door_item");
        }

        this.registerItemRenderer(PHOENIX_SEEDS);
        this.registerItemRenderer(PHOENIX_FRUIT);

        this.registerItemRenderer(CRICKETS);
        this.registerItemRenderer(COCKROACHES);
        this.registerItemRenderer(MEALWORM_BEETLES);

        this.registerItemRenderer(FINE_NET);
        this.registerItemRenderer(PLANKTON);
        this.registerItemRenderer(KRILL);

        this.registerItemRenderer(WILD_POTATO_SEEDS);
        this.registerItemRenderer(WILD_POTATO);
        this.registerItemRenderer(WILD_POTATO_COOKED);

        this.registerItemRenderer(RHAMNUS_SEEDS);
        this.registerItemRenderer(RHAMNUS_BERRIES);

        this.registerItemRenderer(GOAT_RAW);
        this.registerItemRenderer(GOAT_COOKED);

        this.registerItemRenderer(DART_GUN);
        this.registerItemRenderer(DART_TRANQUILIZER, "dart_colored");
        this.registerItemRenderer(DART_POISON_CYCASIN, "dart_colored");
        this.registerItemRenderer(DART_POISON_EXECUTIONER_CONCOCTION, "dart_colored");
        this.registerItemRenderer(DART_TIPPED_POTION, "dart_colored");

        this.registerItemRenderer(WEST_INDIAN_LILAC_BERRIES);

        this.registerRenderInfo(EntityHandler.BRACHIOSAURUS, new BrachiosaurusAnimator(), 1.5F);
        this.registerRenderInfo(EntityHandler.COELACANTH, new CoelacanthAnimator(), 0.0F);
//        this.registerRenderInfo(EntityHandler.ALLIGATORGAR, new AlligatorGarAnimator(), 0.0F);
        this.registerRenderInfo(EntityHandler.DILOPHOSAURUS, new DilophosaurusAnimator(), 0.65F);
        this.registerRenderInfo(EntityHandler.GALLIMIMUS, new GallimimusAnimator(), 0.65F);
        this.registerRenderInfo(EntityHandler.PARASAUROLOPHUS, new ParasaurolophusAnimator(), 0.65F);
        this.registerRenderInfo(EntityHandler.MICRORAPTOR, new MicroraptorAnimator(), 0.45F);
        this.registerRenderInfo(EntityHandler.MUSSAURUS, new MussaurusAnimator(), 0.8F);
        this.registerRenderInfo(EntityHandler.TRICERATOPS, new TriceratopsAnimator(), 0.65F);
        this.registerRenderInfo(EntityHandler.TYRANNOSAURUS, new TyrannosaurusAnimator(), 0.65F);
        this.registerRenderInfo(EntityHandler.VELOCIRAPTOR, new VelociraptorAnimator(), 0.45F);
        //this.registerRenderInfo(EntityHandler.STEGOSAURUS, new StegosaurusAnimator(), 0.65F);

        RenderingRegistry.registerEntityRenderingHandler(PaddockSignEntity.class, new PaddockSignRenderer());
        RenderingRegistry.registerEntityRenderingHandler(AttractionSignEntity.class, new AttractionSignRenderer());
        RenderingRegistry.registerEntityRenderingHandler(HelicopterBaseEntity.class, new HelicopterRenderer());
        RenderingRegistry.registerEntityRenderingHandler(DinosaurEggEntity.class, new DinosaurEggRenderer());
        RenderingRegistry.registerEntityRenderingHandler(VenomEntity.class, new VenomRenderer());
        RenderingRegistry.registerEntityRenderingHandler(JeepWranglerEntity.class, JeepWranglerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(FordExplorerEntity.class, FordExplorerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(MuralEntity.class, MuralRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(GoatEntity.class, GoatRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TranquilizerDartEntity.class, NullRenderer::new);
        
    }

    public void init() {
        BlockColors blockColors = this.mc.getBlockColors();
        blockColors.registerBlockColorHandler((state, access, pos, tintIndex) -> pos != null ? BiomeColorHelper.getGrassColorAtPos(access, pos) : 0xFFFFFF, MOSS);

        for (Map.Entry<TreeType, AncientLeavesBlock> entry : ANCIENT_LEAVES.entrySet()) {
            blockColors.registerBlockColorHandler((state, access, pos, tintIndex) -> pos == null ? ColorizerFoliage.getFoliageColorBasic() : BiomeColorHelper.getFoliageColorAtPos(access, pos), entry.getValue());
        }

        blockColors.registerBlockColorHandler((state, access, pos, tintIndex) -> pos == null ? ColorizerFoliage.getFoliageColorBasic() : BiomeColorHelper.getFoliageColorAtPos(access, pos), MOSS);
        blockColors.registerBlockColorHandler((state, access, pos, tintIndex) -> tintIndex == 1 ? ((TourRailBlock)state.getBlock()).getSpeedType().getColor() : -1, TOUR_RAIL_SLOW, TOUR_RAIL_MEDIUM, TOUR_RAIL_FAST);

        ItemColors itemColors = this.mc.getItemColors();

        itemColors.registerItemColorHandler((stack, tintIndex) -> tintIndex == 1 ? ((TourRailBlock)((ItemBlock)stack.getItem()).getBlock()).getSpeedType().getColor() : -1, TOUR_RAIL_SLOW, TOUR_RAIL_MEDIUM, TOUR_RAIL_FAST);

        for (Map.Entry<TreeType, AncientLeavesBlock> entry : ANCIENT_LEAVES.entrySet()) {
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
        }, SPAWN_EGG);

        itemColors.registerItemColorHandler(((stack, tintIndex) -> tintIndex == 1 ? ((Dart)stack.getItem()).getDartColor(stack) : -1), DART_POISON_CYCASIN, DART_POISON_EXECUTIONER_CONCOCTION, DART_TIPPED_POTION, DART_TRANQUILIZER);
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

    public void registerItemRenderer(Item item) {
        this.registerItemRenderer(item, item.getUnlocalizedName().substring("item.".length()));
    }

    public void registerBlockRenderer(Block block) {
        this.registerBlockRenderer(block, block.getUnlocalizedName().substring("tile.".length()));
    }

    public void registerItemRenderer(Item item, int meta, String path) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(JurassiCraft.MODID + ":" + path, "inventory"));
    }

    public void registerItemRenderer(Item item, String path) {
        this.registerItemRenderer(item, 0, path);
    }

    public void registerBlockRenderer(Block block, int meta, String path) {
        this.registerItemRenderer(Item.getItemFromBlock(block), meta, path);
    }

    public void registerBlockRenderer(Block block, String path) {
        this.registerBlockRenderer(block, 0, path);
    }

    private void registerRenderInfo(Dinosaur dinosaur, EntityAnimator<?> animator, float shadowSize) {
        this.registerRenderInfo(new DinosaurRenderInfo(dinosaur, animator, shadowSize));
    }

    private void registerRenderInfo(DinosaurRenderInfo renderDef) {
        this.renderInfos.put(renderDef.getDinosaur(), renderDef);
        RenderingRegistry.registerEntityRenderingHandler(renderDef.getDinosaur().getDinosaurClass(), renderDef);
    }

    public DinosaurRenderInfo getRenderInfo(Dinosaur dino) {
        return this.renderInfos.get(dino);
    }
}