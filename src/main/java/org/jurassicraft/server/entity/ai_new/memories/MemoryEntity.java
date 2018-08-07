package org.jurassicraft.server.entity.ai_new.memories;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class MemoryEntity extends MemoryBase {

    public Entity entity;
    public Vec3d position;

    public MemoryEntity(MemoryEntity memoryEntity)
    {
        super(memoryEntity);
    }

    public MemoryEntity(Entity entity)
    {
        super(1);
        this.entity = entity;
        this.uniqueKey = MemoryBase.createUniqueKeyEntity(entity);
        this.position = new Vec3d(entity.posX, entity.posY, entity.posZ);
    }
}
