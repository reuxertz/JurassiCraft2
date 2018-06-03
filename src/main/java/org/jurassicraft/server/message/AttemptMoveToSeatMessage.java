package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jurassicraft.server.entity.vehicle.MultiSeatedEntity;

public class AttemptMoveToSeatMessage extends AbstractMessage<AttemptMoveToSeatMessage> {

    private int seat;

    @SuppressWarnings("unused")
    public AttemptMoveToSeatMessage(){}

    public AttemptMoveToSeatMessage(int seat){
        this.seat = seat;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(seat);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        seat = buf.readInt();
    }

    @Override
    public void onClientReceived(Minecraft client, AttemptMoveToSeatMessage message, EntityPlayer player, MessageContext messageContext) {}

    @Override
    public void onServerReceived(MinecraftServer server, AttemptMoveToSeatMessage message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.getRidingEntity();
        if(entity instanceof MultiSeatedEntity) {
            ((MultiSeatedEntity)entity).tryPutInSeat(player, message.seat);
        }
    }
}