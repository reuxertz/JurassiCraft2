package org.jurassicraft.server.plugin.jei.category.fossilgrinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.plugin.jei.category.fossilgrinder.GrinderInput;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class FossilGrinderRecipeWrapper implements IRecipeWrapper {
    private final Dinosaur dinosaur;

    public FossilGrinderRecipeWrapper(GrinderInput input) {
        this.dinosaur = input.dinosaur;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        String[] bones = this.dinosaur.getBones();
        int metadata = EntityHandler.getDinosaurId(this.dinosaur);
        List<ItemStack> fossilVariants = new ArrayList<>(bones.length);
        for (String bone : bones) {
            fossilVariants.add(new ItemStack(ItemHandler.FOSSILS.get(bone), 1, metadata));
        }
        List<List<ItemStack>> inputs = new ArrayList<>();
        inputs.add(fossilVariants);
        ingredients.setInputLists(ItemStack.class, inputs);
        ItemStack output = new ItemStack(ItemHandler.SOFT_TISSUE, 1, metadata);
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
