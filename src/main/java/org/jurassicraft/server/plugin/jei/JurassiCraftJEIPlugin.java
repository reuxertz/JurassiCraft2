package org.jurassicraft.server.plugin.jei;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.tree.AncientDoorBlock;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.plant.PlantHandler;
import org.jurassicraft.server.plugin.jei.category.CalcificationRecipeCategory;
import org.jurassicraft.server.plugin.jei.category.CalcificationRecipeWrapper;
import org.jurassicraft.server.plugin.jei.category.CleaningStationRecipeCategory;
import org.jurassicraft.server.plugin.jei.category.CleaningStationRecipeWrapper;
import org.jurassicraft.server.plugin.jei.category.DNASynthesizerRecipeCategory;
import org.jurassicraft.server.plugin.jei.category.DNASynthesizerRecipeWrapper;
import org.jurassicraft.server.plugin.jei.category.EmbryonicRecipeCategory;
import org.jurassicraft.server.plugin.jei.category.EmbryonicRecipeWrapper;
import org.jurassicraft.server.plugin.jei.category.FossilGrinderRecipeCategory;
import org.jurassicraft.server.plugin.jei.category.FossilGrinderRecipeWrapper;
import org.jurassicraft.server.plugin.jei.category.SkeletonAssemblyRecipeCategory;
import org.jurassicraft.server.plugin.jei.category.SkeletonAssemblyRecipeWrapper;
import org.jurassicraft.server.plugin.jei.category.ingredient.BoneInput;
import org.jurassicraft.server.plugin.jei.category.ingredient.CalcificationInput;
import org.jurassicraft.server.plugin.jei.category.ingredient.EmbryoInput;
import org.jurassicraft.server.plugin.jei.category.ingredient.GrinderInput;
import org.jurassicraft.server.plugin.jei.category.ingredient.SkeletonInput;
import org.jurassicraft.server.plugin.jei.category.ingredient.SynthesizerInput;

import com.google.common.collect.Lists;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

@JEIPlugin
public class JurassiCraftJEIPlugin extends BlankModPlugin {
    
    public static final String GRINDER_RECIPE = "jurassicraft.fossil_grinder";
    public static final String CLEANING_RECIPE = "jurassicraft.cleaning_station";
    public static final String DNA_SYNTHASIZER = "jurassicraft.dna_synthesizer";
    public static final String EMBRYOMIC_MACHINE = "jurassicraft.embryonic_machine";
    public static final String EMBRO_CALCIFICATION_MACHINE = "jurassicraft.embryo_calcification_machine";
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
        registry.handleRecipes(GrinderInput.class, FossilGrinderRecipeWrapper::new, GRINDER_RECIPE);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.FOSSIL_GRINDER), GRINDER_RECIPE);

        registry.handleRecipes(BoneInput.class, CleaningStationRecipeWrapper::new, CLEANING_RECIPE);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.CLEANING_STATION), CLEANING_RECIPE);
        
        registry.handleRecipes(SynthesizerInput.class, DNASynthesizerRecipeWrapper::new, DNA_SYNTHASIZER);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.DNA_SYNTHESIZER), DNA_SYNTHASIZER);
        
        registry.handleRecipes(EmbryoInput.class, EmbryonicRecipeWrapper::new, EMBRYOMIC_MACHINE);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.EMBRYONIC_MACHINE), EMBRYOMIC_MACHINE);
        
        registry.handleRecipes(CalcificationInput.class, CalcificationRecipeWrapper::new, EMBRO_CALCIFICATION_MACHINE);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.EMBRYO_CALCIFICATION_MACHINE), EMBRO_CALCIFICATION_MACHINE);
        
        registry.handleRecipes(SkeletonInput.class, SkeletonAssemblyRecipeWrapper::new, SKELETON_ASSEMBLY);
        registry.addRecipeCatalyst(new ItemStack(BlockHandler.SKELETON_ASSEMBLY), SKELETON_ASSEMBLY);
        
        //register recipes
        
        List<Dinosaur> registeredDinosaurs = EntityHandler.getRegisteredDinosaurs();
        List<Plant> registeredPlants = PlantHandler.getPrehistoricPlantsAndTrees();
        
        
        registry.addRecipes(registeredDinosaurs.stream().map(GrinderInput::new).collect(Collectors.toList()), GRINDER_RECIPE);
        registry.addRecipes(registeredDinosaurs.stream().map(CalcificationInput::new).collect(Collectors.toList()), EMBRO_CALCIFICATION_MACHINE);

        registry.addRecipes(registeredDinosaurs.stream().map(SynthesizerInput.DinosaurInput::new).collect(Collectors.toList()), DNA_SYNTHASIZER);
        registry.addRecipes(registeredPlants.stream().map(SynthesizerInput.PlantInput::new).collect(Collectors.toList()), DNA_SYNTHASIZER);

        registry.addRecipes(registeredDinosaurs.stream().map(EmbryoInput.DinosaurInput::new).collect(Collectors.toList()), EMBRYOMIC_MACHINE);
        registry.addRecipes(registeredPlants.stream().map(EmbryoInput.PlantInput::new).collect(Collectors.toList()), EMBRYOMIC_MACHINE);

        for (Dinosaur dinosaur : registeredDinosaurs) {
            for (String bone : dinosaur.getBones()) {
                registry.addRecipes(Lists.newArrayList(new BoneInput(dinosaur, bone)), CLEANING_RECIPE);
            }
            registry.addRecipes(Lists.newArrayList(new SkeletonInput(dinosaur, false), new SkeletonInput(dinosaur, true)), SKELETON_ASSEMBLY);
        }
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
