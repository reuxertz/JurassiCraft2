package org.genesis;

import org.apache.logging.log4j.Logger;
import org.genesis.internal.GenesisApiHandler;
import org.genesis.proxy.IProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Genesis.MODID, name = Genesis.NAME, version = Genesis.VERSION)
@Mod.EventBusSubscriber(modid = Genesis.MODID)
public class Genesis {
    public static final String MODID = "genesis";
    public static final String NAME = "Genesis";
    public static final String VERSION = "0.0.1";

    @SidedProxy(serverSide = "org.genesis.proxy.ServerProxy", clientSide = "org.genesis.proxy.ClientProxy")
    public static IProxy PROXY;

    @Instance(Genesis.MODID)
    public static Genesis INSTANCE;

    //LLibrary issue @NetworkWrapper
    public static SimpleNetworkWrapper NETWORK_WRAPPER;

    private static Logger logger;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        this.logger = event.getModLog();
        PROXY.onPreInit(event);
        GenesisApiHandler.loadPlugins(event.getAsmData());
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

    public static Logger getLogger() {
        return logger;
    }
}