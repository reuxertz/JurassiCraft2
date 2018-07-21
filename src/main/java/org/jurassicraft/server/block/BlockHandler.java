package org.jurassicraft.server.block;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.api.NoItemBlock;
import org.jurassicraft.server.api.SubBlocksBlock;
import org.jurassicraft.server.block.entity.*;
import org.jurassicraft.server.block.fence.ElectricFenceBaseBlock;
import org.jurassicraft.server.block.fence.ElectricFencePoleBlock;
import org.jurassicraft.server.block.fence.ElectricFenceWireBlock;
import org.jurassicraft.server.block.fence.FenceType;
import org.jurassicraft.server.block.machine.*;
import org.jurassicraft.server.block.plant.*;
import org.jurassicraft.server.block.tree.*;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.util.RegistryHandler;

import java.util.*;

public class BlockHandler
{
    public static final Map<TreeType, AncientPlanksBlock> ANCIENT_PLANKS = new HashMap<>();
    public static final Map<TreeType, AncientLogBlock> ANCIENT_LOGS = new HashMap<>();
    public static final Map<TreeType, AncientLeavesBlock> ANCIENT_LEAVES = new HashMap<>();
    public static final Map<TreeType, AncientSaplingBlock> ANCIENT_SAPLINGS = new HashMap<>();

    public static final Map<TreeType, AncientSlabHalfBlock> ANCIENT_SLABS = new HashMap<>();
    public static final Map<TreeType, AncientDoubleSlabBlock> ANCIENT_DOUBLE_SLABS = new HashMap<>();
    public static final Map<TreeType, AncientStairsBlock> ANCIENT_STAIRS = new HashMap<>();
    public static final Map<TreeType, AncientFenceBlock> ANCIENT_FENCES = new HashMap<>();
    public static final Map<TreeType, AncientFenceGateBlock> ANCIENT_FENCE_GATES = new HashMap<>();
    public static final Map<TreeType, AncientDoorBlock> ANCIENT_DOORS = new HashMap<>();

    public static final Map<TreeType, AncientLogBlock> PETRIFIED_LOGS = new HashMap<>();

    public static final FossilBlock FOSSIL = new FossilBlock();
    public static final EncasedFossilBlock ENCASED_FOSSIL = new EncasedFossilBlock();

    public static final PlantFossilBlock PLANT_FOSSIL = new PlantFossilBlock();

    public static final CleaningStationBlock CLEANING_STATION = new CleaningStationBlock();
    public static final FossilGrinderBlock FOSSIL_GRINDER = new FossilGrinderBlock();
    public static final DNASequencerBlock DNA_SEQUENCER = new DNASequencerBlock();
    public static final DNASynthesizerBlock DNA_SYNTHESIZER = new DNASynthesizerBlock();
    public static final EmbryonicMachineBlock EMBRYONIC_MACHINE = new EmbryonicMachineBlock();
    public static final EmbryoCalcificationMachineBlock EMBRYO_CALCIFICATION_MACHINE = new EmbryoCalcificationMachineBlock();
    public static final IncubatorBlock INCUBATOR = new IncubatorBlock();
    public static final DNAExtractorBlock DNA_EXTRACTOR = new DNAExtractorBlock();
    public static final DNACombinatorHybridizerBlock DNA_COMBINATOR_HYBRIDIZER = new DNACombinatorHybridizerBlock();

    public static final AmberBlock AMBER_ORE = new AmberBlock();
    public static final IceShardBlock ICE_SHARD = new IceShardBlock();

    public static final GypsumStoneBlock GYPSUM_STONE = new GypsumStoneBlock();
    public static final Block GYPSUM_COBBLESTONE = new BasicBlock(Material.ROCK).setHardness(1.5F);
    public static final Block GYPSUM_BRICKS = new BasicBlock(Material.ROCK).setHardness(2.0F);

    public static final Block REINFORCED_STONE = new BasicBlock(Material.ROCK).setHardness(2.0F);
    public static final Block REINFORCED_BRICKS = new BasicBlock(Material.ROCK).setHardness(3.0F);

    public static final Map<EnumDyeColor, CultivatorTopBlock> CULTIVATOR_TOP = Maps.newHashMap();
    public static final Map<EnumDyeColor, CultivatorBottomBlock> CULTIVATOR_BOTTOM = Maps.newHashMap();

    public static final DisplayBlock DISPLAY_BLOCK = new DisplayBlock();

    public static final ClearGlassBlock CLEAR_GLASS = new ClearGlassBlock();

    public static final Map<FossilizedTrackwayBlock.TrackwayType, FossilizedTrackwayBlock> FOSSILIZED_TRACKWAY = Maps.newHashMap();
    public static final Map<NestFossilBlock.Variant, NestFossilBlock> NEST_FOSSIL = Maps.newHashMap();
    public static final Map<NestFossilBlock.Variant, NestFossilBlock> ENCASED_NEST_FOSSIL = Maps.newHashMap();

    public static final SmallRoyalFernBlock SMALL_ROYAL_FERN = new SmallRoyalFernBlock();
    public static final SmallChainFernBlock SMALL_CHAIN_FERN = new SmallChainFernBlock();
    public static final SmallCycadBlock SMALL_CYCAD = new SmallCycadBlock();
    public static final BennettitaleanCycadeoideaBlock CYCADEOIDEA = new BennettitaleanCycadeoideaBlock();
    public static final AncientPlantBlock CRY_PANSY = new AncientPlantBlock();
    public static final ScalyTreeFernBlock SCALY_TREE_FERN = new ScalyTreeFernBlock();
    public static final CycadZamitesBlock ZAMITES = new CycadZamitesBlock();
    public static final DicksoniaBlock DICKSONIA = new DicksoniaBlock();
    public static final DicroidiumZuberiBlock DICROIDIUM_ZUBERI = new DicroidiumZuberiBlock();
    public static final AjuginuculaSmithiiBlock AJUGINUCULA_SMITHII = new AjuginuculaSmithiiBlock();
    public static final WildOnionBlock WILD_ONION = new WildOnionBlock();
    public static final GracilariaBlock GRACILARIA = new GracilariaBlock();
    public static final DictyophyllumBlock DICTYOPHYLLUM = new DictyophyllumBlock();
    public static final WestIndianLilacBlock WEST_INDIAN_LILAC = new WestIndianLilacBlock();
    public static final SerennaVeriformansBlock SERENNA_VERIFORMANS = new SerennaVeriformansBlock();
    public static final SmallPlantBlock LADINIA_SIMPLEX = new SmallPlantBlock();
    public static final OrontiumMackiiBlock ORONTIUM_MACKII = new OrontiumMackiiBlock();
    public static final UmaltolepisBlock UMALTOLEPIS = new UmaltolepisBlock();
    public static final LiriodendritesBlock LIRIODENDRITES = new LiriodendritesBlock();
    public static final RaphaeliaBlock RAPHAELIA = new RaphaeliaBlock();
    public static final EncephalartosBlock ENCEPHALARTOS = new EncephalartosBlock();
    public static final WildPotatoBlock WILD_POTATO_PLANT = new WildPotatoBlock();
    public static final RhamnusSalifocifusBlock RHAMNUS_SALICIFOLIUS_PLANT = new RhamnusSalifocifusBlock();
    public static final SmallPlantBlock CINNAMON_FERN = new SmallPlantBlock();
    public static final SmallPlantBlock BRISTLE_FERN = new SmallPlantBlock();
    public static final DoublePlantBlock TEMPSKYA = new DoublePlantBlock();
    public static final SmallPlantBlock WOOLLY_STALKED_BEGONIA = new SmallPlantBlock();
    public static final SmallPlantBlock LARGESTIPULE_LEATHER_ROOT = new SmallPlantBlock();
    public static final DoublePlantBlock RHACOPHYTON = new DoublePlantBlock();
    public static final DoublePlantBlock GRAMINIDITES_BAMBUSOIDES = new DoublePlantBlock();
    public static final DoublePlantBlock HELICONIA = new DoublePlantBlock();

    public static final AncientCoralBlock ENALLHELIA = new AncientCoralBlock();
    public static final AncientCoralBlock AULOPORA = new AncientCoralBlock();
    public static final AncientCoralBlock CLADOCHONUS = new AncientCoralBlock();
    public static final AncientCoralBlock LITHOSTROTION = new AncientCoralBlock();
    public static final AncientCoralBlock STYLOPHYLLOPSIS = new AncientCoralBlock();
    public static final AncientCoralBlock HIPPURITES_RADIOSUS = new AncientCoralBlock();

    public static final PeatBlock PEAT = new PeatBlock();
    public static final Block PEAT_MOSS = new PeatMossBlock();
    public static final MossBlock MOSS = new MossBlock();

    public static final FeederBlock FEEDER = new FeederBlock();

    public static final BugCrateBlock BUG_CRATE = new BugCrateBlock();

    public static final SwarmBlock PLANKTON_SWARM = new SwarmBlock(() -> ItemHandler.PLANKTON);
    public static final SwarmBlock KRILL_SWARM = new SwarmBlock(() -> ItemHandler.KRILL);
    public static final TourRailBlock TOUR_RAIL = new TourRailBlock(TourRailBlock.SpeedType.NONE);
    public static final TourRailBlock TOUR_RAIL_SLOW = new TourRailBlock(TourRailBlock.SpeedType.SLOW);
    public static final TourRailBlock TOUR_RAIL_MEDIUM = new TourRailBlock(TourRailBlock.SpeedType.MEDIUM);
    public static final TourRailBlock TOUR_RAIL_FAST = new TourRailBlock(TourRailBlock.SpeedType.FAST);

    public static final SkeletonAssemblyBlock SKELETON_ASSEMBLY = new SkeletonAssemblyBlock();

    public static ElectricFencePoleBlock LOW_SECURITY_FENCE_POLE = new ElectricFencePoleBlock(FenceType.LOW);

    public static ElectricFenceBaseBlock LOW_SECURITY_FENCE_BASE = new ElectricFenceBaseBlock(FenceType.LOW);

    public static ElectricFenceWireBlock LOW_SECURITY_FENCE_WIRE = new ElectricFenceWireBlock(FenceType.LOW);

    public static PaleoBaleBlock PALEO_BALE_CYCADEOIDEA = new PaleoBaleBlock(PaleoBaleBlock.Variant.CYCADEOIDEA);
    public static PaleoBaleBlock PALEO_BALE_CYCAD = new PaleoBaleBlock(PaleoBaleBlock.Variant.CYCAD);
    public static PaleoBaleBlock PALEO_BALE_FERN = new PaleoBaleBlock(PaleoBaleBlock.Variant.FERN);
    public static PaleoBaleBlock PALEO_BALE_LEAVES = new PaleoBaleBlock(PaleoBaleBlock.Variant.LEAVES);
    public static PaleoBaleBlock PALEO_BALE_OTHER = new PaleoBaleBlock(PaleoBaleBlock.Variant.OTHER);

    private static CreativeTabs blockTab = TabHandler.BLOCKS;
    private static CreativeTabs plantTab = TabHandler.PLANTS;
    private static CreativeTabs fossilTab = TabHandler.FOSSILS;

    static
    {

        for (TreeType type : TreeType.values()) {
            initilizeTreeType(type);
        }

        for(EnumDyeColor color : EnumDyeColor.values()) {
            CULTIVATOR_BOTTOM.put(color, new CultivatorBottomBlock(color));
            CULTIVATOR_TOP.put(color, new CultivatorTopBlock(color));
        }
        for (FossilizedTrackwayBlock.TrackwayType trackwayType : FossilizedTrackwayBlock.TrackwayType.values()) {
            FOSSILIZED_TRACKWAY.put(trackwayType, new FossilizedTrackwayBlock(trackwayType));
        }

        for(NestFossilBlock.Variant variant : NestFossilBlock.Variant.values()) {
            NEST_FOSSIL.put(variant, new NestFossilBlock(variant, false));
            ENCASED_NEST_FOSSIL.put(variant, new NestFossilBlock(variant, true));
        }
    }

    public static void init()
    {
        //Fossils
        registerBlock(ENCASED_FOSSIL, "Encased Fossil Block", null);

        registerBlock(FOSSIL, "Fossil Block", fossilTab);
        registerBlock(PLANT_FOSSIL, "Plant Fossil Block", fossilTab);

        for(FossilizedTrackwayBlock.TrackwayType type : FossilizedTrackwayBlock.TrackwayType.values()) {
            registerBlock(FOSSILIZED_TRACKWAY.get(type), "Fossilized Trackway " + type.getName(), fossilTab);
        }
        for (NestFossilBlock.Variant variant : NestFossilBlock.Variant.values()) {
            registerBlock(NEST_FOSSIL.get(variant), "Nest Fossil " + variant.getName(), fossilTab);
            registerBlock(ENCASED_NEST_FOSSIL.get(variant), "Encased Nest Fossil " + variant.getName(), fossilTab);
        }

        //Generic Blocks
        registerBlock(AMBER_ORE, "Amber Ore", blockTab);
        registerBlock(ICE_SHARD, "Ice Shard", blockTab);
        registerBlock(GYPSUM_STONE, "Gypsum Stone", blockTab);
        registerBlock(GYPSUM_COBBLESTONE, "Gypsum Cobblestone", blockTab);
        registerBlock(GYPSUM_BRICKS, "Gypsum Bricks", blockTab);
        registerBlock(REINFORCED_STONE, "Reinforced Stone", blockTab);
        registerBlock(REINFORCED_BRICKS, "Reinforced Bricks", blockTab);
        registerBlock(CLEAR_GLASS, "Clear Glass", blockTab);

        //Plants
        registerBlock(AJUGINUCULA_SMITHII, "Ajuginucula Smithii", null);
        registerBlock(SCALY_TREE_FERN, "Scaly Tree Fern", null);
        registerBlock(ZAMITES, "Cycad Zamites", null);
        registerBlock(DICKSONIA, "Dicksonia", null);
        registerBlock(GRACILARIA, "Gracilaria Seaweed", null);
        registerBlock(DICROIDIUM_ZUBERI, "Dicroidium Zuberi", null);
        registerBlock(DICTYOPHYLLUM, "Dictyophyllum",null);
        registerBlock(WEST_INDIAN_LILAC, "West Indian Lilac", null);
        registerBlock(SERENNA_VERIFORMANS, "Serenna Veriformans", null);
        registerBlock(LADINIA_SIMPLEX, "Ladinia Simplex", null);
        registerBlock(ORONTIUM_MACKII, "Orontium Mackii", null);
        registerBlock(UMALTOLEPIS, "Umaltolepis", null);
        registerBlock(LIRIODENDRITES, "Liriodendrites", null);
        registerBlock(RAPHAELIA, "Raphaelia", null);
        registerBlock(ENCEPHALARTOS, "Encephalartos", null);
        registerBlock(WILD_POTATO_PLANT, "Wild Potato Plant", null);
        registerBlock(RHAMNUS_SALICIFOLIUS_PLANT, "Rhamnus Salicifolius", null);
        registerBlock(BRISTLE_FERN, "Bristle Fern", null);
        registerBlock(CINNAMON_FERN, "Cinnamon Fern", null);
        registerBlock(TEMPSKYA, "Tempskya", null);
        registerBlock(WOOLLY_STALKED_BEGONIA, "Woolly Stalked Begonia", null);
        registerBlock(LARGESTIPULE_LEATHER_ROOT, "Largestipule Leather Root", null);
        registerBlock(RHACOPHYTON, "Rhacophyton", null);
        registerBlock(GRAMINIDITES_BAMBUSOIDES, "Graminidites Bambusoides", null);
        registerBlock(ENALLHELIA, "Enallhelia", null);
        registerBlock(AULOPORA, "Aulopora", null);
        registerBlock(CLADOCHONUS, "Cladochonus", null);
        registerBlock(LITHOSTROTION, "Lithostrotion", null);
        registerBlock(STYLOPHYLLOPSIS, "Stylophyllopsis", null);
        registerBlock(HIPPURITES_RADIOSUS, "Hippurites Radiosus", null);
        registerBlock(HELICONIA, "Heliconia", null);

        registerBlock(SMALL_ROYAL_FERN, "Small Royal Fern", plantTab);
        registerBlock(SMALL_CHAIN_FERN, "Small Chain Fern", plantTab);
        registerBlock(SMALL_CYCAD, "Small Cycad", plantTab);
        registerBlock(CYCADEOIDEA, "Bennettitalean Cycadeoidea", plantTab);
        registerBlock(CRY_PANSY, "Cry Pansy", plantTab);
        registerBlock(WILD_ONION, "Wild Onion Plant", plantTab);

        registerBlock(MOSS, "Moss", plantTab);
        registerBlock(PEAT, "Peat", plantTab);
        registerBlock(PEAT_MOSS, "Peat Moss", plantTab);


        registerBlock(PLANKTON_SWARM, "Plankton Swarm", null);
        registerBlock(KRILL_SWARM, "Krill Swarm", null);

        registerBlock(TourRailBlockEntity.class, TOUR_RAIL, "Tour Rail", blockTab);
        registerBlock(TOUR_RAIL_SLOW, "Tour Rail Slow", blockTab);
        registerBlock(TOUR_RAIL_MEDIUM, "Tour Rail Medium", blockTab);
        registerBlock(TOUR_RAIL_FAST, "Tour Rail Fast", blockTab);

        //Machines
        for(EnumDyeColor color : EnumDyeColor.values()) { //TODO: make it so blockentities can be registered on their own
            if(color == EnumDyeColor.WHITE) {
                registerBlock(CultivatorBlockEntity.class, CULTIVATOR_BOTTOM.get(color), "Cultivate Bottom " + color.getDyeColorName(), blockTab);
            } else {
                registerBlock(CULTIVATOR_BOTTOM.get(color), "Cultivate Bottom " + color.getDyeColorName(), blockTab);
            }
            registerBlock(CULTIVATOR_TOP.get(color), "Cultivate Top " + color.getDyeColorName(), null);
        }

        registerBlock(SKELETON_ASSEMBLY, "Skeleton Assembly", blockTab);
        registerBlock(CleaningStationBlockEntity.class, CLEANING_STATION, "Cleaning Station", blockTab);
        registerBlock(FossilGrinderBlockEntity.class, FOSSIL_GRINDER, "Fossil Grinder", blockTab);
        registerBlock(DNASequencerBlockEntity.class, DNA_SEQUENCER, "DNA Sequencer", blockTab);
        registerBlock(DNASynthesizerBlockEntity.class, DNA_SYNTHESIZER, "DNA Synthesizer", blockTab);
        registerBlock(EmbryonicMachineBlockEntity.class, EMBRYONIC_MACHINE, "Embryonic Machine, blockTab", blockTab);
        registerBlock(EmbryoCalcificationMachineBlockEntity.class, EMBRYO_CALCIFICATION_MACHINE, "Embryo Calcification Machine", blockTab);
        registerBlock(DNAExtractorBlockEntity.class, DNA_EXTRACTOR, "DNA Extractor", blockTab);
        registerBlock(DNACombinatorHybridizerBlockEntity.class, DNA_COMBINATOR_HYBRIDIZER, "DNA Combinator Hybridizer", blockTab);
        registerBlock(IncubatorBlockEntity.class, INCUBATOR, "Incubator", blockTab);
        registerBlock(DisplayBlockEntity.class, DISPLAY_BLOCK, "Display Block", blockTab);
        registerBlock(FeederBlockEntity.class, FEEDER, "Feeder", blockTab);
        registerBlock(BugCrateBlockEntity.class, BUG_CRATE, "Bug Crate", blockTab);

        registerBlock(ElectricFenceWireBlockEntity.class, "tileEntityElectricFence", LOW_SECURITY_FENCE_WIRE, "Low Security Fence Wire",null);
        registerBlock(ElectricFencePoleBlockEntity.class, "tileEntityElectricPole", LOW_SECURITY_FENCE_POLE, "Low Security Fence Pole", null);
        registerBlock(ElectricFenceBaseBlockEntity.class, "tileEntityElectricBase", LOW_SECURITY_FENCE_BASE, "Low Security Fence Base", null);


        registerBlock(PALEO_BALE_OTHER, "Paleo Bale Other", plantTab);
        registerBlock(PALEO_BALE_CYCADEOIDEA, "Paleo Bale Cycadeoidea", plantTab);
        registerBlock(PALEO_BALE_CYCAD, "Paleo Bale Cycad", plantTab);
        registerBlock(PALEO_BALE_FERN, "Paleo Bale Fern", plantTab);
        registerBlock(PALEO_BALE_LEAVES, "Paleo Bale Leaves", plantTab);

        for (TreeType type : TreeType.values()) {
            registerTreeType(type);
        }
    }

    private static void initilizeTreeType(TreeType type) {
        AncientPlanksBlock planks = new AncientPlanksBlock(type);
        AncientLogBlock log = new AncientLogBlock(type, false);
        AncientLogBlock petrifiedLog = new AncientLogBlock(type, true);
        AncientLeavesBlock leaves = new AncientLeavesBlock(type);
        AncientSaplingBlock sapling = new AncientSaplingBlock(type);
        AncientStairsBlock stair = new AncientStairsBlock(type, planks.getDefaultState());
        AncientSlabHalfBlock slab = new AncientSlabHalfBlock(type, planks.getDefaultState());
        AncientDoubleSlabBlock doubleSlab = new AncientDoubleSlabBlock(type, slab, planks.getDefaultState());
        AncientFenceBlock fence = new AncientFenceBlock(type);
        AncientFenceGateBlock fenceGate = new AncientFenceGateBlock(type);
        AncientDoorBlock door = new AncientDoorBlock(type);

        ANCIENT_PLANKS.put(type, planks);
        ANCIENT_LOGS.put(type, log);
        ANCIENT_LEAVES.put(type, leaves);
        ANCIENT_SAPLINGS.put(type, sapling);
        ANCIENT_STAIRS.put(type, stair);
        ANCIENT_SLABS.put(type, slab);
        ANCIENT_DOUBLE_SLABS.put(type, doubleSlab);
        ANCIENT_FENCES.put(type, fence);
        ANCIENT_FENCE_GATES.put(type, fenceGate);
        ANCIENT_DOORS.put(type, door);
        PETRIFIED_LOGS.put(type, petrifiedLog);

        Blocks.FIRE.setFireInfo(leaves, 30, 60);
        Blocks.FIRE.setFireInfo(planks, 5, 20);
        Blocks.FIRE.setFireInfo(log, 5, 5);
        Blocks.FIRE.setFireInfo(petrifiedLog, 5, 5);
        Blocks.FIRE.setFireInfo(doubleSlab, 5, 20);
        Blocks.FIRE.setFireInfo(slab, 5, 20);
        Blocks.FIRE.setFireInfo(stair, 5, 20);
        Blocks.FIRE.setFireInfo(fence, 5, 20);
        Blocks.FIRE.setFireInfo(fenceGate, 5, 20);
    }

    //Trees
    private static void registerTreeType(TreeType type) {
        String typeName = type.name();
        registerBlock(ANCIENT_DOORS.get(type), typeName + " Door", null);
        registerBlock(ANCIENT_DOUBLE_SLABS.get(type), typeName + " Double Slab", null);

        registerBlock(ANCIENT_PLANKS.get(type), typeName + " Planks", plantTab);
        registerBlock(ANCIENT_LOGS.get(type), typeName + " Log", plantTab);
        registerBlock(PETRIFIED_LOGS.get(type), typeName + " Log Petrified", plantTab);
        registerBlock(ANCIENT_LEAVES.get(type), typeName + " Leaves", plantTab);
        registerBlock(ANCIENT_SAPLINGS.get(type), typeName + " Sapling", plantTab);
        registerBlock(ANCIENT_STAIRS.get(type), typeName + " Stairs", plantTab);
        registerBlock(ANCIENT_SLABS.get(type), typeName + " Slab", plantTab);
        registerBlock(ANCIENT_FENCES.get(type), typeName + " Fence", plantTab);
        registerBlock(ANCIENT_FENCE_GATES.get(type), typeName + " Fence Gate", plantTab);
    }

    /**
     * Ores need to be registered after blocks and items.
     */
    public static void registerOres()
    {
        for(int i = 0; i < TreeType.values().length; i++)
        {
            OreDictionary.registerOre("logWood", ANCIENT_LOGS.get(TreeType.values()[i]));
            OreDictionary.registerOre("logWood", PETRIFIED_LOGS.get(TreeType.values()[i]));
            OreDictionary.registerOre("plankWood", ANCIENT_PLANKS.get(TreeType.values()[i]));
            OreDictionary.registerOre("treeLeaves", ANCIENT_LEAVES.get(TreeType.values()[i]));
            OreDictionary.registerOre("treeSapling", ANCIENT_SAPLINGS.get(TreeType.values()[i]));
            OreDictionary.registerOre("slabWood", ANCIENT_SLABS.get(TreeType.values()[i]));
            OreDictionary.registerOre("stairWood", ANCIENT_STAIRS.get(TreeType.values()[i]));
            OreDictionary.registerOre("fenceWood", ANCIENT_FENCES.get(TreeType.values()[i]));
            OreDictionary.registerOre("gateWood", ANCIENT_FENCE_GATES.get(TreeType.values()[i]));
            OreDictionary.registerOre("fenceGateWood", ANCIENT_FENCE_GATES.get(TreeType.values()[i]));
            OreDictionary.registerOre("doorWood", ANCIENT_DOORS.get(TreeType.values()[i]));
        }
        OreDictionary.registerOre("fossil", FOSSIL);

    }

    private static void registerBlock(Class<? extends TileEntity> tileEntity, Block block, String name, CreativeTabs creativeTab)
    {
        registerBlock(tileEntity, name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_"), block, name, creativeTab);
    }

    private static void registerBlock(Class<? extends TileEntity> tileEntity, String tileEntityName,  Block block, String name, CreativeTabs creativeTab)
    {
        registerBlock(block, name, creativeTab);
        GameRegistry.registerTileEntity(tileEntity, new ResourceLocation(JurassiCraft.MODID, tileEntityName));
    }

    private static void registerBlock(Block block, String name, CreativeTabs creativeTab)
    {
        if(block instanceof SubBlocksBlock) {
            RegistryHandler.registerBlockWithCustomItem(block, ((SubBlocksBlock) block).getItemBlock(), name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_"));
            block.setCreativeTab(creativeTab);
        }
        else if(block instanceof NoItemBlock) {
            RegistryHandler.registerBlock(block, name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_"));
        } else {
            RegistryHandler.registerBlockWithItem(block, name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_"));
            block.setCreativeTab(creativeTab);

        }
    }

    public static <T extends Block & BlockStayCheck> void checkForDrop(World world, BlockPos pos, IBlockState state, T block){
        if (!block.canBlockStay(world, pos)) {
            block.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    public interface BlockStayCheck{
        boolean canBlockStay(World world, BlockPos pos);
    }
}
