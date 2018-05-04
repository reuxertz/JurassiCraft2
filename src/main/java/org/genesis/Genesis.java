package org.genesis;

import net.minecraft.block.SoundType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.genesis.proxy.ServerProxy;

@Mod(modid = Genesis.MODID, name = Genesis.NAME, version = Genesis.VERSION)
@Mod.EventBusSubscriber(modid = Genesis.MODID)
public class Genesis {
    public static final String MODID = "genesis";
    public static final String NAME = "Genesis";
    public static final String VERSION = "0.0.1";

    @SidedProxy(serverSide = "org.genesis.proxy.ServerProxy", clientSide = "org.genesis.proxy.ClientProxy")
    public static ServerProxy PROXY;

    @Instance(Genesis.MODID)
    public static Genesis INSTANCE;

    //LLibrary issue @NetworkWrapper
    public static SimpleNetworkWrapper NETWORK_WRAPPER;

    private Logger logger;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        this.logger = event.getModLog();

        OBJLoader.INSTANCE.addDomain(Genesis.MODID);
        PROXY.onPreInit(event);

    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        PROXY.onInit(event);
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        PROXY.onPostInit(event);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {

    }

    public Logger getLogger() {
        return this.logger;
    }
}