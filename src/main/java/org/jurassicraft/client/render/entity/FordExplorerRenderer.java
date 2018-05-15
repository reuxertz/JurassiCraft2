package org.jurassicraft.client.render.entity;

import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.ResetControlTabulaModel;
import org.jurassicraft.server.entity.ai.util.MathUtils;
import org.jurassicraft.server.entity.vehicle.CarEntity;
import org.jurassicraft.server.entity.vehicle.FordExplorerEntity;
import org.jurassicraft.server.tabula.TabulaModelHelper;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FordExplorerRenderer extends Render<FordExplorerEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(JurassiCraft.MODID, "textures/entities/ford_explorer/ford_explorer.png");
    protected static final ResourceLocation[] DESTROY_STAGES = new ResourceLocation[] { new ResourceLocation("textures/blocks/destroy_stage_0.png"), new ResourceLocation("textures/blocks/destroy_stage_1.png"), new ResourceLocation("textures/blocks/destroy_stage_2.png"), new ResourceLocation("textures/blocks/destroy_stage_3.png"), new ResourceLocation("textures/blocks/destroy_stage_4.png"), new ResourceLocation("textures/blocks/destroy_stage_5.png"), new ResourceLocation("textures/blocks/destroy_stage_6.png"), new ResourceLocation("textures/blocks/destroy_stage_7.png"), new ResourceLocation("textures/blocks/destroy_stage_8.png"), new ResourceLocation("textures/blocks/destroy_stage_9.png") };

    private TabulaModel baseModel;

    public FordExplorerRenderer(RenderManager manager) {
        super(manager);

        try {
            TabulaModelContainer container = TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/entities/ford_explorer/ford_explorer.tbl");

            this.baseModel = new ResetControlTabulaModel(container);
        } catch (Exception e) {
            JurassiCraft.INSTANCE.getLogger().fatal("Failed to load the models for the Ford Explorer", e);
        }
    }

    @Override
    public void doRender(FordExplorerEntity entity, double x, double y, double z, float yaw, float partialTicks) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.bindEntityTexture(entity);
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        this.renderModel(entity, x, y, z, yaw, pitch, partialTicks);
        int destroyStage = Math.min(10, (int) (10.0F - (entity.getHealth() / CarEntity.MAX_HEALTH) * 10.0F)) - 1;
        if (destroyStage >= 0) {
            GlStateManager.color(1, 1, 1, 0.5F);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.doPolygonOffset(-3, -3);
            GlStateManager.enablePolygonOffset();
            RenderHelper.disableStandardItemLighting();
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            this.renderModel(entity, x, y, z, yaw, pitch, partialTicks);
            GlStateManager.doPolygonOffset(0, 0);
            GlStateManager.disablePolygonOffset();
            RenderHelper.enableStandardItemLighting();
        }
        GlStateManager.disableBlend();
        super.doRender(entity, x, y, z, yaw, partialTicks);
    }

    private void renderModel(FordExplorerEntity entity, double x, double y, double z, float yaw, float pitch, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 1.25F, (float) z);
        
        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
        float f3 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks; //TODO: interp ?

        GlStateManager.rotate(180 - yaw, 0, 1, 0);
        CarRenderer.doCarRotations(entity, partialTicks);
       
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        this.baseModel.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(FordExplorerEntity entity) {
        return TEXTURE;
    }
}
