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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.gui.TrackingTabletGui;
import org.jurassicraft.server.item.TrackingTablet;
import org.jurassicraft.server.message.SyncTrackingTabletMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TrackingMapIterator {

    public static Map<EntityPlayer, TrackingMapIterator> playerMap = Maps.newHashMap();

    private final EntityPlayer player;
    private final BlockPos playerPos;
    private final BiomeProvider biomeProvider;
    private int position;
    private boolean isFinished;

    private Biome[] biomes;

    private Biome[] preBiome = new Biome[TrackingTablet.DISTANCE * TrackingTablet.DISTANCE * 4];
    private int layersCached;

    public TrackingMapIterator(EntityPlayer entityPlayer) {
        this.player = entityPlayer;
        this.playerPos = entityPlayer.getPosition();
        this.biomeProvider = this.player.world.getBiomeProvider();
        if(playerMap.containsKey(entityPlayer)) {
            playerMap.get(entityPlayer).isFinished = true;
        }
        playerMap.put(entityPlayer, this);
        //Mojang like to store the int cache. When using a large area (2048x2048) that can stack up, causing memory issues. This is just to make sure that it isnt cached at all, so theres no memory leaks
        ReflectionHelper.setPrivateValue(IntCache.class, null, 256, "field_76451_a", "intCacheSize");
        IntCache.getIntCache(257);
    }

    public void start() {
        Handler.list.add(this);
    }

    private void onWorldTick() {
        if (this.isFinished) {
            return;
        }
        if(this.biomes == null) {
            Biome[] localBiomes = this.biomeProvider.getBiomes(null, playerPos.getX() - TrackingTablet.DISTANCE,playerPos.getZ() - TrackingTablet.DISTANCE + Math.floorDiv(this.layersCached, TrackingTablet.DISTANCE * 2), TrackingTablet.DISTANCE * 2, 64, false);
            System.arraycopy(localBiomes, 0, this.preBiome, this.layersCached, Math.min(localBiomes.length, this.preBiome.length - layersCached));
            this.layersCached += localBiomes.length;
            if(this.layersCached >= this.preBiome.length) {
                this.biomes = this.preBiome;
            }
            return;
        }
        List<Integer> list = Lists.newArrayList();
        long startTime = System.currentTimeMillis();
        int offset = position;
        boolean finished = true;
        while (position < TrackingTablet.DISTANCE * TrackingTablet.DISTANCE * 4) {
            Biome biome = this.biomes[position];
            int color;
            if (biome.getBaseHeight() >= 0) {
                color = 0xFF003791;
            } else {
                color = 0x20008791;
            }
            list.add(color);
            if(System.currentTimeMillis() - startTime >= 10) { //Timeout time
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

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            list.stream().peek(TrackingMapIterator::onWorldTick).filter(TrackingMapIterator::isFinished).collect(Collectors.toList()).stream().peek(list::remove).forEach(TrackingMapIterator::onRemoved);
        }
    }

}
