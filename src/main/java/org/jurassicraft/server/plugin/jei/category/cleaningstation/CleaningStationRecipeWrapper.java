package org.jurassicraft.server.plugin.jei.category.cleaningstation;

import java.util.Collections;
import java.util.List;

import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.item.ItemHandler;

import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class CleaningStationRecipeWrapper implements IRecipeWrapper {
    private final Dinosaur dinosaur;
    private final String bone;

    public CleaningStationRecipeWrapper(BoneInput input) {
        this.dinosaur = input.getDinosaur();
        this.bone = input.getBone();
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        int metadata = EntityHandler.getDinosaurId(this.dinosaur);
        ingredients.setInputs(ItemStack.class, Lists.newArrayList(new ItemStack(BlockHandler.getEncasedFossil(metadata), 1, BlockHandler.getMetadata(metadata))));
        ItemStack output = new ItemStack(ItemHandler.FOSSILS.get(this.bone), 1, metadata);
        ingredients.setOutput(ItemStack.class, output);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return Collections.emptyList();
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }
}
