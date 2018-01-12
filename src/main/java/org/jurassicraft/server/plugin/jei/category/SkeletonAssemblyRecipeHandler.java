package org.jurassicraft.server.plugin.jei.category;

import org.jurassicraft.server.plugin.jei.category.ingredient.SkeletonInput;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class SkeletonAssemblyRecipeHandler implements IRecipeHandler<SkeletonInput> {
    @Override
    public Class<SkeletonInput> getRecipeClass() {
        return SkeletonInput.class;
    }

    @Override
    public String getRecipeCategoryUid(SkeletonInput recipe) {
    	return "jurassicraft.skeleton_assembly";
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(SkeletonInput recipe) {
        return new SkeletonAssemblyRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(SkeletonInput recipe) {
        return recipe.dinosaur.shouldRegister();
    }
}
