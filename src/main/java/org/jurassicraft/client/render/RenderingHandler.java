package org.jurassicraft.client.render;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

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
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum RenderingHandler {
    INSTANCE;

    private final Minecraft mc = Minecraft.getMinecraft();
    private Map<Dinosaur, DinosaurRenderInfo> renderInfos = Maps.newHashMap();

    public void preInit() {
        TabulaModelHandler.INSTANCE.addDomain(JurassiCraft.MODID);

        for (EnumDyeColor color : EnumDyeColor.values()) {
            this.registerItemRenderer(Item.getItemFromBlock(BlockHandler.CULTIVATOR_BOTTOM), color.getMetadata(), "cultivate/cultivate_bottom_" + color.getName().toLowerCase(Locale.ENGLISH));
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
        ModelLoader.setCustomStateMapper(BlockHandler.MED_SECURITY_FENCE_BASE, new MultipartStateMap());
        ModelLoader.setCustomStateMapper(BlockHandler.MED_SECURITY_FENCE_POLE, new MultipartStateMap());
        ModelLoader.setCustomStateMapper(BlockHandler.MED_SECURITY_FENCE_WIRE, new MultipartStateMap());
        ModelLoader.setCustomStateMapper(BlockHandler.HIGH_SECURITY_FENCE_BASE, new MultipartStateMap());
        ModelLoader.setCustomStateMapper(BlockHandler.HIGH_SECURITY_FENCE_POLE, new MultipartStateMap());
        ModelLoader.setCustomStateMapper(BlockHandler.HIGH_SECURITY_FENCE_WIRE, new MultipartStateMap());

        ItemHandler.DISPLAY_BLOCK.initModels(EntityHandler.getDinosaurs().values(), this);

        int i = 0;

        for (EncasedFossilBlock fossil : BlockHandler.ENCASED_FOSSILS) {
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
            this.registerBlockRenderer(BlockHandler.ANCIENT_LEAVES.get(type), name + "_leaves");
            this.registerBlockRenderer(BlockHandler.ANCIENT_SAPLINGS.get(type), name + "_sapling");
            this.registerBlockRenderer(BlockHandler.ANCIENT_PLANKS.get(type), name + "_planks");
            this.registerBlockRenderer(BlockHandler.ANCIENT_LOGS.get(type), name + "_log");
            this.registerBlockRenderer(BlockHandler.ANCIENT_STAIRS.get(type), name + "_stairs");
            this.registerBlockRenderer(BlockHandler.ANCIENT_SLABS.get(type), name + "_slab");
            this.registerBlockRenderer(BlockHandler.ANCIENT_DOUBLE_SLABS.get(type), name + "_double_slab");
            this.registerBlockRenderer(BlockHandler.ANCIENT_FENCES.get(type), name + "_fence");
            this.registerBlockRenderer(BlockHandler.ANCIENT_FENCE_GATES.get(type), name + "_fence_gate");
            this.registerBlockRenderer(BlockHandler.PETRIFIED_LOGS.get(type), name + "_log_petrified");
        }

        /*for (EnumDyeColor color : EnumDyeColor.values()) {
            this.registerBlockRenderer(BlockHandler.CULTIVATOR_BOTTOM, color.ordinal(), "cultivate/cultivate_bottom_" + color.getName().toLowerCase(Locale.ENGLISH));
        }*/

        this.registerBlockRenderer(BlockHandler.SCALY_TREE_FERN, "scaly_tree_fern");
        this.registerBlockRenderer(BlockHandler.SMALL_ROYAL_FERN, "small_royal_fern");
        this.registerBlockRenderer(BlockHandler.SMALL_CHAIN_FERN, "small_chain_fern");
        this.registerBlockRenderer(BlockHandler.SMALL_CYCAD, "small_cycad");
        this.registerBlockRenderer(BlockHandler.CYCADEOIDEA, "bennettitalean_cycadeoidea");
        this.registerBlockRenderer(BlockHandler.CRY_PANSY, "cry_pansy");
        this.registerBlockRenderer(BlockHandler.ZAMITES, "cycad_zamites");
        this.registerBlockRenderer(BlockHandler.DICKSONIA, "dicksonia");
        this.registerBlockRenderer(BlockHandler.WOOLLY_STALKED_BEGONIA, "woolly_stalked_begonia");
        this.registerBlockRenderer(BlockHandler.LARGESTIPULE_LEATHER_ROOT, "largestipule_leather_root");
        this.registerBlockRenderer(BlockHandler.RHACOPHYTON, "rhacophyton");
        this.registerBlockRenderer(BlockHandler.GRAMINIDITES_BAMBUSOIDES, "graminidites_bambusoides");
        this.registerBlockRenderer(BlockHandler.ENALLHELIA, "enallhelia");
        this.registerBlockRenderer(BlockHandler.AULOPORA, "aulopora");
        this.registerBlockRenderer(BlockHandler.CLADOCHONUS, "cladochonus");
        this.registerBlockRenderer(BlockHandler.LITHOSTROTION, "lithostrotion");
        this.registerBlockRenderer(BlockHandler.STYLOPHYLLOPSIS, "stylophyllopsis");
        this.registerBlockRenderer(BlockHandler.HIPPURITES_RADIOSUS, "hippurites_radiosus");
        this.registerBlockRenderer(BlockHandler.HELICONIA, "heliconia");

        this.registerBlockRenderer(BlockHandler.REINFORCED_STONE, "reinforced_stone");
        this.registerBlockRenderer(BlockHandler.REINFORCED_BRICKS, "reinforced_bricks");

        this.registerBlockRenderer(BlockHandler.CULTIVATOR_BOTTOM, "cultivate_bottom");
        this.registerBlockRenderer(BlockHandler.CULTIVATOR_TOP, "cultivate_top");

        this.registerBlockRenderer(BlockHandler.AMBER_ORE, "amber_ore");
        this.registerBlockRenderer(BlockHandler.ICE_SHARD, "ice_shard");
        this.registerBlockRenderer(BlockHandler.CLEANING_STATION, "cleaning_station");
        this.registerBlockRenderer(BlockHandler.FOSSIL_GRINDER, "fossil_grinder");
        this.registerBlockRenderer(BlockHandler.DNA_SEQUENCER, "dna_sequencer");
        this.registerBlockRenderer(BlockHandler.DNA_COMBINATOR_HYBRIDIZER, "dna_combinator_hybridizer");
        this.registerBlockRenderer(BlockHandler.DNA_SYNTHESIZER, "dna_synthesizer");
        this.registerBlockRenderer(BlockHandler.EMBRYONIC_MACHINE, "embryonic_machine");
        this.registerBlockRenderer(BlockHandler.EMBRYO_CALCIFICATION_MACHINE, "embryo_calcification_machine");
        this.registerBlockRenderer(BlockHandler.INCUBATOR, "incubator");
        this.registerBlockRenderer(BlockHandler.DNA_EXTRACTOR, "dna_extractor");
        this.registerBlockRenderer(BlockHandler.FEEDER, "feeder");
        this.registerBlockRenderer(BlockHandler.SKELETON_ASSEMBLY, "skeleton_assembly");
        this.registerBlockRenderer(BlockHandler.GYPSUM_STONE, "gypsum_stone");
        this.registerBlockRenderer(BlockHandler.GYPSUM_COBBLESTONE, "gypsum_cobblestone");
        this.registerBlockRenderer(BlockHandler.GYPSUM_BRICKS, "gypsum_bricks");
        this.registerBlockRenderer(BlockHandler.DISPLAY_BLOCK, "display_block");

        this.registerBlockRenderer(BlockHandler.MOSS, "moss");
        this.registerBlockRenderer(BlockHandler.CLEAR_GLASS, "clear_glass");

        this.registerBlockRenderer(BlockHandler.WILD_ONION, "wild_onion_plant");
        this.registerBlockRenderer(BlockHandler.GRACILARIA, "gracilaria_seaweed");
        this.registerBlockRenderer(BlockHandler.PEAT, "peat");
        this.registerBlockRenderer(BlockHandler.PEAT_MOSS, "peat_moss");
        this.registerBlockRenderer(BlockHandler.DICROIDIUM_ZUBERI, "dicroidium_zuberi");
        this.registerBlockRenderer(BlockHandler.WEST_INDIAN_LILAC, "west_indian_lilac");
        this.registerBlockRenderer(BlockHandler.DICTYOPHYLLUM, "dictyophyllum");
        this.registerBlockRenderer(BlockHandler.SERENNA_VERIFORMANS, "serenna_veriformans");
        this.registerBlockRenderer(BlockHandler.LADINIA_SIMPLEX, "ladinia_simplex");
        this.registerBlockRenderer(BlockHandler.ORONTIUM_MACKII, "orontium_mackii");
        this.registerBlockRenderer(BlockHandler.UMALTOLEPIS, "umaltolepis");
        this.registerBlockRenderer(BlockHandler.LIRIODENDRITES, "liriodendrites");
        this.registerBlockRenderer(BlockHandler.RAPHAELIA, "raphaelia");
        this.registerBlockRenderer(BlockHandler.ENCEPHALARTOS, "encephalartos");

        for (FossilizedTrackwayBlock.TrackwayType trackwayType : FossilizedTrackwayBlock.TrackwayType.values()) {
            this.registerBlockRenderer(BlockHandler.FOSSILIZED_TRACKWAY, trackwayType.ordinal(), "fossilized_trackway_" + trackwayType.getName());
        }

        for (NestFossilBlock.Variant variant : NestFossilBlock.Variant.values()) {
            this.registerBlockRenderer(BlockHandler.NEST_FOSSIL, variant.ordinal(), "nest_fossil_" + (variant.ordinal() + 1));
            this.registerBlockRenderer(BlockHandler.ENCASED_NEST_FOSSIL, variant.ordinal(), "encased_nest_fossil");
        }

        this.registerBlockRenderer(BlockHandler.PALEO_BALE_CYCADEOIDEA, "paleo_bale_cycadeoidea");
        this.registerBlockRenderer(BlockHandler.PALEO_BALE_CYCAD, "paleo_bale_cycad");
        this.registerBlockRenderer(BlockHandler.PALEO_BALE_FERN, "paleo_bale_fern");
        this.registerBlockRenderer(BlockHandler.PALEO_BALE_LEAVES, "paleo_bale_leaves");
        this.registerBlockRenderer(BlockHandler.PALEO_BALE_OTHER, "paleo_bale_other");

        this.registerBlockRenderer(BlockHandler.AJUGINUCULA_SMITHII);
        this.registerBlockRenderer(BlockHandler.BUG_CRATE);

        this.registerBlockRenderer(BlockHandler.PLANKTON_SWARM);
        this.registerBlockRenderer(BlockHandler.KRILL_SWARM);

        this.registerBlockRenderer(BlockHandler.LOW_SECURITY_FENCE_POLE);
        this.registerBlockRenderer(BlockHandler.LOW_SECURITY_FENCE_BASE);
        this.registerBlockRenderer(BlockHandler.LOW_SECURITY_FENCE_WIRE);
        this.registerBlockRenderer(BlockHandler.MED_SECURITY_FENCE_POLE);
        this.registerBlockRenderer(BlockHandler.MED_SECURITY_FENCE_BASE);
        this.registerBlockRenderer(BlockHandler.MED_SECURITY_FENCE_WIRE);
        this.registerBlockRenderer(BlockHandler.HIGH_SECURITY_FENCE_POLE);
        this.registerBlockRenderer(BlockHandler.HIGH_SECURITY_FENCE_BASE);
        this.registerBlockRenderer(BlockHandler.HIGH_SECURITY_FENCE_WIRE);

        this.registerBlockRenderer(BlockHandler.WILD_POTATO_PLANT);

        this.registerBlockRenderer(BlockHandler.RHAMNUS_SALICIFOLIUS_PLANT);

        this.registerBlockRenderer(BlockHandler.TEMPSKYA);
        this.registerBlockRenderer(BlockHandler.CINNAMON_FERN);
        this.registerBlockRenderer(BlockHandler.BRISTLE_FERN);

        this.registerBlockRenderer(BlockHandler.TOUR_RAIL);
        this.registerBlockRenderer(BlockHandler.TOUR_RAIL_POWERED);

        this.registerItemRenderer(ItemHandler.TRACKER);
        this.registerItemRenderer(ItemHandler.PLANT_CELLS_PETRI_DISH);
        this.registerItemRenderer(ItemHandler.PLANT_CELLS);
        this.registerItemRenderer(ItemHandler.GROWTH_SERUM);
        this.registerItemRenderer(ItemHandler.IRON_ROD);
        this.registerItemRenderer(ItemHandler.IRON_BLADES);
        this.registerItemRenderer(ItemHandler.PETRI_DISH);
        this.registerItemRenderer(ItemHandler.PETRI_DISH_AGAR);
        this.registerItemRenderer(ItemHandler.PLASTER_AND_BANDAGE);

        this.registerItemRenderer(ItemHandler.FUN_FRIES);
        this.registerItemRenderer(ItemHandler.OILED_POTATO_STRIPS);
        this.registerItemRenderer(ItemHandler.LUNCH_BOX);
        this.registerItemRenderer(ItemHandler.STAMP_SET);

        this.registerItemRenderer(ItemHandler.INGEN_JOURNAL);

        for (Entry<Integer, Dinosaur> entry : EntityHandler.getDinosaurs().entrySet()) {
            this.registerItemRenderer(ItemHandler.SPAWN_EGG, entry.getKey(), "dino_spawn_egg");
        }

        this.registerItemRenderer(ItemHandler.PADDOCK_SIGN);

        for (AttractionSignEntity.AttractionSignType type : AttractionSignEntity.AttractionSignType.values()) {
            this.registerItemRenderer(ItemHandler.ATTRACTION_SIGN, type.ordinal(), "attraction_sign_" + type.name().toLowerCase(Locale.ENGLISH));
        }

        this.registerItemRenderer(ItemHandler.EMPTY_TEST_TUBE);
        this.registerItemRenderer(ItemHandler.EMPTY_SYRINGE);
        this.registerItemRenderer(ItemHandler.STORAGE_DISC);
        this.registerItemRenderer(ItemHandler.DISC_DRIVE, "disc_reader");
        this.registerItemRenderer(ItemHandler.LASER);
        this.registerItemRenderer(ItemHandler.DNA_NUCLEOTIDES, "dna_base_material");
        this.registerItemRenderer(ItemHandler.SEA_LAMPREY);

        this.registerItemRenderer(ItemHandler.AMBER, 0, "amber_mosquito");
        this.registerItemRenderer(ItemHandler.AMBER, 1, "amber_aphid");

        this.registerItemRenderer(ItemHandler.HELICOPTER, "helicopter_spawner");

        this.registerItemRenderer(ItemHandler.JURASSICRAFT_THEME_DISC, "disc_jurassicraft_theme");
        this.registerItemRenderer(ItemHandler.DONT_MOVE_A_MUSCLE_DISC, "disc_dont_move_a_muscle");
        this.registerItemRenderer(ItemHandler.TROODONS_AND_RAPTORS_DISC, "disc_troodons_and_raptors");

        this.registerItemRenderer(ItemHandler.AMBER_KEYCHAIN, "amber_keychain");
        this.registerItemRenderer(ItemHandler.AMBER_CANE, "amber_cane");
        this.registerItemRenderer(ItemHandler.MR_DNA_KEYCHAIN, "mr_dna_keychain");

        this.registerItemRenderer(ItemHandler.DINO_SCANNER, "dino_scanner");

        this.registerItemRenderer(ItemHandler.BASIC_CIRCUIT, "basic_circuit");
        this.registerItemRenderer(ItemHandler.ADVANCED_CIRCUIT, "advanced_circuit");
        this.registerItemRenderer(ItemHandler.IRON_NUGGET, "iron_nugget");

        this.registerItemRenderer(ItemHandler.GYPSUM_POWDER, "gypsum_powder");

        this.registerItemRenderer(ItemHandler.AJUGINUCULA_SMITHII_SEEDS, "ajuginucula_smithii_seeds");
        this.registerItemRenderer(ItemHandler.AJUGINUCULA_SMITHII_LEAVES, "ajuginucula_smithii_leaves");
        this.registerItemRenderer(ItemHandler.AJUGINUCULA_SMITHII_OIL, "ajuginucula_smithii_oil");

        this.registerItemRenderer(ItemHandler.WILD_ONION, "wild_onion");
        this.registerItemRenderer(ItemHandler.GRACILARIA);
        this.registerItemRenderer(ItemHandler.LIQUID_AGAR, "liquid_agar");

        this.registerItemRenderer(ItemHandler.PLANT_FOSSIL, "plant_fossil");
        this.registerItemRenderer(ItemHandler.TWIG_FOSSIL, "twig_fossil");

        this.registerItemRenderer(ItemHandler.KEYBOARD, "keyboard");
        this.registerItemRenderer(ItemHandler.COMPUTER_SCREEN, "computer_screen");
        this.registerItemRenderer(ItemHandler.DNA_ANALYZER, "dna_analyzer");

        this.registerItemRenderer(ItemHandler.CHILEAN_SEA_BASS, "chilean_sea_bass");
        this.registerItemRenderer(ItemHandler.FIELD_GUIDE, "field_guide");

        this.registerItemRenderer(ItemHandler.CAR_CHASSIS, "car_chassis");
        this.registerItemRenderer(ItemHandler.CAR_ENGINE_SYSTEM, "car_engine_system");
        this.registerItemRenderer(ItemHandler.CAR_SEATS, "car_seats");
        this.registerItemRenderer(ItemHandler.CAR_TIRE, "car_tire");
        this.registerItemRenderer(ItemHandler.CAR_WINDSCREEN, "car_windscreen");
        this.registerItemRenderer(ItemHandler.UNFINISHED_CAR, "unfinished_car");
        this.registerItemRenderer(ItemHandler.JEEP_WRANGLER, "jeep_wrangler");
        this.registerItemRenderer(ItemHandler.FORD_EXPLORER, "ford_explorer");

        this.registerItemRenderer(ItemHandler.MURAL, "mural");

        for (Dinosaur dinosaur : EntityHandler.getDinosaurs().values()) {
            int meta = EntityHandler.getDinosaurId(dinosaur);

            String formattedName = dinosaur.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

            for (Map.Entry<String, FossilItem> entry : ItemHandler.FOSSILS.entrySet()) {
                List<Dinosaur> dinosaursForType = FossilItem.fossilDinosaurs.get(entry.getKey());
                if (dinosaursForType.contains(dinosaur)) {
                    this.registerItemRenderer(entry.getValue(), meta, "bones/" + formattedName + "_" + entry.getKey());
                }
            }

            for (Map.Entry<String, FossilItem> entry : ItemHandler.FRESH_FOSSILS.entrySet()) {
                List<Dinosaur> dinosaursForType = FossilItem.fossilDinosaurs.get(entry.getKey());
                if (dinosaursForType.contains(dinosaur)) {
                    this.registerItemRenderer(entry.getValue(), meta, "fresh_bones/" + formattedName + "_" + entry.getKey());
                }
            }

            this.registerItemRenderer(ItemHandler.DNA, meta, "dna/dna_" + formattedName);
            this.registerItemRenderer(ItemHandler.DINOSAUR_MEAT, meta, "meat/meat_" + formattedName);
            this.registerItemRenderer(ItemHandler.DINOSAUR_STEAK, meta, "meat/steak_" + formattedName);
            this.registerItemRenderer(ItemHandler.SOFT_TISSUE, meta, "soft_tissue/soft_tissue_" + formattedName);
            this.registerItemRenderer(ItemHandler.SYRINGE, meta, "syringe/syringe_" + formattedName);
            //this.registerItemRenderer(ItemHandler.ACTION_FIGURE, meta, "action_figure/action_figure_" + formattedName);

            if (!dinosaur.givesDirectBirth()) {
                this.registerItemRenderer(ItemHandler.EGG, meta, "egg/egg_" + formattedName);
            }

            this.registerItemRenderer(ItemHandler.HATCHED_EGG, meta, "hatched_egg/egg_" + formattedName);
        }

        for (Plant plant : PlantHandler.getPrehistoricPlantsAndTrees()) {
            int meta = PlantHandler.getPlantId(plant);

            String name = plant.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

            this.registerItemRenderer(ItemHandler.PLANT_DNA, meta, "dna/plants/dna_" + name);
            this.registerItemRenderer(ItemHandler.PLANT_SOFT_TISSUE, meta, "soft_tissue/plants/soft_tissue_" + name);
            this.registerItemRenderer(ItemHandler.PLANT_CALLUS, meta, "plant_callus");
        }

        for (JournalItem.JournalType type : JournalItem.JournalType.values()) {
            this.registerItemRenderer(ItemHandler.INGEN_JOURNAL, type.getMetadata(), "ingen_journal");
        }

        for (NestFossilBlock.Variant variant : NestFossilBlock.Variant.values()) {
            this.registerItemRenderer(ItemHandler.FOSSILIZED_EGG, variant.ordinal(), "fossilized_egg_" + (variant.ordinal() + 1));
        }

        for (TreeType type : TreeType.values()) {
            String name = type.name().toLowerCase(Locale.ENGLISH);
            this.registerItemRenderer(ItemHandler.ANCIENT_DOORS.get(type), name + "_door_item");
        }

        this.registerItemRenderer(ItemHandler.PHOENIX_SEEDS);
        this.registerItemRenderer(ItemHandler.PHOENIX_FRUIT);

        this.registerItemRenderer(ItemHandler.CRICKETS);
        this.registerItemRenderer(ItemHandler.COCKROACHES);
        this.registerItemRenderer(ItemHandler.MEALWORM_BEETLES);

        this.registerItemRenderer(ItemHandler.FINE_NET);
        this.registerItemRenderer(ItemHandler.PLANKTON);
        this.registerItemRenderer(ItemHandler.KRILL);

        this.registerItemRenderer(ItemHandler.WILD_POTATO_SEEDS);
        this.registerItemRenderer(ItemHandler.WILD_POTATO);
        this.registerItemRenderer(ItemHandler.WILD_POTATO_COOKED);

        this.registerItemRenderer(ItemHandler.RHAMNUS_SEEDS);
        this.registerItemRenderer(ItemHandler.RHAMNUS_BERRIES);

        this.registerItemRenderer(ItemHandler.GOAT_RAW);
        this.registerItemRenderer(ItemHandler.GOAT_COOKED);
        
        this.registerItemRenderer(ItemHandler.DART_GUN);
        this.registerItemRenderer(ItemHandler.DART_TRANQUILIZER);

        this.registerRenderInfo(EntityHandler.BRACHIOSAURUS, new BrachiosaurusAnimator(), 1.5F);
        this.registerRenderInfo(EntityHandler.COELACANTH, new CoelacanthAnimator(), 0.0F);
        this.registerRenderInfo(EntityHandler.ALLIGATORGAR, new AlligatorGarAnimator(), 0.0F);
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
        blockColors.registerBlockColorHandler((state, access, pos, tintIndex) -> pos != null ? BiomeColorHelper.getGrassColorAtPos(access, pos) : 0xFFFFFF, BlockHandler.MOSS);

        for (Map.Entry<TreeType, AncientLeavesBlock> entry : BlockHandler.ANCIENT_LEAVES.entrySet()) {
            blockColors.registerBlockColorHandler((state, access, pos, tintIndex) -> pos == null ? ColorizerFoliage.getFoliageColorBasic() : BiomeColorHelper.getFoliageColorAtPos(access, pos), entry.getValue());
        }

        blockColors.registerBlockColorHandler((state, access, pos, tintIndex) -> pos == null ? ColorizerFoliage.getFoliageColorBasic() : BiomeColorHelper.getFoliageColorAtPos(access, pos), BlockHandler.MOSS);

        ItemColors itemColors = this.mc.getItemColors();

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