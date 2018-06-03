package org.jurassicraft;

import org.apache.logging.log4j.Logger;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.command.ForceAnimationCommand;
import org.jurassicraft.server.conf.JurassiCraftConfig;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.message.*;
import org.jurassicraft.server.proxy.ServerProxy;

import net.ilexiconn.llibrary.server.config.Config;
import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.ilexiconn.llibrary.server.update.UpdateHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = JurassiCraft.MODID, name = JurassiCraft.NAME, version = JurassiCraft.VERSION, guiFactory = "org.jurassicraft.client.gui.JurassiCraftGUIFactory", dependencies = "required-after:llibrary@[" + JurassiCraft.LLIBRARY_VERSION + ",)")
public class JurassiCraft {
    public static final String MODID = "jurassicraft";
    public static final String NAME = "JurassiCraft";
    public static final String VERSION = "2.1.3";

    public static final String LLIBRARY_VERSION = "1.7.7";
    @SidedProxy(serverSide = "org.jurassicraft.server.proxy.ServerProxy", clientSide = "org.jurassicraft.client.proxy.ClientProxy")
    public static ServerProxy PROXY;

    @Instance(JurassiCraft.MODID)
    public static JurassiCraft INSTANCE;

    @Config
    public static JurassiCraftConfig CONFIG;

    public static long timerTicks;

    @NetworkWrapper({ PlacePaddockSignMessage.class, ChangeTemperatureMessage.class, HelicopterEngineMessage.class, HelicopterDirectionMessage.class, HelicopterModulesMessage.class, SwitchHybridizerCombinatorMode.class, SetOrderMessage.class, OpenFieldGuideGuiMessage.class, UpdateVehicleControlMessage.class, MicroraptorDismountMessage.class, FordExplorerChangeStateMessage.class, FordExplorerUpdatePositionStateMessage.class, DNASequenceTransferClicked.class, CarEntityPlayRecord.class, AttemptMoveToSeatMessage.class})
    public static SimpleNetworkWrapper NETWORK_WRAPPER;

    private Logger logger;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        this.logger = event.getModLog();
        UpdateHandler.INSTANCE.registerUpdateChecker(this, "http://pastebin.com/raw/Rb96SNWb");
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
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ForceAnimationCommand());
    }

    @Mod.EventHandler
    public void missingMap(FMLMissingMappingsEvent event) {
        for (FMLMissingMappingsEvent.MissingMapping miss : event.get()) {
            ResourceLocation identifier = miss.resourceLocation;
            switch (miss.type) {
                case BLOCK:
                    if (identifier.equals(new ResourceLocation(JurassiCraft.MODID, "action_figure_block"))) {
                        miss.remap(BlockHandler.DISPLAY_BLOCK);
                    }
                    break;
                case ITEM:
                    if (identifier.equals(new ResourceLocation(JurassiCraft.MODID, "action_figure"))) {
                        miss.remap(ItemHandler.DISPLAY_BLOCK);
                    }
                    break;
            }
        }
    }

    public Logger getLogger() {
        return this.logger;
    }
}