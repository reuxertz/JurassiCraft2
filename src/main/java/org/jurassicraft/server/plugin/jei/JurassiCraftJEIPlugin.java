package org.jurassicraft.server.plugin.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.jurassicraft.client.gui.*;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.tree.AncientDoorBlock;
import org.jurassicraft.server.container.*;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.plant.PlantHandler;
import org.jurassicraft.server.plugin.jei.category.calcification.CalcificationInput;
import org.jurassicraft.server.plugin.jei.category.calcification.CalcificationRecipeCategory;
import org.jurassicraft.server.plugin.jei.category.calcification.CalcificationRecipeWrapper;
import org.jurassicraft.server.plugin.jei.category.cleaningstation.BoneInput;
import org.jurassicraft.server.plugin.jei.category.cleaningstation.CleaningStationRecipeCategory;
import org.jurassicraft.server.plugin.jei.category.cleaningstation.CleaningStationRecipeWrapper;
import org.jurassicraft.server.plugin.jei.category.dnasynthesizer.DNASynthesizerRecipeCategory;
import org.jurassicraft.server.plugin.jei.category.dnasynthesizer.DNASynthesizerRecipeWrapper;
import org.jurassicraft.server.plugin.jei.category.dnasynthesizer.SynthesizerInput;
import org.jurassicraft.server.plugin.jei.category.embroyonicmachine.EmbryoInput;
import org.jurassicraft.server.plugin.jei.category.embroyonicmachine.EmbryonicRecipeCategory;
import org.jurassicraft.server.plugin.jei.category.embroyonicmachine.EmbryonicRecipeWrapper;
import org.jurassicraft.server.plugin.jei.category.fossilgrinder.*;
import org.jurassicraft.server.plugin.jei.category.skeletonassembly.SkeletonAssemblyRecipeCategory;
import org.jurassicraft.server.plugin.jei.category.skeletonassembly.SkeletonAssemblyRecipeWrapper;
import org.jurassicraft.server.plugin.jei.category.skeletonassembly.SkeletonInput;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@JEIPlugin
public class JurassiCraftJEIPlugin implements IModPlugin {

    public static final String FOSSIL_GRINDER = "jurassicraft.fossil_grinder";
    public static final String CLEANING_STATION = "jurassicraft.cleaning_station";
    public static final String DNA_SYNTHASIZER = "jurassicraft.dna_synthesizer";
    public static final String EMBRYOMIC_MACHINE = "jurassicraft.embryonic_machine";
    public static final String EMBRYO_CALCIFICATION_MACHINE = "jurassicraft.embryo_calcification_machine";
    public static final String SKELETON_ASSEMBLY = "jurassicraft.skeleton_assembly";

    @Override
    public void register(IModRegistry registry) {
        IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();

        Collection<AncientDoorBlock> doors = BlockHandler.ANCIENT_DOORS.values();
        for (Block door : doors) {
            blacklist.addIngredientToBlacklist(new ItemStack(door));
        }

        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.CULTIVATOR_TOP, 1, OreDictionary.WILDCARD_VALUE));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.DISPLAY_BLOCK));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.KRILL_SWARM));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.PLANKTON_SWARM));
//        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.TOUR_RAIL_POWERED)); TODO
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.RHAMNUS_SALICIFOLIUS_PLANT));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.AJUGINUCULA_SMITHII));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.WILD_ONION));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.WILD_POTATO_PLANT));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.GRACILARIA));
        blacklist.addIngredientToBlacklist(new ItemStack(ItemHandler.HATCHED_EGG));

        //register recipe hander stuff
        registry.handleRecipes(GrinderInput.class, FossilGrinderRecipeWrapper::new, FOSSIL_GRINDER);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.FOSSIL_GRINDER), FOSSIL_GRINDER);

        registry.handleRecipes(BoneInput.class, CleaningStationRecipeWrapper::new, CLEANING_STATION);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.CLEANING_STATION), CLEANING_STATION);

        registry.handleRecipes(SynthesizerInput.class, DNASynthesizerRecipeWrapper::new, DNA_SYNTHASIZER);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.DNA_SYNTHESIZER), DNA_SYNTHASIZER);

        registry.handleRecipes(EmbryoInput.class, EmbryonicRecipeWrapper::new, EMBRYOMIC_MACHINE);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.EMBRYONIC_MACHINE), EMBRYOMIC_MACHINE);

        registry.handleRecipes(CalcificationInput.class, CalcificationRecipeWrapper::new, EMBRYO_CALCIFICATION_MACHINE);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.EMBRYO_CALCIFICATION_MACHINE), EMBRYO_CALCIFICATION_MACHINE);

        registry.handleRecipes(SkeletonInput.class, SkeletonAssemblyRecipeWrapper::new, SKELETON_ASSEMBLY);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.SKELETON_ASSEMBLY), SKELETON_ASSEMBLY);

        registry.addRecipeClickArea(FossilGrinderGui.class, 78, 33, 26, 19, FOSSIL_GRINDER);
        registry.addRecipeClickArea(CleaningStationGui.class, 78, 33, 26, 19, CLEANING_STATION);
        registry.addRecipeClickArea(DNASynthesizerGui.class, 78, 33, 26, 19, DNA_SYNTHASIZER);
        registry.addRecipeClickArea(EmbryonicMachineGui.class, 78, 33, 26, 19, EMBRYOMIC_MACHINE);
        registry.addRecipeClickArea(EmbryoCalcificationMachineGui.class, 66, 30, 26, 19, EMBRYO_CALCIFICATION_MACHINE);
        registry.addRecipeClickArea(SkeletonAssemblyGui.class, 106, 50, 26, 19, SKELETON_ASSEMBLY);

        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

        recipeTransferRegistry.addRecipeTransferHandler(FossilGrinderContainer.class, FOSSIL_GRINDER, 0, 6, 12, 36);
        recipeTransferRegistry.addRecipeTransferHandler(CleaningStationContainer.class, CLEANING_STATION, 0, 2, 8, 36);
        recipeTransferRegistry.addRecipeTransferHandler(DNASynthesizerContainer.class, DNA_SYNTHASIZER, 0, 3, 7, 36);
        recipeTransferRegistry.addRecipeTransferHandler(EmbryonicMachineContainer.class, EMBRYOMIC_MACHINE, 0, 3, 7, 36);
        recipeTransferRegistry.addRecipeTransferHandler(EmbryoCalcificationMachineContainer.class, EMBRYO_CALCIFICATION_MACHINE, 0, 2, 3, 36);
        recipeTransferRegistry.addRecipeTransferHandler(SkeletonAssemblyContainer.class, SKELETON_ASSEMBLY, 1, 25, 26, 36);

        //register recipes

        registry.addRecipes(getDinos(GrinderInput::new), FOSSIL_GRINDER);
        registry.addRecipes(getDinos(CalcificationInput::new), EMBRYO_CALCIFICATION_MACHINE);

        registry.addRecipes(getDinos(SynthesizerInput.DinosaurInput::new), DNA_SYNTHASIZER);
        registry.addRecipes(getPlants(SynthesizerInput.PlantInput::new), DNA_SYNTHASIZER);

        registry.addRecipes(getDinos(EmbryoInput.DinosaurInput::new), EMBRYOMIC_MACHINE);
        registry.addRecipes(getPlants(EmbryoInput.PlantInput::new), EMBRYOMIC_MACHINE);

        for (Dinosaur dinosaur : EntityHandler.getRegisteredDinosaurs()) {
            for (String bone : dinosaur.getBones()) {
                registry.addRecipes(Lists.newArrayList(new BoneInput(dinosaur, bone)), CLEANING_STATION);
            }
            registry.addRecipes(Lists.newArrayList(new SkeletonInput(dinosaur, false), new SkeletonInput(dinosaur, true)), SKELETON_ASSEMBLY);
        }
    }

    private <T> List<T> getDinos(Function<Dinosaur,T> func) {
        return EntityHandler.getRegisteredDinosaurs().stream().map(func).collect(Collectors.toList());
    }

    private <T> List<T> getPlants(Function<Plant,T> func) {
        return PlantHandler.getPrehistoricPlantsAndTrees().stream().map(func).collect(Collectors.toList());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

        registry.addRecipeCategories(
        	new FossilGrinderRecipeCategory(guiHelper),
        	new CleaningStationRecipeCategory(guiHelper),
        	new DNASynthesizerRecipeCategory(guiHelper),
        	new EmbryonicRecipeCategory(guiHelper),
        	new CalcificationRecipeCategory(guiHelper),
        	new SkeletonAssemblyRecipeCategory(guiHelper)

        );
    }
}
