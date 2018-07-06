package org.jurassicraft.server.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.gui.TrackingTabletGui;
import org.jurassicraft.server.item.TrackingTablet;
import org.jurassicraft.server.message.SyncTrackingTabletMap;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TrackingMapIterator {

    public static Map<EntityPlayer, TrackingMapIterator> playerMap = Maps.newHashMap();

    private final EntityPlayer player;
    private final BlockPos playerPos;
    private final BiomeProvider biomeProvider;
    private final int distance;

    private int position;
    private boolean isFinished;

    private Biome[] biomes;

    private Biome[] preBiome;
    private int layersCached;

    public TrackingMapIterator(EntityPlayer entityPlayer, int distance) {
        this.player = entityPlayer;
        this.playerPos = entityPlayer.getPosition();
        this.biomeProvider = this.player.world.getBiomeProvider();
        this.distance = distance;
        this.preBiome = new Biome[this.distance * this.distance * 4];
        if(playerMap.containsKey(entityPlayer)) {
            playerMap.get(entityPlayer).isFinished = true;
        }
        playerMap.put(entityPlayer, this);
        //Mojang like to store the int cache. When using a large area (2048x2048) that can stack up, causing memory issues. This is just to make sure that it isnt cached at all, so theres no memory leaks
        ReflectionHelper.setPrivateValue(IntCache.class, null, 256, FMLDeobfuscatingRemapper.INSTANCE.mapFieldName("net/minecraft/world/gen/layer/IntCache", "field_76451_a", "I"));
        IntCache.getIntCache(257);
    }

    public void start() {
        Handler.listToAdd.add(this);
    }

    private void onWorldTick() {
        if (this.isFinished) {
            return;
        }
        if(this.biomes == null) {
            Biome[] localBiomes = this.biomeProvider.getBiomes(null, playerPos.getX() - this.distance,playerPos.getZ() - this.distance + Math.floorDiv(this.layersCached, this.distance * 2), this.distance * 2, 128, false);
            System.arraycopy(localBiomes, 0, this.preBiome, this.layersCached, Math.min(localBiomes.length, this.preBiome.length - layersCached));
            this.layersCached += localBiomes.length;
            if(this.layersCached >= this.preBiome.length) {
                this.biomes = this.preBiome;
                this.preBiome = null;
            }
            return;
        }
        List<Integer> list = Lists.newArrayList();
        long startTime = System.currentTimeMillis();
        int offset = position;
        boolean finished = true;
        while (position < this.distance * this.distance * 4) {
            Biome biome = this.biomes[position];
            list.add(Biome.getIdForBiome(biome));
            if(System.currentTimeMillis() - startTime >= 20) { //Timeout time
                finished = false;
                break;
            }
            position++;
        }
        this.isFinished = finished;
        JurassiCraft.NETWORK_WRAPPER.sendTo(new SyncTrackingTabletMap(list, offset), (EntityPlayerMP) this.player);
    }

    public void onRemoved() {
        this.biomes = new Biome[0]; //Clear cache
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    public void markFinished() {
        this.isFinished = true;
    }

    @Mod.EventBusSubscriber(modid = JurassiCraft.MODID)
    @SuppressWarnings("unused")
    public static class Handler {

        private static final List<TrackingMapIterator> list = Lists.newArrayList();
        private static final List<TrackingMapIterator> listToAdd = Lists.newArrayList();

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            list.addAll(listToAdd);
            listToAdd.clear();
            list.stream().peek(TrackingMapIterator::onWorldTick).filter(TrackingMapIterator::isFinished).collect(Collectors.toList()).stream().peek(list::remove).forEach(TrackingMapIterator::onRemoved);
        }
    }

}
