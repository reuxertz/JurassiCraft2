package org.genesis;

import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.genesis.genetics.Gene;
import org.genesis.genetics.Genome;
import org.genesis.server.ServerProxy;

import java.util.List;

@Mod(modid = Genesis.MODID, name = Genesis.NAME, version = Genesis.VERSION)
public class Genesis {
    public static final String MODID = "genesis";
    public static final String NAME = "Genesis";
    public static final String VERSION = "0.0.1";

    @SidedProxy(serverSide = "org.genesis.server.ServerProxy", clientSide = "org.genesis.client.ClientProxy")
    public static ServerProxy PROXY;

    @Instance(Genesis.MODID)
    public static Genesis INSTANCE;

    //LLibrary issue @NetworkWrapper
    public static SimpleNetworkWrapper NETWORK_WRAPPER;

    private Logger logger;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        this.logger = event.getModLog();

        String tg = Gene.TestGene;
        Genome g = new Genome(tg, tg);

        List<Gene> expGenes = g.ExpressedGenes;

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

    public Logger getLogger() {
        return this.logger;
    }
}