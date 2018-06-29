package org.jurassicraft.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jurassicraft.server.item.TrackingTablet;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class TrackingTabletGui extends GuiScreen {

    private final Minecraft mc = Minecraft.getMinecraft();

    private final ItemStack stack;//TODO: do i even need this?
    private final List<RenderDinosaurInfo> renderList = Lists.newArrayList();
    private ResourceLocation location;

    private Point lastMouseClicked = new Point(0, 0);
    private Point currentOffset = new Point(0, 0);
    private float zoomedScale = 1f;

    public TrackingTabletGui(ItemStack stack) {
        this.stack = stack;
        TrackingTablet.TrackingInfo info = new TrackingTablet.TrackingInfo(this.stack);
        this.location = mc.getTextureManager().getDynamicTextureLocation("tracking_tablet", info.createDynamicTexture(mc.player));
        refreshDinosaurs();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void refreshDinosaurs() {
        this.renderList.clear();
        TrackingTablet.TrackingInfo info = new TrackingTablet.TrackingInfo(mc.player.getHeldItem(EnumHand.MAIN_HAND));
        for (TrackingTablet.DinosaurInfo dinosaurInfo : info.getDinosaurInfos()) {
            renderList.add(new RenderDinosaurInfo(mc.player, dinosaurInfo));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        refreshDinosaurs();
        this.drawDefaultBackground();
        mc.getTextureManager().bindTexture(this.location);
        GlStateManager.disableAlpha();
        int i = (int)(256 * zoomedScale);
        int xOffset = -currentOffset.x;
        int zOffset = -currentOffset.y;
        Gui.drawModalRectWithCustomSizedTexture((int)(this.width / 2 + xOffset - 128 * zoomedScale), (int)(this.height / 2 + zOffset - 128 * zoomedScale), 0, 0, i, i, i, i);
        GlStateManager.enableAlpha();
        for (RenderDinosaurInfo r : renderList) {
            ResourceLocation location = r.info.getDinosaur().getRegistryName();
            mc.getTextureManager().bindTexture(new ResourceLocation(location.getResourceDomain(), "textures/gui/mapicons/" + location.getResourcePath() + ".png"));
            Gui.drawModalRectWithCustomSizedTexture((int)(this.width / 2 + xOffset + r.x * zoomedScale) - 4, (int)(this.height / 2 + zOffset + r.z * zoomedScale) - 4, 0, 0, 8, 8, 8, 8);
        }

        int relX = mouseX - width / 2;
        int relY = mouseY - height / 2;
        for (RenderDinosaurInfo render : this.renderList) {
            if(Math.abs(relX - xOffset - render.x * zoomedScale + 4) <= 10 && Math.abs(relY - zOffset - render.z * zoomedScale + 4) <= 10) {
                List<String> lines = Lists.newArrayList();
                TrackingTablet.DinosaurInfo info = render.info;
                ResourceLocation regName = info.getDinosaur().getRegistryName();
                lines.add("Dinosaur: " + I18n.format("entity." + regName.getResourceDomain() + "." + regName.getResourcePath() + ".name"));
                lines.add("Gender: " + I18n.format("gender." + (info.isMale() ? "male" : "female") + ".name"));
                BlockPos pos = info.getPos();
                lines.add("At: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
                lines.add("Days existed: " + Math.floor((info.getGrowthPercentage() * 8.0F) / 24000.0F));
                this.drawHoveringText(lines, mouseX, mouseY);
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(mouseButton == 0) {
            this.lastMouseClicked = new Point(mouseX, mouseY);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if(clickedMouseButton == 0) {
            currentOffset = new Point(currentOffset.x + lastMouseClicked.x - mouseX, currentOffset.y + lastMouseClicked.y - mouseY);
            this.lastMouseClicked = new Point(mouseX, mouseY);
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getDWheel();
        if(wheel > 0) {
            this.zoomedScale += 0.1F;
        } else if(wheel < 0) {
            this.zoomedScale -= 0.1F;
        }
        this.zoomedScale = MathHelper.clamp(this.zoomedScale, 0.1f, 3f);
    }

    public static class RenderDinosaurInfo {
        private final TrackingTablet.DinosaurInfo info;
        private final int x;
        private final int z;

        public RenderDinosaurInfo(EntityPlayer player, TrackingTablet.DinosaurInfo info) {
            this.info = info;
            this.x = (info.getPos().getX() - player.getPosition().getX()) / (TrackingTablet.DISTANCE / 128);
            this.z = (info.getPos().getZ() - player.getPosition().getZ()) / (TrackingTablet.DISTANCE / 128);
        }
    }
}
