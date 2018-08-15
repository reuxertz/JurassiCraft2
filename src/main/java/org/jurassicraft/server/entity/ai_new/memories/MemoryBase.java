package org.jurassicraft.server.entity.ai_new.memories;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.ai_new.helpers.RandomHelper;
import org.jurassicraft.server.entity.ai_new.helpers.TickCounter;

public abstract class MemoryBase {

    public static String createUniqueKeyBlockPos(BlockPos pos)
    {
        return pos.getX() + "_" + pos.getY() + "_" + pos.getZ();
    }

    public static String createUniqueKeyBlockPos(Vec3d pos)
    {
        return pos.x + "_" + pos.y + "_" + pos.z;
    }

    public static String createUniqueKeyBlockState(Block block)
    {
        String blockKey = "";
        IProperty<?>[] properties = new IProperty<?>[block.getBlockState().getProperties().size()];
        block.getBlockState().getProperties().toArray(properties);
        for (int i = 0; i < properties.length; i++)
        {
            IProperty<?> property = properties[i];
            blockKey += property.getName();

            if (i < properties.length - 1)
                blockKey += "_";
        }

        return block.getUnlocalizedName() + "_" + blockKey;
    }

    public static String createUniqueKeyBlock(World world, BlockPos pos)
    {
        return "block_" + createUniqueKeyBlockPos(pos);// + "_" +
                //createUniqueKeyBlockState(world.getBlockState(pos).getBlock());

    }

    public static String createUniqueKeyEntity(Entity entity)
    {
        return "entity_" + entity.getPersistentID().toString();// + "_" +
        //createUniqueKeyBlockState(world.getBlockState(pos).getBlock());

    }

    protected String uniqueKey;
    protected double value;

    public double getValue()
    {
        return value;
    }

    public String getUniqueKey()
    {
        return uniqueKey;
    }

    public MemoryBase(MemoryBase memoryBase)
    {
        this.value = memoryBase.value;
    }
    public MemoryBase(double value)
    {
        this.value = value;
    }

    public void decay()
    {
        double decayFactor = .01 + RandomHelper.RND.nextDouble() * .01;
        value *= 1.0 - decayFactor;
    }

}
