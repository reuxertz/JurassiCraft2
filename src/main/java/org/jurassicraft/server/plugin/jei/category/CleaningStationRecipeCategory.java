package org.jurassicraft.server.plugin.jei.category;

import java.util.List;

import org.jurassicraft.JurassiCraft;

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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class CleaningStationRecipeCategory extends BlankRecipeCategory<IRecipeWrapper> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(JurassiCraft.MODID, "textures/gui/cleaning_station.png");

    private final IDrawable background;
    private final String title;

    private final IDrawableAnimated arrow;
    private final IDrawableAnimated water;

    public CleaningStationRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 46, 16, 115, 54);
        this.title = I18n.translateToLocal("tile.cleaning_station.name");

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(TEXTURE, 176, 14, 24, 16);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

        IDrawableStatic waterDrawable = guiHelper.createDrawable(TEXTURE, 176, 31, 6, 51);
        this.water = guiHelper.createAnimatedDrawable(waterDrawable, 400, IDrawableAnimated.StartDirection.TOP, true);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        GlStateManager.enableBlend();
        this.arrow.draw(minecraft, 33, 18);
        this.water.draw(minecraft, 0, 2);
    }

    @Override
    public String getUid() {
        return "jurassicraft.cleaning_station";
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
        stackGroup.init(0, true, 9, 36);
        stackGroup.set(0, new ItemStack(Items.WATER_BUCKET));
        stackGroup.init(1, true, 9, 0);
        stackGroup.set(1, inputs.get(0));
        for (int row = 0; row < 2; row++) {
            for (int column = 0; column < 3; column++) {
                int index = column + row * 2;
                stackGroup.init(index + 2, false, 61 + column * 18, 9 + row * 18);
                if (index < outputs.size()) {
                    stackGroup.set(index + 2, outputs.get(index));
                }
            }
        }
    }

	@Override
	public String getModName() {
		return JurassiCraft.NAME;
	}
}
