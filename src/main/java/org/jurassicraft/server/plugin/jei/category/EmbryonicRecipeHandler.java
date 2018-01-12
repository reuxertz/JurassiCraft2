package org.jurassicraft.server.plugin.jei.category;

import org.jurassicraft.server.plugin.jei.category.ingredient.EmbryoInput;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class EmbryonicRecipeHandler implements IRecipeHandler<EmbryoInput> {
    @Override
    public Class<EmbryoInput> getRecipeClass() {
        return EmbryoInput.class;
    }

    @Override
    public String getRecipeCategoryUid(EmbryoInput recipe) {
    	return "jurassicraft.embryonic_machine";
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(EmbryoInput recipe) {
        return new EmbryonicRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(EmbryoInput recipe) {
        return recipe.isValid();
    }
}
