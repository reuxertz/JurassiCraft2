package org.jurassicraft.server.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class EntityEventHandler {

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent event) {
        //getProxy().render(event.getPartialTicks());

        return;

    }

    @SubscribeEvent
    public void onRenderLivingEvent(RenderLivingEvent.Specials.Pre<DinosaurEntity> renderLivingEvent)
    {

        if (true)//!(renderLivingEvent.getEntity() instanceof DinosaurEntity))
            return;

//        double minX = renderLivingEvent.getEntity().posX;
//        double minY = renderLivingEvent.getEntity().posY;
//        double minZ = renderLivingEvent.getEntity().posZ;
//
//        double maxX = minX + 2;
//        double maxY = minY + 20;
//        double maxZ = minZ + 2;

        double maxX = renderLivingEvent.getEntity().posX;
        double maxY = renderLivingEvent.getEntity().posY;
        double maxZ = renderLivingEvent.getEntity().posZ;

        double minX = maxX + 2;
        double minY = maxY + 20;
        double minZ = maxZ + 2;

//        double minX = 0;
//        double minY = 0;
//        double minZ = 0;
//
//        double maxX = 2;
//        double maxY = 20;
//        double maxZ = 2;

//        double maxX = 0;
//        double maxY = 0;
//        double maxZ = 0;
//
//        double minX = 1;
//        double minY = 2;
//        double minZ = 1;

        Vec3d posA = new Vec3d(maxX, maxY, maxZ);
        Vec3d posB = new Vec3d(minX, minY, minZ);

        Entity entity = renderLivingEvent.getEntity();
        Vec3d player_pos = new Vec3d(entity.posX, entity.posY, entity.posZ);

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glTranslated(-player_pos.x, -player_pos.y, -player_pos.z);

        Color c = new Color(255, 0, 0, 150);
        GL11.glColor4d(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        GL11.glLineWidth(1.0f);
        GL11.glDepthMask(false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        double dx = Math.abs(posA.x - posB.x);
        double dy = Math.abs(posA.y - posB.y);
        double dz = Math.abs(posA.z - posB.z);

        //AB
        bufferBuilder.pos(posA.x, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();          //A
        bufferBuilder.pos(posA.x, posA.y, posA.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();       //B
        //BC
        bufferBuilder.pos(posA.x, posA.y, posA.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();       //B
        bufferBuilder.pos(posA.x+dx, posA.y, posA.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();    //C
        //CD
        bufferBuilder.pos(posA.x+dx, posA.y, posA.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();    //C
        bufferBuilder.pos(posA.x+dx, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();       //D
        //DA
        bufferBuilder.pos(posA.x+dx, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();       //D
        bufferBuilder.pos(posA.x, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();          //A
        //EF
        bufferBuilder.pos(posA.x, posA.y+dy, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();       //E
        bufferBuilder.pos(posA.x, posA.y+dy, posA.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();    //F
        //FG
        bufferBuilder.pos(posA.x, posA.y+dy, posA.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();    //F
        bufferBuilder.pos(posA.x+dx, posA.y+dy, posA.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex(); //G
        //GH
        bufferBuilder.pos(posA.x+dx, posA.y+dy, posA.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex(); //G
        bufferBuilder.pos(posA.x+dx, posA.y+dy, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();    //H
        //HE
        bufferBuilder.pos(posA.x+dx, posA.y+dy, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();    //H
        bufferBuilder.pos(posA.x, posA.y+dy, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();       //E
        //AE
        bufferBuilder.pos(posA.x, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();          //A
        bufferBuilder.pos(posA.x, posA.y+dy, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();       //E
        //BF
        bufferBuilder.pos(posA.x, posA.y, posA.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();       //B
        bufferBuilder.pos(posA.x, posA.y+dy, posA.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();    //F
        //CG
        bufferBuilder.pos(posA.x+dx, posA.y, posA.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();    //C
        bufferBuilder.pos(posA.x+dx, posA.y+dy, posA.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex(); //G
        //DH
        bufferBuilder.pos(posA.x+dx, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();       //D
        bufferBuilder.pos(posA.x+dx, posA.y+dy, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();    //H

        tessellator.draw();


        GL11.glDepthMask(true);
        GL11.glPopAttrib();

        //drawSelectionBox(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ));

        return;
    }
    /**
     * Draws the selection box for the player.
     */
    public void drawSelectionBox(AxisAlignedBB bb)
    {

//        if (execute == 0 && movingObjectPositionIn.typeOfHit == RayTraceResult.Type.BLOCK)
//        {

            if(true) {
                GL11.glDisable(GL11.GL_DEPTH_TEST);
            }

            GlStateManager.enableBlend();
            //GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.glLineWidth(2.0f);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);

//            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && this.mc.objectMouseOver.getBlockPos() != null )
//
//            {
//                BlockPos blockpos1 = this.mc.objectMouseOver.getBlockPos();
//
//                // BlockPos blockpos = movingObjectPositionIn.getBlockPos();
//                IBlockState iblockstate = player.getEntityWorld().getBlockState(blockpos1);
//
//
//                //  if (iblockstate.getMaterial() != Material.AIR && player.getEntityWorld().getWorldBorder().contains(blockpos))
//                //  {
//                double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
//                double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
//                double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
                //AxisAlignedBB bb = iblockstate.getSelectedBoundingBox(player.getEntityWorld(), blockpos1).grow(0.0020000000949949026D).offset(-d0, -d1, -d2);



                //Draw Boxes
                drawSelectionBoxMask(bb, 1.0f, 0f, 1.0f, 1.0f);

                drawSelectionBoundingBox(bb, 1.0f, 0f, 1.0f, 1.0f);
            //}


            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        //}
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB box, float red, float green, float blue, float alpha)
    {
        Tessellator t = Tessellator.getInstance();
        BufferBuilder b = t.getBuffer();
        b.begin(7, DefaultVertexFormats.POSITION_COLOR);
        drawBoundingBox(b, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, red, green, blue, alpha);
        t.draw();
    }

    public static void drawSelectionBoxMask(AxisAlignedBB box, float red, float green, float blue, float alpha)
    {
        drawMask(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, red, green, blue, alpha);
    }

    public static void drawMask(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha)
    {
        Tessellator t = Tessellator.getInstance();
        BufferBuilder b = t.getBuffer();
        b.begin(7, DefaultVertexFormats.POSITION_COLOR);
        drawMask(b, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
        t.draw();
    }

    public static void drawBoundingBox(BufferBuilder b, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha)
    {
        b.pos(minX, minY, minZ).color(red, green, blue, 0.0F).endVertex();
        b.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        b.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        b.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        b.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        b.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        b.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        b.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        b.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        b.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        b.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        b.pos(minX, maxY, maxZ).color(red, green, blue, 0.0F).endVertex();
        b.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        b.pos(maxX, maxY, maxZ).color(red, green, blue, 0.0F).endVertex();
        b.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        b.pos(maxX, maxY, minZ).color(red, green, blue, 0.0F).endVertex();
        b.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        b.pos(maxX, minY, minZ).color(red, green, blue, 0.0F).endVertex();
    }

    public static void drawMask(BufferBuilder b, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha)
    {
        //up
        b.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        b.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        b.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        b.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        //b.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();

        //down
        b.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        b.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        b.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        b.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        //b.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();

        //north
        b.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        b.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        b.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        b.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        //b.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();

        //south
        b.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        b.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        b.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        b.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        //b.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();

        //east
        b.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        b.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        b.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        b.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        //b.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();

        //west
        b.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        b.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        b.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        b.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        //b.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
    }
}
