package org.jurassicraft.client.gui;

import lombok.RequiredArgsConstructor;
import net.minecraft.client.gui.GuiScreen;

import java.util.function.DoubleSupplier;

@RequiredArgsConstructor
public class GuiProgressBlock {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int texX;
    private final int texY;
    private final DoubleSupplier supplier;

    public void render(GuiScreen gui) {
        gui.drawTexturedModalRect(this.x, this.y, this.texX, this.texY, (int)(supplier.getAsDouble() * this.width), this.height);
    }
}
