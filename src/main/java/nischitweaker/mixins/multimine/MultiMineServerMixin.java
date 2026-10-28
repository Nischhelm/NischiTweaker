package nischitweaker.mixins.multimine;

import atomicstryker.multimine.common.MultiMineServer;
import atomicstryker.multimine.common.PartiallyMinedBlock;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import nischitweaker.features.MultiMineProgressLimiter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(MultiMineServer.class)
public abstract class MultiMineServerMixin {

    @Shadow(remap = false) private List<PartiallyMinedBlock> getPartiallyMinedBlocksForDimension(int dim) { return null; }

    @WrapMethod(method = "onClientSentPartialBlockPacket", remap = false)
    private void nischitweaker_validateProgress(EntityPlayerMP player, int x, int y, int z, float value, Operation<Void> original) {
        if (!nischitweaker$isInReach(player, x, y, z)) return;

        BlockPos pos = new BlockPos(x, y, z);
        float relativeHardness = player.world.getBlockState(pos).getPlayerRelativeBlockHardness(player, player.world, pos);
        if (relativeHardness <= 0) return;

        float storedProgress = 0;
        List<PartiallyMinedBlock> blocks = this.getPartiallyMinedBlocksForDimension(player.dimension);
        if (blocks != null) {
            for (PartiallyMinedBlock block : blocks) {
                if (block.getPos().equals(pos)) {
                    storedProgress = block.getProgress();
                    break;
                }
            }
        }

        original.call(player, x, y, z, MultiMineProgressLimiter.limit(player, pos, storedProgress, relativeHardness, value));
    }

    //Same reach check as vanilla digging, with some more leeway for lag
    @Unique
    private static boolean nischitweaker$isInReach(EntityPlayerMP player, int x, int y, int z) {
        double dx = player.posX - (x + 0.5);
        double dy = player.posY - (y + 0.5) + 1.5;
        double dz = player.posZ - (z + 0.5);
        double reach = player.interactionManager.getBlockReachDistance() + 16;
        return dx * dx + dy * dy + dz * dz <= reach * reach;
    }
}
