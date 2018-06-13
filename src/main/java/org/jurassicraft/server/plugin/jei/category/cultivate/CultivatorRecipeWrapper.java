package org.jurassicraft.server.plugin.jei.category.cultivate;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.item.ItemHandler;

public class CultivatorRecipeWrapper implements IRecipeWrapper {
    private final Dinosaur dinosaur;

    public CultivatorRecipeWrapper(CultivateInput input) {
        this.dinosaur = input.dino;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, ItemHandler.SYRINGE.getItemStack(this.dinosaur));
        ingredients.setOutput(ItemStack.class, ItemHandler.HATCHED_EGG.getItemStack(this.dinosaur));
    }

    public Dinosaur getDinosaur() {
        return dinosaur;
    }
}
