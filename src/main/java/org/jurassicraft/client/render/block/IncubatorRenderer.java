package org.jurassicraft.client.render.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jurassicraft.client.render.RenderingHandler;
import org.jurassicraft.client.render.entity.dinosaur.DinosaurRenderInfo;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.entity.IncubatorBlockEntity;
import org.jurassicraft.server.api.DinosaurProvider;

public class IncubatorRenderer extends TileEntitySpecialRenderer<IncubatorBlockEntity> {
    private Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void render(IncubatorBlockEntity tileEntity, double x, double y, double z, float p_180535_8_, int p_180535_9_, float alpha) {
        World world = tileEntity.getWorld();

        IBlockState state = world.getBlockState(tileEntity.getPos());

        if (state.getBlock() == BlockHandler.INCUBATOR) {
            GlStateManager.cullFace(GlStateManager.CullFace.FRONT);
            GlStateManager.disableRescaleNormal();

            this.renderEgg(tileEntity.getStackInSlot(0), x, y, z, 0.2, 0.2);
            this.renderEgg(tileEntity.getStackInSlot(1), x, y, z, 0.2, 0.8);
            this.renderEgg(tileEntity.getStackInSlot(3), x, y, z, 0.8, 0.8);
            this.renderEgg(tileEntity.getStackInSlot(4), x, y, z, 0.8, 0.2);
            this.renderEgg(tileEntity.getStackInSlot(2), x, y + 0.05, z, 0.5, 0.5);

            GlStateManager.enableRescaleNormal();
            GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        }
    }

    private void renderEgg(ItemStack stack, double x, double y, double z, double xOffset, double zOffset) {
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y + 0.05, z);
            GlStateManager.translate(xOffset, 1.2, zOffset);
            GlStateManager.scale(-0.5F, -0.5F, -0.5F);
            DinosaurRenderInfo renderDef = RenderingHandler.INSTANCE.getRenderInfo(DinosaurProvider.getFromStack(stack).getValue(stack));
            this.mc.getTextureManager().bindTexture(renderDef.getEggTexture());
            renderDef.getEggModel().render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            GlStateManager.popMatrix();
        }
    }
}
