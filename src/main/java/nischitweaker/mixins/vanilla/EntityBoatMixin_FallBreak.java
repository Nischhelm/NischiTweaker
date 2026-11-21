package nischitweaker.mixins.vanilla;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.item.EntityBoat;
import nischitweaker.config.ConfigHandler;
import nischitweaker.config.folders.VanillaConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityBoat.class)
public abstract class EntityBoatMixin_FallBreak {

    @Shadow protected abstract EntityBoat.Status getBoatStatus();

    /**
     * The status is calculated before moving, so a landing boat is only ON_LAND if the previous tick ended exactly on the ground (MC-119369).
     * NO_BREAK treats it as never on land, ALWAYS_BREAK uses the status after the move.
     */
    @Definition(id = "status", field = "Lnet/minecraft/entity/item/EntityBoat;status:Lnet/minecraft/entity/item/EntityBoat$Status;")
    @Definition(id = "ON_LAND", field = "Lnet/minecraft/entity/item/EntityBoat$Status;ON_LAND:Lnet/minecraft/entity/item/EntityBoat$Status;")
    @Expression("this.status != ON_LAND")
    @ModifyExpressionValue(method = "updateFallState", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean nischitweaker_fixLandingStatus(boolean notOnLand) {
        if (ConfigHandler.vanilla.boatFallBreak == VanillaConfig.BoatFallBreak.ALWAYS_BREAK)
            return this.getBoatStatus() != EntityBoat.Status.ON_LAND; //recalcs the status
        return true;
    }
}
