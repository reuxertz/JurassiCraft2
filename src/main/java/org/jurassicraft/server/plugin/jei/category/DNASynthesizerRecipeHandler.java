package org.jurassicraft.server.plugin.jei.category;

import org.jurassicraft.server.plugin.jei.category.ingredient.SynthesizerInput;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class DNASynthesizerRecipeHandler implements IRecipeHandler<SynthesizerInput> {
    @Override
    public Class<SynthesizerInput> getRecipeClass() {
        return SynthesizerInput.class;
    }
    
    @Override
    public String getRecipeCategoryUid(SynthesizerInput recipe) {
        return "jurassicraft.dna_synthesizer";
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(SynthesizerInput recipe) {
        return new DNASynthesizerRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(SynthesizerInput recipe) {
        return recipe.isValid();
    }
}
