package org.jurassicraft.server.entity.ai_new.helpers;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static org.jurassicraft.server.util.RandomHelper.RND;

public class MovementHelper {


    public static boolean isBelowWaterLine(World world, Vec3d minVec, Vec3d maxVec)
    {
        boolean isAllWater = true;

        double minXd = minVec.x - 1;
        double maxXd = maxVec.x - 1;
        double minZd = minVec.z - 1;
        double maxZd = maxVec.z - 1;

        int minX = (int) minXd;
        int maxX = (int) (maxXd);
        int minZ = (int) minZd;
        int maxZ = (int) (maxZd);

        for (int x = minX; x <= maxX; x++)
        {
            for (int z = minZ; z <= maxZ; z++) {
                double yd = minVec.y;
                double y = Math.floor(yd + 1);
                IBlockState blockState = world.getBlockState(new BlockPos((int) x, y - 1, (int) z));
                Material blockMaterial = blockState.getMaterial();

                boolean isNonSolid = blockMaterial instanceof MaterialLiquid;

                if (!isNonSolid) {
                    isAllWater = false;
                    break;
                }
            }
        }

        if (!isAllWater)
        {
            int a = 9;
        }

        if (isAllWater)
        {
            int a = 9;
        }


        //double ey = entity.posY;
        //double by = entity.world.getBlockState();

        return isAllWater;
    }

    public static boolean isBelowWaterLine(Entity entity)
    {
        boolean isAllWater = true;

        double minXd = entity.getEntityBoundingBox().minX - 1;
        double maxXd = entity.getEntityBoundingBox().maxX - 1;
        double minZd = entity.getEntityBoundingBox().minZ - 1;
        double maxZd = entity.getEntityBoundingBox().maxZ - 1;

        int minX = (int) minXd;
        int maxX = (int) (maxXd);
        int minZ = (int) minZd;
        int maxZ = (int) (maxZd);

        for (int x = minX; x <= maxX; x++)
        {
            for (int z = minZ; z <= maxZ; z++) {
                double yd = entity.getEntityBoundingBox().minY;
                double y = Math.floor(yd + 1);
                IBlockState blockState = entity.world.getBlockState(new BlockPos((int) x, y - 1, (int) z));
                Material blockMaterial = blockState.getMaterial();

                boolean isNonSolid = blockMaterial instanceof MaterialLiquid;

                if (!isNonSolid) {
                    isAllWater = false;
                    break;
                }
            }
        }

        if (!isAllWater)
        {
            int a = 9;
        }

        if (isAllWater)
        {
            int a = 9;
        }


        //double ey = entity.posY;
        //double by = entity.world.getBlockState();

        return isAllWater;
    }

    public static boolean isAtPosition(Vec3d v1, Vec3d v2, double threshold)
    {
        boolean isAtPosition = Math.abs(v1.x - v2.x) < threshold &&
                Math.abs(v1.y - v2.y) < threshold &&
                Math.abs(v1.y - v2.y) < threshold;

        return isAtPosition;
    }
    public static boolean isAtPosition(double x, double y, double z, Vec3d v2, double threshold)
    {
        boolean isAtPosition = Math.abs(x - v2.x) < threshold &&
                Math.abs(y - v2.y) < threshold &&
                Math.abs(z - v2.z) < threshold;

        return isAtPosition;
    }
    public static boolean isAtPosition(double x1, double y1, double z1, double x2, double y2, double z2, double threshold) {
        double distanceSum = 0.0;

        double xDif = Math.abs(x1 - x2);
        double yDif = Math.abs(y1 - y2);
        double zDif = Math.abs(z1 - z2);

        distanceSum += xDif;
        distanceSum += yDif;
        distanceSum += zDif;

        boolean result = distanceSum < threshold;

        return result;
    }

    public static Vec3d getRandomPosition(Entity entity, double xz, double y, boolean landOnly)
    {
        World world = entity.world;
        Vec3d position = entity.getPositionVector();
        double xRand = (xz * RND.nextDouble()) * ((RND.nextInt(2) * 2) - 1);
        double zRand = (xz * RND.nextDouble()) * ((RND.nextInt(2) * 2) - 1);
        double yRand = (y * RND.nextDouble()) * ((RND.nextInt(2) * 2) - 1);

        if (landOnly)
        {
            BlockPos blockPos = new BlockPos((int)(position.x + xRand), (int)(position.y + yRand), (int)(position.z + zRand));
            IBlockState blockState = world.getBlockState(blockPos);
            boolean isLiquid = blockState.getMaterial().isLiquid();

            if (isLiquid)
            {
                while (blockState.getMaterial().isLiquid())
                {
                    blockPos = new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ());
                    blockState = world.getBlockState(blockPos);
                }
            }

            return new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }

        return new Vec3d(position.x + xRand, position.y + yRand, position.z + zRand);
    }
}
