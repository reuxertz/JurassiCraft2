package org.jurassicraft.client.render.entity;

import java.util.Map.Entry;

import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.ResetControlTabulaModel;
import org.jurassicraft.client.model.animation.entity.vehicle.JeepWranglerAnimator;
import org.jurassicraft.server.entity.ai.util.MathUtils;
import org.jurassicraft.server.entity.vehicle.CarEntity;
import org.jurassicraft.server.entity.vehicle.JeepWranglerEntity;
import org.jurassicraft.server.tabula.TabulaModelHelper;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class JeepWranglerRenderer extends Render<JeepWranglerEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(JurassiCraft.MODID, "textures/entities/jeep_wrangler/jeep_wrangler.png");

    private static final ResourceLocation[] DESTROY_STAGES = { new ResourceLocation("textures/blocks/destroy_stage_0.png"), new ResourceLocation("textures/blocks/destroy_stage_1.png"), new ResourceLocation("textures/blocks/destroy_stage_2.png"), new ResourceLocation("textures/blocks/destroy_stage_3.png"), new ResourceLocation("textures/blocks/destroy_stage_4.png"), new ResourceLocation("textures/blocks/destroy_stage_5.png"), new ResourceLocation("textures/blocks/destroy_stage_6.png"), new ResourceLocation("textures/blocks/destroy_stage_7.png"), new ResourceLocation("textures/blocks/destroy_stage_8.png"), new ResourceLocation("textures/blocks/destroy_stage_9.png") };

    private static final String WINDSCREEN = "Windscreen";

    private TabulaModel baseModel;

    private TabulaModel windscreen;

    private TabulaModel destroyModel;

    public JeepWranglerRenderer(RenderManager manager) {
        super(manager);
        try {
            JeepWranglerAnimator animator = new JeepWranglerAnimator();
            TabulaModelContainer container = TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/entities/jeep_wrangler/jeep_wrangler.tbl");
            this.baseModel = new ResetControlTabulaModel(container);
            this.baseModel.getCube(WINDSCREEN).showModel = false;
            this.windscreen = new TabulaModel(container);
            for (Entry<String, AdvancedModelRenderer> entry : this.windscreen.getCubes().entrySet()) {
                entry.getValue().showModel = entry.getKey().equals(WINDSCREEN);
            }
            this.destroyModel = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/entities/jeep_wrangler/jeep_wrangler_break.tbl"), animator);
        } catch (Exception e) {
            JurassiCraft.INSTANCE.getLogger().fatal("Failed to load the models for the Jeep Wrangler", e);
        }
    }

    @Override
    public void doRender(JeepWranglerEntity entity, double x, double y, double z, float yaw, float partialTicks) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
        this.bindEntityTexture(entity);
        this.renderModel(entity, x, y, z, yaw, partialTicks, false, false);
        this.renderModel(entity, x, y, z, yaw, partialTicks, true, false);
        int destroyStage = Math.min(10, (int) (10 - (entity.getHealth() / CarEntity.MAX_HEALTH) * 10)) - 1;
        if (destroyStage >= 0) {
            GlStateManager.color(1, 1, 1, 0.5F);
            GlStateManager.tryBlendFuncSeparate(SourceFactor.DST_COLOR, DestFactor.SRC_COLOR, SourceFactor.ONE, DestFactor.ZERO);
            GlStateManager.doPolygonOffset(-3, -3);
            GlStateManager.enablePolygonOffset();
            RenderHelper.disableStandardItemLighting();
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            this.renderModel(entity, x, y, z, yaw, partialTicks, false, true);
            GlStateManager.doPolygonOffset(0, 0);
            GlStateManager.disablePolygonOffset();
            RenderHelper.enableStandardItemLighting();
        }
        GlStateManager.disableBlend();
        super.doRender(entity, x, y, z, yaw, partialTicks);
    }

    private void renderModel(JeepWranglerEntity entity, double x, double y, double z, float yaw, float partialTicks, boolean windscreen, boolean destroy) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 1.25F, (float) z);        
        GlStateManager.rotate(180 - yaw, 0, 1, 0);
        CarRenderer.doCarRotations(entity, partialTicks);
        GlStateManager.scale(-1, -1, 1);
        (windscreen ? this.windscreen : destroy ? this.destroyModel : this.baseModel).render(entity, 0, 0, 0, 0, 0, 0.0625F);
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(JeepWranglerEntity entity) {
        return TEXTURE;
    }
}
