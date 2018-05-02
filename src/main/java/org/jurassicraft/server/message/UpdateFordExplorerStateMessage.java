package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.jurassicraft.server.entity.vehicle.FordExplorerEntity;

public class UpdateFordExplorerStateMessage extends AbstractMessage<UpdateFordExplorerStateMessage> {
    private int entityId;

    private FordExplorerEntity.StateType state;

    public UpdateFordExplorerStateMessage() {
    }

    public UpdateFordExplorerStateMessage(FordExplorerEntity entity) {
        this.entityId = entity.getEntityId();
        this.state = entity.getState().getType();
    }

    @Override
    public void onClientReceived(Minecraft minecraft, UpdateFordExplorerStateMessage message, EntityPlayer player, MessageContext context) {
        Entity entity = player.world.getEntityByID(message.entityId);
        if (entity instanceof FordExplorerEntity) {
            FordExplorerEntity vehicle = (FordExplorerEntity) entity;
            vehicle.setState(this.state.create(vehicle));
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, UpdateFordExplorerStateMessage message, EntityPlayer player, MessageContext context) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        int stateTypeIndex = buf.readUnsignedByte();
        FordExplorerEntity.StateType[] stateTypes = FordExplorerEntity.StateType.values();
        this.state = stateTypeIndex >= 0 && stateTypeIndex < stateTypes.length ? stateTypes[stateTypeIndex] : FordExplorerEntity.StateType.OFF_RAIL;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeByte(this.state.ordinal());
    }

    @Override
    public boolean registerOnSide(Side side) {
        return side.isClient();
    }
}
