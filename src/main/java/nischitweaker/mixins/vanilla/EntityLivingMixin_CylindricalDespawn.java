package nischitweaker.mixins.vanilla;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityLiving.class)
public abstract class EntityLivingMixin_CylindricalDespawn {
    /**
     * Ignore the vertical distance to the closest player when checking for despawning
     */
    @ModifyExpressionValue(
            method = "despawnEntity",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;posY:D", ordinal = 0)
    )
    private double nischitweaker_ignorePlayerY(double original) {
        return ((Entity) (Object) this).posY;
    }
}
