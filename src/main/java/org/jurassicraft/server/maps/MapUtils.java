package org.jurassicraft.server.maps;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Random;

public class MapUtils {

    private static final int MIN_DISTANCE = 5000;
    private static final int MAX_DISTANCE = 10000;

    public static BlockPos getVisitorCenterPosition() {
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
        Random worldRandom = new Random(world.getSeed());
        int range = MAX_DISTANCE - MIN_DISTANCE;
        BlockPos pos = new BlockPos(worldRandom.nextInt(range) + MIN_DISTANCE, 0, worldRandom.nextInt(range) + MIN_DISTANCE);
//        System.out.println(pos);
        return pos;
    }
}
