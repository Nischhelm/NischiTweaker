package nischitweaker.mixins.bloodmoon;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import lumien.bloodmoon.handler.BloodmoonEventHandler;
import nischitweaker.features.LostCitiesDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BloodmoonEventHandler.class)
public abstract class BloodmoonEventHandlerMixin_LostCities {

    @ModifyExpressionValue(
            method = "loadWorld",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldProvider;getDimension()I"),
            remap = false
    )
    private int nischitweaker_lostCitiesAsOverworld_loadWorld(int original) {
        return LostCitiesDimension.isLostCities(original) ? 0 : original;
    }

    @ModifyExpressionValue(
            method = "livingUpdate",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;dimension:I"),
            remap = false
    )
    private int nischitweaker_lostCitiesAsOverworld_livingUpdate(int original) {
        return LostCitiesDimension.isLostCities(original) ? 0 : original;
    }
}
