package nischitweaker.mixins.srparasites;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPAdapted;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * SRP overrides despawnEntity with copies of the vanilla distance checks
 */
@Mixin({EntityParasiteBase.class, EntityPAdapted.class})
public abstract class EntityParasiteBaseMixin_CylindricalDespawn {
    @ModifyExpressionValue(
            method = "despawnEntity",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;posY:D", ordinal = 0)
    )
    private double nischitweaker_ignorePlayerY(double original) {
        return ((Entity) (Object) this).posY;
    }
}
