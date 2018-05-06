package org.genesis.client;

import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.genesis.Genesis;
import org.genesis.server.ServerProxy;

public class ClientProxy extends ServerProxy {

    public void onPreInit(FMLPreInitializationEvent event) {
        OBJLoader.INSTANCE.addDomain(Genesis.MODID);
    }

    public void onPostInit(FMLPostInitializationEvent event) {
    }

    public void onInit(FMLInitializationEvent event) {

    }
}
