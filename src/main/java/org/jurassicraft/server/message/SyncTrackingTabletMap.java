package org.jurassicraft.server.message;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.jurassicraft.client.gui.TrackingTabletGui;

import java.util.List;

public class SyncTrackingTabletMap extends AbstractMessage<SyncTrackingTabletMap> {

    private List<Integer> list;

    private int offset;

    @SuppressWarnings("unused")
    public SyncTrackingTabletMap(){
    }

    public SyncTrackingTabletMap(List<Integer> list, int offset) {
        this.list = list;
        this.offset = offset;
    }

    @Override
    public void onClientReceived(Minecraft client, SyncTrackingTabletMap message, EntityPlayer player, MessageContext messageContext) {
        GuiScreen screen = client.currentScreen;
        if(screen instanceof TrackingTabletGui) {
            ((TrackingTabletGui)screen).upload(message.list, message.offset);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, SyncTrackingTabletMap message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.offset = buf.readInt();
        this.list = Lists.newArrayList();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            this.list.add(buf.readInt());
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.offset);
        buf.writeInt(this.list.size());
        this.list.forEach(buf::writeInt);
    }
}
