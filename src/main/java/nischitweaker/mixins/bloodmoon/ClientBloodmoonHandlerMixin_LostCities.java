package nischitweaker.mixins.bloodmoon;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import lumien.bloodmoon.client.ClientBloodmoonHandler;
import nischitweaker.features.LostCitiesDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientBloodmoonHandler.class)
public abstract class ClientBloodmoonHandlerMixin_LostCities {

    @ModifyExpressionValue(
            method = "clientTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldProvider;getDimension()I"),
            remap = false
    )
    private int nischitweaker_lostCitiesAsOverworld(int original) {
        return LostCitiesDimension.isLostCities(original) ? 0 : original;
    }
}
