package org.jurassicraft.server.entity;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import org.jurassicraft.server.entity.dinosaur.DinosaurEntity;

public class DinosaurSerializers {
    public static final DataSerializer<DinosaurEntity.Order> ORDER = new DataSerializer<DinosaurEntity.Order>() {
        @Override
        public void write(PacketBuffer buf, DinosaurEntity.Order value) {
            buf.writeByte((value == null ? DinosaurEntity.Order.WANDER : value).ordinal());
        }

        @Override
        public DinosaurEntity.Order read(PacketBuffer buf) {
            return DinosaurEntity.Order.values()[buf.readByte()];
        }

        @Override
        public DataParameter<DinosaurEntity.Order> createKey(int id) {
            return new DataParameter<>(id, this);
        }

        @Override
        public DinosaurEntity.Order copyValue(DinosaurEntity.Order value)
        {
            return null;
        }
    };

    public static void register() {
        DataSerializers.registerSerializer(ORDER);
    }
}
