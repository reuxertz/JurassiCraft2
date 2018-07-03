package org.jurassicraft.client.gui;

import com.google.common.collect.Lists;
import com.sun.javafx.geom.Vec2d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.item.TrackingTablet;
import org.jurassicraft.server.message.StopMapSyncMessage;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class TrackingTabletGui extends GuiScreen {

    private final Minecraft mc = Minecraft.getMinecraft();

    private final ItemStack stack;//TODO: do i even need this?
    private final DynamicTexture texture;
    private final List<RenderDinosaurInfo> renderList = Lists.newArrayList();
    private ResourceLocation location;

    private Vec2d lastMouseClicked = new Vec2d(0, 0);
    private Vec2d currentOffset = new Vec2d(0, 0);
    private float zoomedScale = 1f;

    public TrackingTabletGui(ItemStack stack) {
        this.stack = stack;
        TrackingTablet.TrackingInfo info = new TrackingTablet.TrackingInfo(this.stack);
        this.location = mc.getTextureManager().getDynamicTextureLocation("tracking_tablet", texture = new DynamicTexture(TrackingTablet.DISTANCE * 2, TrackingTablet.DISTANCE * 2));
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
    public void onGuiClosed() {
        JurassiCraft.NETWORK_WRAPPER.sendToServer(new StopMapSyncMessage());
        mc.getTextureManager().deleteTexture(this.location);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        refreshDinosaurs();
        this.drawDefaultBackground();
        mc.getTextureManager().bindTexture(this.location);
        int i = (int)(256 * zoomedScale);
        double xOffset = -currentOffset.x;
        double zOffset = -currentOffset.y;
        drawModalRectWithCustomSizedTexture(this.width / 2 + (xOffset - 128) * zoomedScale, this.height / 2 + (zOffset - 128) * zoomedScale, 0, 0, i, i, i, i);
        for (RenderDinosaurInfo r : renderList) {
            ResourceLocation location = r.info.getDinosaur().getRegistryName();
            mc.getTextureManager().bindTexture(new ResourceLocation(location.getResourceDomain(), "textures/gui/mapicons/" + location.getResourcePath() + ".png"));
            drawModalRectWithCustomSizedTexture(this.width / 2 + (xOffset + r.x) * zoomedScale - 4, this.height / 2 + (zOffset + r.z) * zoomedScale - 4, 0, 0, 8, 8, 8, 8);
        }

        int relX = mouseX - width / 2;
        int relY = mouseY - height / 2;
        RenderDinosaurInfo closest = null;
        for (RenderDinosaurInfo render : this.renderList) {
            if(Math.abs(relX - xOffset * zoomedScale - render.x * zoomedScale + 4) <= 10 && Math.abs(relY - zOffset * zoomedScale - render.z * zoomedScale + 4) <= 10) {
                if(closest == null || Vec2d.distance(render.x, render.z, relX, relY) < Vec2d.distance(closest.x, closest.z, relX, relY)) {
                    closest = render;
                }
            }
        }
        if(closest != null) {
            List<String> lines = Lists.newArrayList();
            TrackingTablet.DinosaurInfo info = closest.info;
            ResourceLocation regName = info.getDinosaur().getRegistryName();
            lines.add("Dinosaur: " + I18n.format("entity." + regName.getResourceDomain() + "." + regName.getResourcePath() + ".name"));
            lines.add("Gender: " + I18n.format("gender." + (info.isMale() ? "male" : "female") + ".name"));
            BlockPos pos = info.getPos();
            lines.add("At: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
            lines.add("Days existed: " + Math.floor((info.getGrowthPercentage() * 8.0F) / 24000.0F));
            this.drawHoveringText(lines, mouseX, mouseY);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public static void drawModalRectWithCustomSizedTexture(double x, double y, float u, float v, int width, int height, float textureWidth, float textureHeight)
    {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0.0D).tex(u * f,(v + height) * f1).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0D).tex((u + width) * f,(v + height) * f1).endVertex();
        bufferbuilder.pos(x + width, y, 0.0D).tex((u + width) * f, v * f1).endVertex();
        bufferbuilder.pos(x, y, 0.0D).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(mouseButton == 0) {
            this.lastMouseClicked = new Vec2d(mouseX, mouseY);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if(clickedMouseButton == 0) {
            currentOffset = new Vec2d(currentOffset.x + (lastMouseClicked.x - mouseX) / zoomedScale, currentOffset.y + (lastMouseClicked.y - mouseY) / zoomedScale);
            this.lastMouseClicked = new Vec2d(mouseX, mouseY);
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getDWheel();
        if(wheel > 0) {
            this.zoomedScale += 0.1F * zoomedScale;
        } else if(wheel < 0) {
            this.zoomedScale -= 0.1F * zoomedScale;
        }
    }

    public void upload(int[] aint, int offset) {
        for (int i = 0; i < aint.length; i++) {
            int pos = offset + i;
            if(pos < TrackingTablet.DISTANCE * TrackingTablet.DISTANCE * 4) {
                this.texture.getTextureData()[pos] = aint[i];
            }
        }
        this.texture.updateDynamicTexture();
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
