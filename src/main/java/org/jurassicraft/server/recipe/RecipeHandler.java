package org.jurassicraft.server.recipe;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.PaleoBaleBlock;
import org.jurassicraft.server.block.plant.DoublePlantBlock;
import org.jurassicraft.server.block.tree.AncientPlanksBlock;
import org.jurassicraft.server.block.tree.TreeType;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.entity.item.AttractionSignEntity;
import org.jurassicraft.server.item.ItemHandler;

public class RecipeHandler {
    public static void init() {
        for (Dinosaur dinosaur : EntityHandler.getRegisteredDinosaurs()) {
            int id = EntityHandler.getDinosaurId(dinosaur);

            GameRegistry.addSmelting(new ItemStack(ItemHandler.DINOSAUR_MEAT, 1, id), new ItemStack(ItemHandler.DINOSAUR_STEAK, 1, id), 5F);
        }
//
//        for (TreeType type : TreeType.values()) {
//            AncientPlanksBlock planks = BlockHandler.ANCIENT_PLANKS.get(type);
//
//            GameRegistry.addShapelessRecipe(new ItemStack(planks, 4), BlockHandler.ANCIENT_LOGS.get(type));
//            GameRegistry.addRecipe(new ItemStack(BlockHandler.ANCIENT_STAIRS.get(type), 4), "w  ", "ww ", "www", 'w', planks);
//            GameRegistry.addRecipe(new ItemStack(BlockHandler.ANCIENT_STAIRS.get(type), 4), "  w", " ww", "www", 'w', planks);
//            GameRegistry.addRecipe(new ItemStack(BlockHandler.ANCIENT_SLABS.get(type), 6), "www", 'w', planks);
//            GameRegistry.addRecipe(new ItemStack(ItemHandler.ANCIENT_DOORS.get(type), 3), "ww", "ww", "ww", 'w', planks);
//            GameRegistry.addRecipe(new ItemStack(BlockHandler.ANCIENT_FENCES.get(type), 3), "wsw", "wsw", 'w', planks, 's', Items.STICK);
//            GameRegistry.addRecipe(new ItemStack(BlockHandler.ANCIENT_FENCE_GATES.get(type), 3), "sws", "sws", 'w', planks, 's', Items.STICK);
//
//            GameRegistry.addSmelting(BlockHandler.ANCIENT_LOGS.get(type), new ItemStack(Items.COAL, 1, 1), 0.15F);
//        }
//
//        GameRegistry.addSmelting(new ItemStack(BlockHandler.GYPSUM_COBBLESTONE), new ItemStack(BlockHandler.GYPSUM_STONE), 1.5F);
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.IRON_BLADES), "I I", " S ", "I I", 'I', Items.IRON_INGOT, 'S', Items.STICK);
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.IRON_ROD, 4), "ISI", "ISI", "ISI", 'I', Items.IRON_INGOT, 'S', Items.STICK);
//        GameRegistry.addRecipe(new ItemStack(BlockHandler.FOSSIL_GRINDER), "lBl", "rRr", "IPI", 'I', Items.IRON_INGOT, 'R', ItemHandler.IRON_ROD, 'B', ItemHandler.IRON_BLADES, 'r', Items.REDSTONE, 'l', new ItemStack(Items.DYE, 1, 4), 'P', Blocks.PISTON);
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.PLASTER_AND_BANDAGE, 9), "PGP", "GWG", "PGP", 'P', Items.PAPER, 'W', Blocks.WOOL, 'G', ItemHandler.GYPSUM_POWDER);
//        GameRegistry.addRecipe(new ItemStack(BlockHandler.GYPSUM_STONE), "GGG", "GGG", "GGG", 'G', ItemHandler.GYPSUM_POWDER);
//        GameRegistry.addRecipe(new ItemStack(BlockHandler.GYPSUM_BRICKS, 4), "SS", "SS", 'S', BlockHandler.GYPSUM_STONE);
//        GameRegistry.addRecipe(new ItemStack(BlockHandler.CLEANING_STATION), "iii", "RGR", "IBI", 'i', Items.IRON_INGOT, 'B', Items.BUCKET, 'G', Blocks.GLASS_PANE, 'R', Items.REDSTONE, 'I', Blocks.IRON_BLOCK);
//        GameRegistry.addRecipe(new ItemStack(BlockHandler.REINFORCED_STONE, 8), "PPP", "PWP", "PPP", 'P', Blocks.STONE, 'W', Items.IRON_INGOT);
//        GameRegistry.addRecipe(new ItemStack(BlockHandler.REINFORCED_BRICKS, 8), "PPP", "PWP", "PPP", 'P', Blocks.STONEBRICK, 'W', Items.IRON_INGOT);
//        GameRegistry.addRecipe(new ItemStack(BlockHandler.REINFORCED_BRICKS, 4), "SS", "SS", 'S', BlockHandler.REINFORCED_STONE);
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.EMPTY_TEST_TUBE, 8), "G", "G", 'G', Blocks.GLASS);
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.PETRI_DISH, 4), "G G", "GGG", 'G', Blocks.GLASS_PANE);
//        GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.PETRI_DISH_AGAR, 4), ItemHandler.PETRI_DISH, ItemHandler.PETRI_DISH, ItemHandler.PETRI_DISH, ItemHandler.PETRI_DISH, ItemHandler.LIQUID_AGAR);
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.EMPTY_SYRINGE), " I ", " T ", "III", 'I', "nuggetIron", 'T', ItemHandler.EMPTY_TEST_TUBE));
//        GameRegistry.addSmelting(new ItemStack(Items.POTIONITEM, 1, 0), new ItemStack(ItemHandler.DNA_NUCLEOTIDES), 1.0F);
//        GameRegistry.addRecipe(new ItemStack(BlockHandler.DNA_EXTRACTOR), "III", "SAP", "HII", 'I', Items.IRON_INGOT, 'S', ItemHandler.COMPUTER_SCREEN, 'A', ItemHandler.DNA_ANALYZER, 'P', Blocks.GLASS_PANE, 'H', ItemHandler.DISC_DRIVE);
//        GameRegistry.addRecipe(new ItemStack(BlockHandler.DNA_SEQUENCER), "IAI", "SAI", "HAK", 'I', Items.IRON_INGOT, 'A', ItemHandler.DNA_ANALYZER, 'S', ItemHandler.COMPUTER_SCREEN, 'H', ItemHandler.DISC_DRIVE, 'K', ItemHandler.KEYBOARD);
//        GameRegistry.addRecipe(new ItemStack(BlockHandler.DNA_SYNTHESIZER), "IIS", "ICD", "IIC", 'I', Items.IRON_INGOT, 'S', ItemHandler.COMPUTER_SCREEN, 'C', ItemHandler.ADVANCED_CIRCUIT, 'D', ItemHandler.DISC_DRIVE);
//
//        for (int i = 0; i < 16; i++) {
//            GameRegistry.addRecipe(new ItemStack(BlockHandler.CULTIVATOR_BOTTOM, 1, i), "GIG", "G G", "ICI", 'G', new ItemStack(Blocks.STAINED_GLASS_PANE, 1, i), 'I', Items.IRON_INGOT, 'C', Items.COMPARATOR);
//        }
//
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.EMBRYONIC_MACHINE), "npb", "ngb", "iib", 'n', "nuggetIron", 'p', Blocks.PISTON, 'g', Items.GLOWSTONE_DUST, 'b', Blocks.STONE_BUTTON, 'i', Items.IRON_INGOT));
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.EMBRYONIC_MACHINE), "npb", "ngb", "iib", 'n', "nuggetIron", 'p', Blocks.PISTON, 'g', Items.GLOWSTONE_DUST, 'b', Blocks.WOODEN_BUTTON, 'i', Items.IRON_INGOT));
//        GameRegistry.addRecipe(new ItemStack(BlockHandler.EMBRYO_CALCIFICATION_MACHINE), "iBK", "RCR", "RIR", 'i', Items.IRON_INGOT, 'I', Blocks.IRON_BLOCK, 'B', Items.BOWL, 'K', ItemHandler.KEYBOARD, 'R', ItemHandler.IRON_ROD, 'C', ItemHandler.BASIC_CIRCUIT);
//        GameRegistry.addRecipe(new ItemStack(BlockHandler.INCUBATOR), "GIG", "RRR", "IKI", 'I', Items.IRON_INGOT, 'R', Items.COMPARATOR, 'G', Blocks.GLASS, 'K', ItemHandler.KEYBOARD);
//        GameRegistry.addRecipe(new ItemStack(BlockHandler.DNA_COMBINATOR_HYBRIDIZER), "SCS", "IDI", " K ", 'S', ItemHandler.COMPUTER_SCREEN, 'C', ItemHandler.BASIC_CIRCUIT, 'I', Items.IRON_INGOT, 'D', ItemHandler.DISC_DRIVE, 'L', ItemHandler.KEYBOARD);
//        GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.PLANT_CELLS_PETRI_DISH), ItemHandler.PLANT_CELLS, ItemHandler.PETRI_DISH_AGAR);
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.SKELETON_ASSEMBLY), "BPP", "WCW", "FQF", 'B', "dyeBlue", 'P', Items.PAPER, 'W', "slabWood", 'C', Blocks.CRAFTING_TABLE, 'F', Blocks.OAK_FENCE, 'Q', BlockHandler.LOW_SECURITY_FENCE_WIRE));
//
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.IRON_NUGGET, 9), "i", 'i', Items.IRON_INGOT);
//        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemHandler.BASIC_CIRCUIT, 2), "nuggetIron", "nuggetIron", "nuggetIron", "nuggetIron", Items.GOLD_NUGGET, Items.GOLD_NUGGET, Items.GOLD_NUGGET, Items.GOLD_NUGGET, Items.REDSTONE));
//        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemHandler.ADVANCED_CIRCUIT, 1), ItemHandler.BASIC_CIRCUIT, ItemHandler.BASIC_CIRCUIT, "nuggetIron", Items.GOLD_NUGGET, Items.REDSTONE));
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.STORAGE_DISC), " I ", "ICI", " I ", 'I', "nuggetIron", 'C', ItemHandler.BASIC_CIRCUIT));
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.LASER), "IDI", "ICI", "IRI", 'I', "nuggetIron", 'C', ItemHandler.BASIC_CIRCUIT, 'D', Items.DIAMOND, 'R', Items.REDSTONE));
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.DISC_DRIVE), "CLI", "i  ", "iib", 'i', "nuggetIron", 'C', ItemHandler.ADVANCED_CIRCUIT, 'L', ItemHandler.LASER, 'I', Items.IRON_INGOT, 'b', Blocks.STONE_BUTTON));
//
//        GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.AJUGINUCULA_SMITHII_OIL), ItemHandler.AJUGINUCULA_SMITHII_LEAVES, ItemHandler.AJUGINUCULA_SMITHII_LEAVES, ItemHandler.AJUGINUCULA_SMITHII_LEAVES, ItemHandler.AJUGINUCULA_SMITHII_LEAVES, Items.GLASS_BOTTLE);
//
//        GameRegistry.addRecipe(new ItemStack(Blocks.MOSSY_COBBLESTONE, 4), "MCM", "CMC", "MCM", 'M', BlockHandler.MOSS, 'C', Blocks.COBBLESTONE);
//        GameRegistry.addRecipe(new ItemStack(Blocks.STONEBRICK, 4, 1), "MCM", "CMC", "MCM", 'M', BlockHandler.MOSS, 'C', new ItemStack(Blocks.STONEBRICK, 1, 0));
//
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.COMPUTER_SCREEN), "IGI", "RPB", "CQI", 'I', Items.IRON_INGOT, 'G', new ItemStack(Items.DYE, 1, 2), 'R', new ItemStack(Items.DYE, 1, 1), 'P', Blocks.GLASS_PANE, 'B', new ItemStack(Items.DYE, 1, 4), 'C', ItemHandler.BASIC_CIRCUIT, 'Q', Items.QUARTZ);
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.KEYBOARD), "bbb", "bbb", "psc", 'b', Blocks.STONE_BUTTON, 'p', Blocks.STONE_PRESSURE_PLATE, 's', Blocks.STONE_SLAB, 'c', ItemHandler.BASIC_CIRCUIT);
//
//        GameRegistry.addRecipe(new ItemStack(BlockHandler.PEAT_MOSS), "MMM", "MMM", "MMM", 'M', BlockHandler.MOSS);
//
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.DNA_ANALYZER), "iGi", "CLi", "iii", 'i', "nuggetIron", 'G', Blocks.GLASS, 'C', ItemHandler.ADVANCED_CIRCUIT, 'L', ItemHandler.LASER));
//
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.CAR_CHASSIS), "III", "I I", "III", 'I', Items.IRON_INGOT);
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.CAR_ENGINE_SYSTEM), "BIL", "cRC", "BpP", 'B', Blocks.IRON_BARS, 'I', Items.IRON_INGOT, 'L', Blocks.LEVER, 'c', Items.COMPARATOR, 'R', Items.REPEATER, 'C', Items.CAULDRON, 'p', Blocks.STONE_PRESSURE_PLATE, 'P', Blocks.PISTON);
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.CAR_ENGINE_SYSTEM), "LIB", "CRc", "PpB", 'B', Blocks.IRON_BARS, 'I', Items.IRON_INGOT, 'L', Blocks.LEVER, 'c', Items.COMPARATOR, 'R', Items.REPEATER, 'C', Items.CAULDRON, 'p', Blocks.STONE_PRESSURE_PLATE, 'P', Blocks.PISTON);
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.CAR_SEATS), "LWL", "LSL", "III", 'L', Items.LEATHER, 'W', Blocks.WOOL, 'S', Items.SADDLE, 'I', Items.IRON_INGOT);
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.CAR_WINDSCREEN), " I ", "IPI", " I ", 'I', Items.IRON_INGOT, 'P', Blocks.GLASS_PANE);
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.CAR_TIRE, 2), "ISI", "SPS", "ISI", 'I', new ItemStack(Items.DYE, 1, 0), 'S', Items.SLIME_BALL, 'P', Blocks.PISTON);
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.UNFINISHED_CAR), "SCW", "TEI", "TTT", 'I', Items.IRON_INGOT, 'C', ItemHandler.CAR_CHASSIS, 'W', ItemHandler.CAR_WINDSCREEN, 'T', ItemHandler.CAR_TIRE, 'E', ItemHandler.CAR_ENGINE_SYSTEM, 'S', ItemHandler.CAR_SEATS);
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.UNFINISHED_CAR), "WCS", "IET", "TTT", 'I', Items.IRON_INGOT, 'C', ItemHandler.CAR_CHASSIS, 'W', ItemHandler.CAR_WINDSCREEN, 'T', ItemHandler.CAR_TIRE, 'E', ItemHandler.CAR_ENGINE_SYSTEM, 'S', ItemHandler.CAR_SEATS);
//
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.JEEP_WRANGLER), "LRL", "TCR", "LRL", 'L', new ItemStack(Items.DYE, 1, 7), 'R', new ItemStack(Items.DYE, 1, 1), 'C', ItemHandler.UNFINISHED_CAR, 'T', ItemHandler.CAR_TIRE);
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.JEEP_WRANGLER), "LRL", "RCT", "LRL", 'L', new ItemStack(Items.DYE, 1, 7), 'R', new ItemStack(Items.DYE, 1, 1), 'C', ItemHandler.UNFINISHED_CAR, 'T', ItemHandler.CAR_TIRE);
//
////        GameRegistry.addRecipe(new ItemStack(ItemHandler.FORD_EXPLORER), "RWG", "LCR", "YYY", 'R', new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()), 'W', Blocks.GLASS_PANE, 'G', new ItemStack(Items.DYE, 1, EnumDyeColor.GREEN.getDyeDamage()), 'L', new ItemStack(Items.DYE, 1, EnumDyeColor.LIME.getDyeDamage()), 'C', ItemHandler.UNFINISHED_CAR, 'Y', new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()));
////        GameRegistry.addRecipe(new ItemStack(ItemHandler.FORD_EXPLORER), "GWR", "RCL", "YYY", 'R', new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()), 'W', Blocks.GLASS_PANE, 'G', new ItemStack(Items.DYE, 1, EnumDyeColor.GREEN.getDyeDamage()), 'L', new ItemStack(Items.DYE, 1, EnumDyeColor.LIME.getDyeDamage()), 'C', ItemHandler.UNFINISHED_CAR, 'Y', new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()));
//
//        GameRegistry.addRecipe(new ItemStack(BlockHandler.FEEDER), "TDT", "ICI", "SSS", 'T', Blocks.IRON_TRAPDOOR, 'D', Blocks.DISPENSER, 'I', Items.IRON_INGOT, 'C', Blocks.CHEST, 'S', Blocks.COBBLESTONE);
//
//        GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.CHILEAN_SEA_BASS), Items.COOKED_FISH, ItemHandler.WILD_ONION, Items.CARROT, ItemHandler.AJUGINUCULA_SMITHII_LEAVES);
//
//        GameRegistry.addSmelting(ItemHandler.GRACILARIA, new ItemStack(ItemHandler.LIQUID_AGAR), 0.5F);
//
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.MURAL), "IWI", "WPW", "IWI", 'I', Items.IRON_INGOT, 'W', Blocks.WOOL, 'P', Items.PAINTING);
//
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.AMBER_CANE), "A", "|", "|", 'A', ItemHandler.AMBER, '|', Items.STICK);
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.AMBER_KEYCHAIN), "A", "#", 'A', ItemHandler.AMBER, '#', Items.IRON_INGOT);
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.MR_DNA_KEYCHAIN), "A", "#", 'A', ItemHandler.DNA_NUCLEOTIDES, '#', Items.IRON_INGOT);
//
//        GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.OILED_POTATO_STRIPS), ItemHandler.WILD_POTATO, ItemHandler.WILD_POTATO, ItemHandler.WILD_POTATO, ItemHandler.WILD_POTATO, ItemHandler.AJUGINUCULA_SMITHII_OIL);
//        GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.OILED_POTATO_STRIPS, 4), Items.POTATO, Items.POTATO, ItemHandler.AJUGINUCULA_SMITHII_OIL);
//        GameRegistry.addSmelting(ItemHandler.OILED_POTATO_STRIPS, new ItemStack(ItemHandler.FUN_FRIES), 0.5F);
//
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.PADDOCK_SIGN), "III", "SSS", "III", 'I', "nuggetIron", 'S', Items.SIGN));
//
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.FIELD_GUIDE), "WBW", "PPP", "WBW", 'W', new ItemStack(Blocks.WOOL, 1, 3), 'B', Items.BONE, 'P', Items.PAPER);
//
//        GameRegistry.addRecipe(new ItemStack(BlockHandler.CLEAR_GLASS, 8), "GGG", "GIG", "GGG", 'G', Blocks.GLASS, 'I', Items.IRON_INGOT);
//
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.BUG_CRATE), "III", "PCP", "SSS", 'I', "nuggetIron", 'P', Blocks.PLANKS, 'C', Blocks.CHEST, 'S', Blocks.STONE_SLAB));
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.BUG_CRATE), "III", "PCP", "SSS", 'I', "nuggetIron", 'P', Blocks.PLANKS, 'C', Blocks.CHEST, 'S', Blocks.STONE_SLAB2));
//
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.FINE_NET), "SsW", " ss", "s S", 'S', Items.STRING, 's', Items.STICK, 'W', Blocks.WOOL);
//
//        GameRegistry.addRecipe(new ItemStack(BlockHandler.LOW_SECURITY_FENCE_POLE, 2), "R", "I", "I", 'R', Items.REDSTONE, 'I', Items.IRON_INGOT);
//        GameRegistry.addRecipe(new ItemStack(BlockHandler.LOW_SECURITY_FENCE_WIRE, 16), "III", " R ", "III", 'I', Items.IRON_INGOT, 'R', Items.REDSTONE);
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.LOW_SECURITY_FENCE_BASE, 6), "iIi", "SSS", " R ", 'i', "nuggetIron", 'I', Items.IRON_INGOT, 'S', Blocks.STONE, 'R', Items.REDSTONE));
//
//        GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.PHOENIX_SEEDS), ItemHandler.PHOENIX_FRUIT);
//
//        GameRegistry.addSmelting(ItemHandler.WILD_POTATO, new ItemStack(ItemHandler.WILD_POTATO_COOKED), 0.5F);
//        GameRegistry.addSmelting(ItemHandler.GOAT_RAW, new ItemStack(ItemHandler.GOAT_COOKED), 0.5F);
//
//        addGrowthSerumRecipe(Items.COOKED_BEEF);
//        addGrowthSerumRecipe(Items.COOKED_CHICKEN);
//        addGrowthSerumRecipe(Items.COOKED_FISH);
//        addGrowthSerumRecipe(Items.COOKED_MUTTON);
//        addGrowthSerumRecipe(Items.COOKED_PORKCHOP);
//        addGrowthSerumRecipe(Items.COOKED_RABBIT);
//
//        for (Dinosaur dinosaur : EntityHandler.getRegisteredDinosaurs()) {
//            addGrowthSerumRecipe(new ItemStack(ItemHandler.DINOSAUR_STEAK, 1, EntityHandler.getDinosaurId(dinosaur)));
//        }
//
//        addPaleoBaleRecipe(BlockHandler.PALEO_BALE_CYCADEOIDEA);
//        addPaleoBaleRecipe(BlockHandler.PALEO_BALE_CYCAD);
//        addPaleoBaleRecipe(BlockHandler.PALEO_BALE_FERN);
//        addPaleoBaleRecipe(BlockHandler.PALEO_BALE_LEAVES);
//        addPaleoBaleRecipe(BlockHandler.PALEO_BALE_OTHER);
//
//        GameRegistry.addRecipe(new ItemStack(ItemHandler.ATTRACTION_SIGN), "III", "SSS", "III", 'I', Items.IRON_INGOT, 'S', Items.SIGN);
//
//        GameRegistry.addShapelessRecipe(new ItemStack(Items.DYE, 2, EnumDyeColor.YELLOW.getDyeDamage()), ItemHandler.RHAMNUS_BERRIES, ItemHandler.RHAMNUS_BERRIES, ItemHandler.RHAMNUS_BERRIES, ItemHandler.RHAMNUS_BERRIES);
//        GameRegistry.addShapelessRecipe(new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage()), ItemHandler.RHAMNUS_SEEDS, ItemHandler.RHAMNUS_SEEDS, ItemHandler.RHAMNUS_SEEDS, ItemHandler.RHAMNUS_SEEDS);
//
//        AttractionSignEntity.AttractionSignType[] types = AttractionSignEntity.AttractionSignType.values();
//
//        for (int i = 0; i < types.length; i++) {
//            int previous = i - 1;
//
//            if (previous < 0) {
//                previous = types.length - 1;
//            }
//
//            GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.ATTRACTION_SIGN, 1, i), new ItemStack(ItemHandler.ATTRACTION_SIGN, 1, previous));
//        }
      }
//
//    private static void addPaleoBaleRecipe(PaleoBaleBlock block) {
//        for (Item ingredient : block.getVariant().getIngredients()) {
//            GameRegistry.addRecipe(new ItemStack(block, Block.getBlockFromItem(ingredient) instanceof DoublePlantBlock ? 2 : 1), "###", "###", "###", '#', ingredient);
//        }
//    }
//
//    private static void addGrowthSerumRecipe(Item meat) {
//        addGrowthSerumRecipe(new ItemStack(meat));
//    }
//
//    private static void addGrowthSerumRecipe(ItemStack meat) {
//        GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.GROWTH_SERUM), Items.GOLDEN_CARROT, ItemHandler.EMPTY_SYRINGE, Items.WATER_BUCKET, meat);
//    }
}
