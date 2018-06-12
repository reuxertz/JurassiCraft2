package org.jurassicraft.server.plugin.jei.category.incubator;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.item.ItemHandler;

public class IncubatorRecipeWrapper extends BlankRecipeWrapper  {

    private final Dinosaur dinosaur;

    public IncubatorRecipeWrapper(IncubatorInput input) {
        this.dinosaur = input.getDinosaur();
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        int meta = EntityHandler.getDinosaurId(this.dinosaur);
        ingredients.setInput(ItemStack.class, new ItemStack(ItemHandler.EGG, 1, meta));
        ingredients.setOutput(ItemStack.class, new ItemStack(ItemHandler.HATCHED_EGG, 1, meta));
    }

    public Dinosaur getDinosaur() {
        return dinosaur;
    }
}
