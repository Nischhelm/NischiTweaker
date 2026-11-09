package nischitweaker.mixins.enchantertools;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.sirsquidly.enchanter_tools.common.items.ItemEnchantedInkwell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEnchantedInkwell.class)
public abstract class ItemEnchantedInkwellMixin {

    // Don't block further enchants once the inkwell is enchanted (enchanting table is still gated by isEnchantable)
    @ModifyReturnValue(method = "canApplyAtEnchantingTable", at = @At("RETURN"), remap = false)
    private boolean nischitweaker_allowMultipleEnchants(boolean original) {
        return true;
    }
}
