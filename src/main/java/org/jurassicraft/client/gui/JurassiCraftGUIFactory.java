package org.jurassicraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

@SideOnly(Side.CLIENT)
public class JurassiCraftGUIFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft mc) {
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

	@Override
	public boolean hasConfigGui() {
		return false;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return null;
	}
}
