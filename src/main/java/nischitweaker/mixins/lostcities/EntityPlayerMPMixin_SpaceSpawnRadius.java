package nischitweaker.mixins.lostcities;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldProvider;
import nischitweaker.features.LostCitiesSpaceSpawn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPlayerMP.class)
public abstract class EntityPlayerMPMixin_SpaceSpawnRadius {

    /**
     * Wrapping the call site instead of WorldProvider.getRandomizedSpawnPoint, since Nether API replaces that method body via ASM
     */
    @WrapOperation(
            method = "<init>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldProvider;getRandomizedSpawnPoint()Lnet/minecraft/util/math/BlockPos;"),
            remap = false
    )
    private BlockPos nischitweaker_lostCitiesSpaceSpawnRadius(WorldProvider provider, Operation<BlockPos> original) {
        BlockPos pos = LostCitiesSpaceSpawn.getRandomizedSpawnPoint(((EntityPlayerMP) (Object) this).world);
        return pos != null ? pos : original.call(provider);
    }
}
