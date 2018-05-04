package org.jurassicraft.client.render.entity;

import java.util.List;

import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.entity.vehicle.CarEntity;
import org.jurassicraft.server.entity.vehicle.util.WheelParticleData;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid=JurassiCraft.MODID, value=Side.CLIENT)
public class TyretrackRenderer {
    
    public static final List<Material> ALLOWED_MATERIALS = Lists.newArrayList(Material.GRASS, Material.GROUND, Material.SAND);//TODO: configurable ?
    
    public static final ResourceLocation TYRE_TRACKS_LOCATION = new ResourceLocation(JurassiCraft.MODID, "textures/misc/tyre-tracks.png");
    
    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.world;
        EntityPlayer player = mc.player;
        Vec3d playerHead = player.getPositionEyes(event.getPartialTicks());
      
        GlStateManager.enableBlend();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            
        mc.getTextureManager().bindTexture(TYRE_TRACKS_LOCATION);
        Tessellator tess = Tessellator.getInstance();
        VertexBuffer buffer = tess.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        for(Entity entity : world.loadedEntityList) {
            if(entity instanceof CarEntity) {
            CarEntity car = (CarEntity)entity;
                GlStateManager.pushMatrix(); 
                {
                    double d0 = (player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)event.getPartialTicks());
                    double d1 = (player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)event.getPartialTicks());
                    double d2 = (player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)event.getPartialTicks());
                    buffer.setTranslation(-d0, -d1, -d2);
                    List<WheelParticleData> dataList = car.wheelDataList;
                    for(int i = 0; i < dataList.size() - 4; i++) {
                        
                        WheelParticleData start = dataList.get(i);
                        WheelParticleData end = dataList.get(i + 4);
                            
                        Vec3d sv = start.getPosition();
                        Vec3d ev = end.getPosition();
                        Vec3d opposite = dataList.get(i - (i % 4) + (3 - i % 4)/*//TODO: simplify (if possible?)*/).getPosition();
                       
                        BlockPos position = new BlockPos(sv);
                        BlockPos downPos = position.down();
                        IBlockState downState = world.getBlockState(downPos);
                        if(!downState.isSideSolid(world, downPos, EnumFacing.UP) || sv.y != ev.y || !ALLOWED_MATERIALS.contains(downState.getMaterial())) {
                            continue;
                        }
                        
                        double d = 1D / Math.sqrt(Math.pow(sv.x - opposite.x, 2) + Math.pow(sv.z - opposite.z, 2)) / 2D;    
                        Vec3d vec = new Vec3d((sv.x - opposite.x) * d, 0, (sv.z - opposite.z) * d);
                        
                        float sl = world.getLightBrightness(position);
                        float el = world.getLightBrightness(new BlockPos(ev));
                            
                            
                        float sa = start.getAlpha(event.getPartialTicks());
                        float ea = end.getAlpha(event.getPartialTicks());
                                            
                        double offset = (i + 1) * 0.000002D; //No z-fighting on my watch
                        
                        buffer.pos(sv.x , sv.y + offset, sv.z).tex(0, 0).color(sl, sl, sl, sa).endVertex();
                        buffer.pos(sv.x - vec.x, sv.y + offset, sv.z - vec.z).tex(0, 1).color(sl, sl, sl, sa).endVertex();
                        buffer.pos(ev.x - vec.x, ev.y + offset, ev.z - vec.z).tex(1, 1).color(el, el, el, ea).endVertex();
                        buffer.pos(ev.x, ev.y + offset, ev.z).tex(1, 0).color(el, el, el, ea).endVertex();
                            
                        //Flip quad to render upside down. Means when looking at tyre track from underneath, it still rendered. Needed because one set of tyres are upside down. //TODO: Fix that
                        buffer.pos(sv.x , sv.y + offset, sv.z).tex(0, 0).color(sl, sl, sl, sa).endVertex();
                        buffer.pos(ev.x, ev.y + offset, ev.z).tex(1, 0).color(el, el, el, ea).endVertex();     
                        buffer.pos(ev.x - vec.x, ev.y + offset, ev.z - vec.z).tex(1, 1).color(el, el, el, ea).endVertex();
                        buffer.pos(sv.x - vec.x, sv.y + offset, sv.z - vec.z).tex(0, 1).color(sl, sl, sl, sa).endVertex();
                    }
                }
                GlStateManager.popMatrix();
            }
        }
        tess.draw();
        buffer.setTranslation(0, 0, 0);
            
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
    
    }
}
