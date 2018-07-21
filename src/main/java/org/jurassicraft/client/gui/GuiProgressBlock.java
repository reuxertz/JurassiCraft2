package org.jurassicraft.client.gui;

import net.minecraft.client.gui.GuiScreen;

import java.util.function.DoubleSupplier;

public class GuiProgressBlock {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int texX;
    private final int texY;
    private final DoubleSupplier supplier;

    public GuiProgressBlock(int x, int y, int width, int height, int texX, int texY, DoubleSupplier supplier) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texX = texX;
        this.texY = texY;
        this.supplier = supplier;
    }

    public void render(GuiScreen gui) {
        gui.drawTexturedModalRect(this.x, this.y, this.texX, this.texY, (int)(supplier.getAsDouble() * this.width), this.height);
    }
}
