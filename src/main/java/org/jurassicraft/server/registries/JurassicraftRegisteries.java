package org.jurassicraft.server.registries;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.api.Hybrid;
import org.jurassicraft.server.conf.JurassiCraftConfig;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.period.TimePeriod;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber(modid = JurassiCraft.MODID)
public class JurassicraftRegisteries {

    public static IForgeRegistry<Dinosaur> DINOSAUR_REGISTRY;

    @SubscribeEvent
    public static void createRegisteries(RegistryEvent.NewRegistry event) {
        DINOSAUR_REGISTRY = new RegistryBuilder<Dinosaur>()
                .setType(Dinosaur.class)
                .setName(new ResourceLocation(JurassiCraft.MODID, "dinosaur"))
                .setDefaultKey(new ResourceLocation("jurassicraft:velociraptor"))
                .set(((key, isNetwork) -> Dinosaur.MISSING))
                .addCallback(new DinosaurCallbackManager())
                .create();
    }

    @SuppressWarnings("unchecked")
    public static HashMap<TimePeriod, List<Dinosaur>> getTimePeriodMap() {
        return DINOSAUR_REGISTRY.getSlaveMap(DinosaurCallbackManager.TIME_PERIOD_MAP, HashMap.class);
    }

    private static class DinosaurCallbackManager implements IForgeRegistry.AddCallback<Dinosaur>, IForgeRegistry.CreateCallback<Dinosaur> {

        public static final ResourceLocation TIME_PERIOD_MAP = new ResourceLocation(JurassiCraft.MODID, "timeperiod");

        @Override
        public void onCreate(IForgeRegistryInternal<Dinosaur> owner, RegistryManager stage) {
            owner.setSlaveMap(TIME_PERIOD_MAP, Maps.newHashMap());
        }

        @Override
        @SuppressWarnings("unchecked")
        public void onAdd(IForgeRegistryInternal<Dinosaur> owner, RegistryManager stage, int id, Dinosaur dinosaur, @Nullable Dinosaur oldObj) {
            dinosaur.init();
            if(!(dinosaur instanceof Hybrid)) {
                ((HashMap<TimePeriod, List<Dinosaur>>)owner.getSlaveMap(TIME_PERIOD_MAP, HashMap.class)).computeIfAbsent(dinosaur.getPeriod(), time -> Lists.newArrayList()).add(dinosaur);
                if(JurassiCraftConfig.ENTITIES.naturalSpawning) {
                    EntityRegistry.addSpawn(dinosaur.getDinosaurClass(), dinosaur.getSpawnChance(), 1, Math.min(6, dinosaur.getMaxHerdSize() / 2), dinosaur.isMarineCreature() ? EnumCreatureType.WATER_CREATURE : EnumCreatureType.CREATURE, dinosaur.getSpawnBiomes());
                }
            }
        }
    }
}
