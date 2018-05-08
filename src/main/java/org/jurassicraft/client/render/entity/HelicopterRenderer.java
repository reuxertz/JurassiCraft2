package org.jurassicraft.client.render.entity;

import com.google.common.collect.Maps;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.animation.entity.vehicle.HelicopterAnimator;
import org.jurassicraft.server.entity.vehicle.HelicopterBaseEntity;
import org.jurassicraft.server.entity.vehicle.modules.HelicopterAnimationModels;
import org.jurassicraft.server.entity.vehicle.modules.HelicopterModule;
import org.jurassicraft.server.entity.vehicle.modules.HelicopterModuleSpot;
import org.jurassicraft.server.tabula.TabulaModelHelper;
import org.lwjgl.opengl.GL11;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class HelicopterRenderer implements IRenderFactory<HelicopterBaseEntity> {
    @Override
    public Render<? super HelicopterBaseEntity> createRenderFor(RenderManager manager) {
        return new Renderer(manager);
    }

    public static class Renderer extends Render<HelicopterBaseEntity> {
        private static final ResourceLocation TEXTURE = new ResourceLocation(JurassiCraft.MODID, "textures/entities/helicopter/ranger_helicopter_texture.png");
        private final Map<String, TabulaModel> moduleMap;
        private final Map<String, ResourceLocation> moduleTextures;
        private TabulaModel baseModel;

        public Renderer(RenderManager manager) {
            super(manager);
            this.moduleMap = Maps.newHashMap();
            this.moduleTextures = Maps.newHashMap();
            try {
                this.baseModel = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/entities/helicopter/ranger_helicopter"), new HelicopterAnimator());

                // Modules init.
                for (String id : HelicopterModule.registry.keySet()) {
                    TabulaModel model = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/entities/helicopter/modules/ranger_helicopter_" + id), HelicopterAnimationModels.animatorRegistry.get(HelicopterModule.registry.get(id)));
                    this.moduleMap.put(id, model);

                    this.moduleTextures.put(id, new ResourceLocation(JurassiCraft.MODID, "textures/entities/helicopter/modules/ranger_helicopter_" + id + "_texture.png"));
                }
            } catch (Exception e) {
                JurassiCraft.INSTANCE.getLogger().fatal("Failed to load the models for the Helicopter", e);
            }
        }

        @Override
        public void doRender(HelicopterBaseEntity helicopter, double x, double y, double z, float yaw, float partialTicks) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x, (float) y + 1.5F, (float) z);
            GlStateManager.rotate(180.0F - yaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((float) helicopter.interpRotationPitch.getValueForRendering(partialTicks), 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate((float) helicopter.interpRotationRoll.getValueForRendering(partialTicks), 0.0F, 0.0F, 1.0F);

            float f4 = 1f;
            GlStateManager.scale(f4, f4, f4);
            GlStateManager.scale(1.0F / f4, 1.0F / f4, 1.0F / f4);
            this.bindEntityTexture(helicopter);
            GlStateManager.scale(-1.0F, -1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.baseModel.render(helicopter, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

            this.renderModules(helicopter);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GlStateManager.popMatrix();
            super.doRender(helicopter, x, y, z, yaw, partialTicks);
        }

        private void renderModules(HelicopterBaseEntity helicopter) {
            for (HelicopterModuleSpot spot : helicopter.getModuleSpots()) {
                GlStateManager.pushMatrix();
                GlStateManager.rotate((float) Math.toDegrees(spot.getAngleFromCenter()), 0, 1, 0);
                for (HelicopterModule m : spot.getModules()) {
                    if (m == null) {
                        continue;
                    }
                    GlStateManager.rotate((float) Math.toDegrees(m.getBaseRotationAngle()), 0, 1, 0);
                    this.bindTexture(this.moduleTextures.get(m.getModuleID()));
                    TabulaModel model = this.moduleMap.get(m.getModuleID());
                    model.render(helicopter, 0f, 0f, 0f, 0f, 0f, 0.0625f);
                    GlStateManager.rotate(-(float) Math.toDegrees(m.getBaseRotationAngle()), 0, 1, 0);
                }
                GlStateManager.popMatrix();
            }
        }

        @Override
        protected ResourceLocation getEntityTexture(HelicopterBaseEntity entity) {
            return TEXTURE;
        }
    }
}