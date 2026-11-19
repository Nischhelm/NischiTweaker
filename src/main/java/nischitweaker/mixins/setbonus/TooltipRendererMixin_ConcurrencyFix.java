package nischitweaker.mixins.setbonus;

import com.fantasticsource.setbonus.client.TooltipRenderer;
import com.fantasticsource.setbonus.common.Bonus;
import com.fantasticsource.setbonus.common.bonusrequirements.setrequirement.Set;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;

@Mixin(TooltipRenderer.class)
public abstract class TooltipRendererMixin_ConcurrencyFix {
    @ModifyExpressionValue(
            method = "tooltips",
            at = @At(value = "FIELD", target = "Lcom/fantasticsource/setbonus/SetBonusData;sets:Ljava/util/LinkedHashSet;"),
            remap = false
    )
    private static LinkedHashSet<Set> nischitweaker_copySets(LinkedHashSet<Set> original) {
        return nischitweaker$copy(original);
    }

    @ModifyExpressionValue(
            method = "tooltips",
            at = @At(value = "FIELD", target = "Lcom/fantasticsource/setbonus/SetBonusData;bonuses:Ljava/util/LinkedHashSet;"),
            remap = false
    )
    private static LinkedHashSet<Bonus> nischitweaker_copyBonuses(LinkedHashSet<Bonus> original) {
        return nischitweaker$copy(original);
    }

    // SetBonus' JEI tooltip reload thread runs this while the client thread can still change the sets
    @Unique
    private static <T> LinkedHashSet<T> nischitweaker$copy(LinkedHashSet<T> original) {
        while (true) {
            try {
                LinkedHashSet<T> copy = new LinkedHashSet<>(original);
                if (copy.size() == original.size()) return copy;
            } catch (ConcurrentModificationException ignored) {
            }
        }
    }
}
