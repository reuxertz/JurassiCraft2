package org.genesis.proxy;

import org.genesis.Genesis;
import org.genesis.internal.GenesisApiHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    public RenderManager renderManager;
    public RenderItem renderItem;
    public ItemModelMesher itemModelMesher;

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void onInit(FMLInitializationEvent event) {
	
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {

        renderManager = Minecraft.getMinecraft().getRenderManager();
        renderItem = Minecraft.getMinecraft().getRenderItem();
        itemModelMesher = renderItem.getItemModelMesher();

        this.registerEntityRenderers();
        this.registerObjRegRenderers();
        
        ((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(resourceManager -> {
            GenesisApiHandler.register();
            Genesis.getLogger().info("Reloaded API");
        });
    }

    // Protected Functions
    protected void registerObjRegRenderers() {
    }

    protected void registerEntityRenderers() {

    }
}