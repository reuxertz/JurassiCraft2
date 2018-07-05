package org.jurassicraft.client.gui;

import com.google.common.collect.Lists;
import com.sun.javafx.geom.Vec2d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.item.TrackingTablet;
import org.jurassicraft.server.message.StopMapSyncMessage;
import org.jurassicraft.server.util.TrackingMapIterator;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;

public class TrackingTabletGui extends GuiScreen {

    private final Minecraft mc = Minecraft.getMinecraft();

    private final DynamicTexture texture;
    private final EnumHand hand;
    private final List<RenderDinosaurInfo> renderList = Lists.newArrayList();
    private ResourceLocation location;

    private Vec2d lastMouseClicked = new Vec2d(0, 0);
    private Vec2d currentOffset = new Vec2d(0, 0);
    private float zoomedScale = 1f;

    private boolean startRecieved;
    private AnimatedTexture loadingTexture;

    public TrackingTabletGui(EnumHand hand, TrackingMapIterator mapIterator) {
        this.hand = hand;
        this.location = mc.getTextureManager().getDynamicTextureLocation("tracking_tablet", texture = new DynamicTexture(TrackingTablet.DISTANCE * 2, TrackingTablet.DISTANCE * 2));
        refreshDinosaurs();
    }

    @Override
    public void initGui() {
        this.loadingTexture = new AnimatedTexture(new ResourceLocation(JurassiCraft.MODID, "textures/gui/map_loading.png"), this.width / 2 - 50, this.height / 2 - 50, 100, 100, 100D);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void refreshDinosaurs() {
        this.renderList.clear();
        TrackingTablet.TrackingInfo info = new TrackingTablet.TrackingInfo(mc.player.getHeldItem(hand));
        for (TrackingTablet.DinosaurInfo dinosaurInfo : info.getDinosaurInfos()) {
            renderList.add(new RenderDinosaurInfo(mc.player, dinosaurInfo));
        }
    }

    @Override
    public void onGuiClosed() {
        mc.getTextureManager().deleteTexture(this.location);
        JurassiCraft.NETWORK_WRAPPER.sendToServer(new StopMapSyncMessage());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        refreshDinosaurs();
        this.drawDefaultBackground();
        if(startRecieved) {
            mc.getTextureManager().bindTexture(this.location);
            int i = (int)(256 * this.zoomedScale);
            double xOffset = -currentOffset.x;
            double zOffset = -currentOffset.y;

            double left = this.width / 2 + (xOffset - 128) * this.zoomedScale;
            double top = this.height / 2 + (zOffset - 128) * this.zoomedScale;

            GlStateManager.enableBlend();
            drawModalRectWithCustomSizedTexture(left, top, 0, 0, i, i, i, i);
            GlStateManager.disableBlend();
            drawBorder(left, top, left + i, top + i, 0xFF000000, 2F * this.zoomedScale);
            for (RenderDinosaurInfo r : renderList) {
                ResourceLocation location = r.info.getDinosaur().getRegistryName();
                mc.getTextureManager().bindTexture(new ResourceLocation(location.getResourceDomain(), "textures/gui/mapicons/" + location.getResourcePath() + ".png"));
                drawModalRectWithCustomSizedTexture(this.width / 2 + (xOffset + r.x) * this.zoomedScale - 4, this.height / 2 + (zOffset + r.z) * this.zoomedScale - 4, 0, 0, 8, 8, 8, 8);
            }

            int relX = mouseX - width / 2;
            int relY = mouseY - height / 2;
            RenderDinosaurInfo closest = null;
            for (RenderDinosaurInfo render : this.renderList) {
                if(Math.abs(relX - xOffset * this.zoomedScale - render.x * this.zoomedScale + 4) <= 10 && Math.abs(relY - zOffset * this.zoomedScale - render.z * this.zoomedScale + 4) <= 10) {
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
        } else {
            this.loadingTexture.render();
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
            currentOffset = new Vec2d(currentOffset.x + (lastMouseClicked.x - mouseX) / this.zoomedScale, currentOffset.y + (lastMouseClicked.y - mouseY) / this.zoomedScale);
            this.lastMouseClicked = new Vec2d(mouseX, mouseY);
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getDWheel();
        if(wheel > 0) {
            this.zoomedScale += 0.1F * this.zoomedScale;
        } else if(wheel < 0) {
            this.zoomedScale -= 0.1F * this.zoomedScale;
        }
    }

    public void upload(List<Integer> list, int offset) {
        for (int i = 0; i < list.size(); i++) {
            int c = list.get(i);
            this.texture.getTextureData()[offset + i] = c;
        }
        int length = TrackingTablet.DISTANCE * 2;
        int height = Math.min(Math.floorDiv(list.size(), length) + 1, length);
        int area = length * height;
        int aint[] = new int[area];
        for(int i = 0; i < area; i++) {
            int pos = i + Math.floorDiv(offset, length) * length;
            if(pos < this.texture.getTextureData().length) { //TODO: remove?
                aint[i] = this.texture.getTextureData()[pos];
            }
        }
        GlStateManager.bindTexture(this.texture.getGlTextureId());
        TextureUtil.uploadTextureMipmap(new int[][]{aint}, length, height, 0, Math.floorDiv(offset, length), false, false);
        this.startRecieved = true;
    }

    public static void drawBorder(double left, double top, double right, double bottom, int color, double thickness) {
        thickness /= 2;
        drawRect(left - thickness, top - thickness, right + thickness, top + thickness, color);
        drawRect(left - thickness, bottom - thickness, right + thickness, bottom + thickness, color);
        drawRect(left - thickness, top + thickness, left + thickness, bottom - thickness, color);
        drawRect(right + thickness, top + thickness, right - thickness, bottom - thickness, color);
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, top, 0.0D).endVertex();
        bufferbuilder.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
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
