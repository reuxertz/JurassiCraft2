package org.jurassicraft.server.plugin.jei.category.incubator;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.item.ItemHandler;

public class IncubatorRecipeWrapper implements IRecipeWrapper {

    private final Dinosaur dinosaur;

    public IncubatorRecipeWrapper(IncubatorInput input) {
        this.dinosaur = input.getDinosaur();
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, ItemHandler.EGG.getItemStack(this.dinosaur));
        ingredients.setOutput(ItemStack.class, ItemHandler.HATCHED_EGG.getItemStack(this.dinosaur));
    }

    public Dinosaur getDinosaur() {
        return dinosaur;
    }
}