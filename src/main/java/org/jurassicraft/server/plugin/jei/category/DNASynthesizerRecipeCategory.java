package org.jurassicraft.server.plugin.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.item.ItemHandler;

import java.util.List;

public class DNASynthesizerRecipeCategory extends BlankRecipeCategory<IRecipeWrapper> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(JurassiCraft.MODID, "textures/gui/dna_synthesizer.png");

    private final IDrawable background;
    private final String title;

    private final IDrawableAnimated arrow;

    public DNASynthesizerRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 22, 21, 132, 45);
        this.title = I18n.translateToLocal("tile.dna_synthesizer.name");

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(TEXTURE, 176, 14, 24, 16);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        this.arrow.draw(minecraft, 57, 14);
    }

    @Override
    public String getUid() {
        return "jurassicraft.dna_synthesizer";
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);
        stackGroup.init(0, true, 15, 0);
        stackGroup.set(0, inputs.get(0));
        stackGroup.init(1, true, 1, 27);
        stackGroup.set(1, new ItemStack(ItemHandler.EMPTY_TEST_TUBE));
        stackGroup.init(2, true, 27, 27);
        stackGroup.set(2, new ItemStack(ItemHandler.DNA_NUCLEOTIDES));
        stackGroup.init(3, false, 96, 4);
        stackGroup.set(3, outputs.get(0));
    }

	@Override
	public String getModName() {
		return JurassiCraft.NAME;
	}
}
