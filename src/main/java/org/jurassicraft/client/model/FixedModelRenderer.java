package org.jurassicraft.client.model;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.jurassicraft.server.entity.dinosaur.DinosaurEntity;

import java.util.Random;

public class FixedModelRenderer extends AdvancedModelRenderer {

    private static final Random RANDOM = new Random();

    public DinosaurEntity entityInJaw;
    public float partialTicks;
    public ResourceLocation location;

    private float fixX;
    private float fixY;
    private float fixZ;

    private int displayList;
    private boolean compiled;

    public FixedModelRenderer(AdvancedModelBase model, String name) {
        super(model, name);
        RANDOM.setSeed(name.hashCode() << 16);
        float offsetScale = 0.005F;
        this.fixX = (RANDOM.nextFloat() - 0.5F) * offsetScale;
        this.fixY = (RANDOM.nextFloat() - 0.5F) * offsetScale;
        this.fixZ = (RANDOM.nextFloat() - 0.5F) * offsetScale;
    }

    @Override
    public void render(float scale) {
        if(this.entityInJaw != null && !this.entityInJaw.isDead && location != null) { //100% threadsafe trust me my dad works at microsoft
            Minecraft mc = Minecraft.getMinecraft();
            float jawOffset = 0.15f; //TODO: make jawOffset and jawWidth rely on dinosaur class and in tern dinosaur json
            float jawWidth = 0.4f;
            GlStateManager.translate(0, -entityInJaw.height - jawOffset, -jawWidth / 2F);
            GlStateManager.rotate(90F, 0, 1, 0);

            entityInJaw.forceRender();
            mc.getRenderManager().getEntityClassRenderObject(this.entityInJaw.getClass()).doRender(entityInJaw, 0, 0, 0, 0, partialTicks);
            GlStateManager.rotate(-90F, 0, 1, 0);
            GlStateManager.translate(0, entityInJaw.height + jawOffset, jawWidth / 2F);
            mc.getTextureManager().bindTexture(location);
        }
        if (!this.isHidden) {
            if (this.showModel) {
                GlStateManager.pushMatrix();
                if (!this.compiled) {
                    this.compileDisplayList(scale);
                }
                GlStateManager.translate(this.offsetX + this.fixX, this.offsetY + this.fixY, this.offsetZ + this.fixZ);
                GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                if (this.rotateAngleZ != 0.0F) {
                    GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleZ), 0.0F, 0.0F, 1.0F);
                }
                if (this.rotateAngleY != 0.0F) {
                    GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleY), 0.0F, 1.0F, 0.0F);
                }
                if (this.rotateAngleX != 0.0F) {
                    GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleX), 1.0F, 0.0F, 0.0F);
                }
                if (this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F) {
                    GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
                }
                GlStateManager.callList(this.displayList);
                if (this.childModels != null) {
                    for (ModelRenderer childModel : this.childModels) {
                        childModel.render(scale);
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }

    private void compileDisplayList(float scale) {
        this.displayList = GLAllocation.generateDisplayLists(1);
        GlStateManager.glNewList(this.displayList, 4864);
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        for (ModelBox box : this.cubeList) {
            box.render(buffer, scale);
        }
        GlStateManager.glEndList();
        this.compiled = true;
    }
}
