package nischitweaker.features;

import mcjty.lostcities.config.LostCityProfile;
import mcjty.lostcities.dimensions.world.LostCityChunkGenerator;
import mcjty.lostcities.dimensions.world.WorldTypeTools;
import mcjty.lostcities.dimensions.world.lost.BuildingInfo;
import mcjty.lostcities.dimensions.world.lost.CitySphere;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class LostCitiesSpaceSpawn {

    private static final int ATTEMPTS = 64;

    /**
     * Random position within the spawnRadius gamerule around the world spawn, inside a city sphere and below its dome.
     * Returns null if the world isn't a Lost Cities space world or no valid position was found.
     */
    @Nullable
    public static BlockPos getRandomizedSpawnPoint(World world) {
        if (world.isRemote || !WorldTypeTools.isLostCities(world)) return null;
        if (world.getWorldInfo().getGameType() == GameType.ADVENTURE) return null;

        LostCityProfile profile = WorldTypeTools.getProfile(world);
        if (profile == null || !profile.isSpace()) return null;
        LostCityChunkGenerator provider = WorldTypeTools.getLostCityChunkGenerator(world);
        if (provider == null) return null;

        BlockPos spawn = world.getSpawnPoint();
        int radius = Math.max(0, world.getGameRules().getInt("spawnRadius")) / 2;
        radius = Math.min(radius, MathHelper.floor(world.getWorldBorder().getClosestDistance(spawn.getX(), spawn.getZ())));
        if (radius <= 0) return null;

        for (int i = 0; i < ATTEMPTS; i++) {
            int x = spawn.getX() + world.rand.nextInt(2 * radius + 1) - radius;
            int z = spawn.getZ() + world.rand.nextInt(2 * radius + 1) - radius;

            CitySphere sphere = CitySphere.getCitySphere(x >> 4, z >> 4, provider);
            if (!sphere.isEnabled()) continue;

            // Same 80% margin Lost Cities uses for spawnSphere=<in>
            BlockPos center = sphere.getCenterPos();
            double innerRadius = sphere.getRadius() * 0.8;
            double dx = x - center.getX();
            double dz = z - center.getZ();
            if (dx * dx + dz * dz >= innerRadius * innerRadius) continue;

            if (profile.SPAWN_NOT_IN_BUILDING) {
                BuildingInfo info = BuildingInfo.getBuildingInfo(x >> 4, z >> 4, provider);
                if (info.isCity() && info.hasBuilding) continue;
            }

            // Bottom-up from below ground level like Lost Cities' own spawn search, stop once head height would leave the dome
            double sqRadius = sphere.getRadius() * sphere.getRadius();
            for (int y = Math.max(1, center.getY() - 5); y < world.getHeight() - 2; y++) {
                BlockPos pos = new BlockPos(x, y, z);
                if (pos.up(2).distanceSq(center) >= sqRadius) break;
                if (isValidStandingPosition(world, pos)) return pos.up();
            }
        }
        return null;
    }

    private static boolean isValidStandingPosition(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().isTopSolid(state)
                && state.getBlock().isFullCube(state)
                && state.getBlock().isOpaqueCube(state)
                && world.isAirBlock(pos.up())
                && world.isAirBlock(pos.up(2));
    }
}
