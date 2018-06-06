package org.jurassicraft.server.entity.villager;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.world.structure.GeneticistVillagerHouse;

@Mod.EventBusSubscriber(modid = JurassiCraft.MODID)
public class VillagerHandler {
    public static final GeneticistProfession GENETICIST = new GeneticistProfession();

    public static void init() {
        VillagerRegistry.instance().registerVillageCreationHandler(new GeneticistVillagerHouse.CreationHandler());
        MapGenStructureIO.registerStructureComponent(GeneticistVillagerHouse.class, "GeneticistHouse");
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onReigster(RegistryEvent.Register<VillagerRegistry.VillagerProfession> event) {
        event.getRegistry().register(GENETICIST);
    }
}