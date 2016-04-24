package org.jurassicraft.server.entity;

import net.minecraft.world.World;
import org.jurassicraft.server.entity.base.AggressiveDinosaurEntity;

public class CarnotaurusEntity extends AggressiveDinosaurEntity
{
    public CarnotaurusEntity(World world)
    {
        super(world);
    }

    @Override
    public int getTailBoxCount()
    {
        return 6;
    }
}
