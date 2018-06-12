package org.jurassicraft.client.render.entity.dinosaur.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.render.entity.DinosaurRenderer;
import org.jurassicraft.server.entity.DinosaurEntity;

@SideOnly(Side.CLIENT)
public class LayerEyes implements LayerRenderer<DinosaurEntity>
{
    private final DinosaurRenderer renderer;

    public LayerEyes(DinosaurRenderer renderer)
    {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(DinosaurEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float age, float yaw, float pitch, float scale)
    {
        if (!entity.isInvisible())
        {
            if (!entity.areEyelidsClosed())
            {
                ResourceLocation texture = this.renderer.dinosaur.getOverlayTexture(entity, "eyes");
                if (texture != null)
                {
                    ITextureObject textureObject = Minecraft.getMinecraft().getTextureManager().getTexture(texture);
                    if (textureObject != TextureUtil.MISSING_TEXTURE)
                    {
                        this.renderer.bindTexture(texture);

                        this.renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, age, yaw, pitch, scale);
                        this.renderer.setLightmap(entity);
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