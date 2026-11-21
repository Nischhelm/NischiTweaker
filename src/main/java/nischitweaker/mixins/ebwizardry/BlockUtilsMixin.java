package nischitweaker.mixins.ebwizardry;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import electroblob.wizardry.util.BlockUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nischitweaker.config.ConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockUtils.class)
public abstract class BlockUtilsMixin {

    @ModifyReturnValue(method = "isBlockUnbreakable", at = @At("RETURN"), remap = false)
    private static boolean nischitweaker_addUnbreakableBlockList(boolean original, World world, BlockPos pos) {
        if (original) return true;
        return ConfigHandler.ebwizardry.isWizardryUnbreakable(world.getBlockState(pos).getBlock());
    }
}
