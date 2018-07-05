package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jurassicraft.server.util.TrackingMapIterator;

public class StopMapSyncMessage extends AbstractMessage<StopMapSyncMessage> {
    @Override
    public void onClientReceived(Minecraft client, StopMapSyncMessage message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void onServerReceived(MinecraftServer server, StopMapSyncMessage message, EntityPlayer player, MessageContext messageContext) {
        if(TrackingMapIterator.playerMap.containsKey(player)) {
            TrackingMapIterator.playerMap.get(player).markFinished();
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }
}
