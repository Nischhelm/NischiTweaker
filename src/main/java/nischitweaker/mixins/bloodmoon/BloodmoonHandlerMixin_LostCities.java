package nischitweaker.mixins.bloodmoon;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import lumien.bloodmoon.network.PacketHandler;
import lumien.bloodmoon.network.messages.MessageBloodmoonStatus;
import lumien.bloodmoon.server.BloodmoonHandler;
import mcjty.lostcities.config.LostCityConfiguration;
import nischitweaker.features.LostCitiesDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BloodmoonHandler.class)
public abstract class BloodmoonHandlerMixin_LostCities {
    @Shadow(remap = false) boolean bloodMoon;

    @ModifyExpressionValue(
            method = "endWorldTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldProvider;getDimension()I"),
            remap = false
    )
    private int nischitweaker_lostCitiesAsOverworld(int original) {
        return LostCitiesDimension.isLostCities(original) ? 0 : original;
    }

    /**
     * Lets the time == 12000 branch run in Lost Cities as well, which sends the blood moon message
     */
    @ModifyExpressionValue(
            method = "endWorldTick",
            at = @At(value = "INVOKE", target = "Llumien/bloodmoon/server/BloodmoonHandler;isBloodmoonActive()Z"),
            remap = false
    )
    private boolean nischitweaker_skipFirstBloodmoonTick(boolean original, @Local int time) {
        return time != 12000 && original;
    }

    @Inject(
            method = "updateClients",
            at = @At(value = "HEAD"),
            remap = false
    )
    private void nischitweaker_updateLostCitiesClients(CallbackInfo ci) {
        if (LostCityConfiguration.DIMENSION_ID != -1)
            PacketHandler.INSTANCE.sendToDimension(new MessageBloodmoonStatus(this.bloodMoon), LostCityConfiguration.DIMENSION_ID);
    }

    @Inject(
            method = "setBloodmoon",
            at = @At(value = "HEAD"),
            remap = false
    )
    private void nischitweaker_setLostCitiesBloodmoon(boolean bloodMoon, CallbackInfo ci) {
        if (this.bloodMoon != bloodMoon && LostCityConfiguration.DIMENSION_ID != -1)
            PacketHandler.INSTANCE.sendToDimension(new MessageBloodmoonStatus(bloodMoon), LostCityConfiguration.DIMENSION_ID);
    }
}
