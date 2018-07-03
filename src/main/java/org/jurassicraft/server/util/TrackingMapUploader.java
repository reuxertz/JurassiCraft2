package org.jurassicraft.server.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.item.TrackingTablet;
import org.jurassicraft.server.message.SyncTrackingTabletMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TrackingMapUploader {

    public static Map<EntityPlayer, TrackingMapUploader> playerMap = Maps.newHashMap();

    private final EntityPlayer entityPlayer;
    private final BlockPos playerPos;
    private int position;
    int[][] cachedYHeights = new int[TrackingTablet.DISTANCE * 2][TrackingTablet.DISTANCE * 2];
    private boolean isFinished;

    List<Chunk> chunksToUnload = Lists.newArrayList();

    public TrackingMapUploader(EntityPlayer entityPlayer, BlockPos playerPos) {
        if(playerMap.containsKey(entityPlayer)) {
            playerMap.get(entityPlayer).isFinished = true;
        }
        playerMap.put(entityPlayer, this);
        this.entityPlayer = entityPlayer;
        this.playerPos = playerPos;
        Handler.list.add(this);
    }

    public void onWorldTick() {
        if(this.isFinished) {
            return;
        }
        World world = entityPlayer.world;
        IChunkProvider provider = world.getChunkProvider();
        int length = TrackingTablet.DISTANCE * 2;
        List<Integer> list = Lists.newArrayList();
        long startTime = System.currentTimeMillis();
        int offset = position;
        boolean finished = true;
        while(position < TrackingTablet.DISTANCE * TrackingTablet.DISTANCE * 4) {
            int x = (position % length);
            int z = Math.floorDiv(position, length);
            int chunkX = (playerPos.getX() + x - TrackingTablet.DISTANCE) >> 4;
            int chunkZ = (playerPos.getZ() + z - TrackingTablet.DISTANCE) >> 4;
            Chunk rawChunk = provider.getLoadedChunk(chunkX, chunkZ);
            boolean unloaded = false;
            if(rawChunk == null || !rawChunk.isLoaded()) {
                unloaded = true;
            }
            Chunk chunk = provider.provideChunk(chunkX, chunkZ);
            if(unloaded) {
                this.chunksToUnload.add(chunk);
            }
            int y = chunk.getTopFilledSegment() + 16;
            int color = -16777216;
            while(y >= 0) {
                BlockPos pos = new BlockPos(playerPos.getX() + x - TrackingTablet.DISTANCE, y, playerPos.getZ() + z - TrackingTablet.DISTANCE);
                MapColor mapColor = world.getBlockState(pos).getMapColor(world, pos);
                if(mapColor != MapColor.AIR) {
                    color = mapColor.getMapColor(MathHelper.clamp(y - this.cachedYHeights[x][z == 0 ? 0 : z - 1], -1, 1) + 1);
                    this.cachedYHeights[x][z] = y;
                    break;
                }
                y--;
            }
            list.add(color);
            if(System.currentTimeMillis() - startTime >= 50) { //Timeout time
                finished = false;
                break;
            }
            position++;
        }
        if(finished && !this.isFinished) {
            for (Chunk chunk : this.chunksToUnload) {
                chunk.unloadQueued = true;
            }
        }
        this.isFinished = finished;
        JurassiCraft.NETWORK_WRAPPER.sendTo(new SyncTrackingTabletMap(list, offset), (EntityPlayerMP)this.entityPlayer);
    }

    public void markFinished() {
        this.isFinished = true;
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    @Mod.EventBusSubscriber(modid = JurassiCraft.MODID)
    @SuppressWarnings("unused")
    public static class Handler {

        private static final List<TrackingMapUploader> list = Lists.newArrayList();

        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event) {
            list.stream().peek(TrackingMapUploader::onWorldTick).filter(TrackingMapUploader::isFinished).collect(Collectors.toList()).forEach(list::remove);
        }
    }

}
