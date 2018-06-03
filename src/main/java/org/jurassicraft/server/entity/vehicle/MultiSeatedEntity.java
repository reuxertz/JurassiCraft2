package org.jurassicraft.server.entity.vehicle;

import net.minecraft.entity.Entity;

public interface MultiSeatedEntity {
    boolean tryPutInSeat(Entity passenger, int seatID);
}
