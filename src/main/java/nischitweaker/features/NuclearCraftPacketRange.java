package nischitweaker.features;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

public class NuclearCraftPacketRange {
    //Multiblock GUIs (reactors, turbines) point at the controller, which can be far from the part the player clicked
    private static final double MAX_DISTANCE = 64;

    public static boolean isInRange(EntityPlayerMP player, BlockPos pos) {
        return player.world.isBlockLoaded(pos) && player.getDistanceSqToCenter(pos) <= MAX_DISTANCE * MAX_DISTANCE;
    }
}
