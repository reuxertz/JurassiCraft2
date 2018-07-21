package org.jurassicraft.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.model.AnimatableModel;
import org.jurassicraft.client.render.RenderingHandler;
import org.jurassicraft.client.render.entity.dinosaur.DinosaurRenderInfo;
import org.jurassicraft.server.entity.Dinosaur;
import org.jurassicraft.server.dna.GeneType;
import org.jurassicraft.server.entity.dinosaur.DinosaurEntity;
import org.jurassicraft.server.entity.GrowthStage;

import java.awt.*;

public class DinosaurRenderer extends RenderLiving<DinosaurEntity> {

    public DinosaurRenderer(DinosaurRenderInfo fallBackInfo, RenderManager rendermanagerIn) {
        super(rendermanagerIn, fallBackInfo.getModelAdult(), fallBackInfo.getShadowSize());
    }

    @Override
    public void preRenderCallback(DinosaurEntity entity, float partialTick) {
        float scaleModifier = entity.getAttributes().getScaleModifier();
        Dinosaur dinosaur = entity.getDinosaur();
        DinosaurRenderInfo renderInfo = RenderingHandler.renderInfos.get(dinosaur);

        float scale = (float) entity.interpolate(dinosaur.scaleInfant, dinosaur.scaleAdult) * scaleModifier * (entity.getDNA().getValueFloat(GeneType.SIZE) + 1.5f) / 1.5f;

        this.shadowSize = scale * renderInfo.getShadowSize();

        GlStateManager.translate(dinosaur.offsetX * scale, dinosaur.offsetY * scale, dinosaur.offsetZ * scale);

        String name = entity.getCustomNameTag();
//        scale = 1;
        switch (name) {
            case "Kashmoney360":
            case "JTGhawk137":
                GlStateManager.scale(0.1F, scale, scale);
                break;
            case "Gegy":
                GlStateManager.scale(scale, 0.01F, scale);
                break;
            case "Notch":
                GlStateManager.scale(scale * 2, scale * 2, scale * 2);
                break;
            case "jglrxavpok":
                GlStateManager.scale(scale, scale, scale * -1);
                break;
            case "Wyn":
                int color = Color.HSBtoRGB((entity.world.getTotalWorldTime() % 1000) / 100f, 1f, 1f);
                GlStateManager.color((color & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, ((color >> 16) & 0xFF) / 255f);
            default:
                GlStateManager.scale(scale, scale, scale);
                break;
        }
    }

    @Override
    public void doRender(DinosaurEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        AnimatableModel model = RenderingHandler.renderInfos.get(entity.getDinosaur()).getModel(entity.getGrowthStage());
        this.mainModel = model;
        model.partialTicks = partialTicks;
        model.location = this.getEntityTexture(entity);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    public ResourceLocation getEntityTexture(DinosaurEntity entity) {
        GrowthStage growthStage = entity.getGrowthStage();
        Dinosaur dinosaur = entity.getDinosaur();
        if (!dinosaur.doesSupportGrowthStage(growthStage)) {
            growthStage = GrowthStage.ADULT;
        }
        return entity.isMale() ? dinosaur.getMaleTexture(growthStage) : dinosaur.getFemaleTexture(growthStage);
    }

    @Override
    protected void applyRotations(DinosaurEntity entity, float p_77043_2_, float rotationYaw, float partialTicks) {
        GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
    }

    @SideOnly(Side.CLIENT)
    public class LayerEyelid implements LayerRenderer<DinosaurEntity> {
        private final DinosaurRenderer renderer;

        public LayerEyelid(DinosaurRenderer renderer) {
            this.renderer = renderer;
        }

        @Override
        public void doRenderLayer(DinosaurEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float age, float yaw, float pitch, float scale) {
            if (!entity.isInvisible()) {
                if (entity.areEyelidsClosed()) {
                    Dinosaur dinosaur = entity.getDinosaur();
                    ResourceLocation texture = dinosaur.getEyelidTexture(entity);
                    if (texture != null) {
                        ITextureObject textureObject = Minecraft.getMinecraft().getTextureManager().getTexture(texture);
                        if (textureObject != TextureUtil.MISSING_TEXTURE) {
                            this.renderer.bindTexture(texture);

                            RenderingHandler.renderInfos.get(dinosaur).getModelAdult().render(entity, limbSwing, limbSwingAmount, age, yaw, pitch, scale);
                            this.renderer.setLightmap(entity); //TODO: Make sure this works this.renderer.setLightmap(entity, partialTicks);
                        }
                    }
                }
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return true;
        }
    }
}
