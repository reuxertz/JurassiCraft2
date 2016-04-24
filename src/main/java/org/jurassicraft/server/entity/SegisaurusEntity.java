package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.AggressiveDinosaurEntity;

public class SegisaurusEntity extends AggressiveDinosaurEntity
{
    public SegisaurusEntity(World world)
    {
        super(world);
    }

    @Override
    public int getTailBoxCount()
    {
        return 5;
    }
}
