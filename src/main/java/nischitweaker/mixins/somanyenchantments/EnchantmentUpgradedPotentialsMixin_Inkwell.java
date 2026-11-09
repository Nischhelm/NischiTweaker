package nischitweaker.mixins.somanyenchantments;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.shultrea.rin.enchantments.EnchantmentUpgradedPotentials;
import com.sirsquidly.enchanter_tools.init.EnchanterToolsItems;
import net.minecraftforge.event.AnvilUpdateEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentUpgradedPotentials.class)
public abstract class EnchantmentUpgradedPotentialsMixin_Inkwell {

    // Skip SME's custom anvil handling for inkwells so vanilla merges the book normally
    @ModifyExpressionValue(
            method = "onAnvilUpdateEvent",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 0)
    )
    private boolean nischitweaker_skipInkwell(boolean leftIsEmpty, AnvilUpdateEvent event) {
        return leftIsEmpty || event.getLeft().getItem() == EnchanterToolsItems.ENCHANTED_INKWELL;
    }
}
