package nischitweaker.mixins.srparasites;

import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import lumien.bloodmoon.server.BloodmoonHandler;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import nischitweaker.config.ConfigHandler;
import nischitweaker.features.LostCitiesDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class SRPSpawningDimensionHandlerMixin_LostCitiesBloodmoonCap {

    @ModifyExpressionValue(
            method = "onSpawn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCap:I"),
            remap = false
    )
    private static int nischitweaker_lostCitiesBloodmoonMobCap(int original, @Local(argsOnly = true) LivingSpawnEvent.CheckSpawn event) {
        return nischitweaker$multiply(original, event);
    }

    @ModifyExpressionValue(
            method = "onSpawn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCapPlusPlayer:I"),
            remap = false
    )
    private static int nischitweaker_lostCitiesBloodmoonMobCapPerPlayer(int original, @Local(argsOnly = true) LivingSpawnEvent.CheckSpawn event) {
        return nischitweaker$multiply(original, event);
    }

    @Unique
    private static int nischitweaker$multiply(int mobCap, LivingSpawnEvent.CheckSpawn event) {
        if (!LostCitiesDimension.isLostCities(event.getWorld().provider.getDimension())) return mobCap;
        if (BloodmoonHandler.INSTANCE == null || !BloodmoonHandler.INSTANCE.isBloodmoonActive()) return mobCap;
        return (int) (mobCap * ConfigHandler.bloodmoon.lostCitiesParasiteCapMultiplier);
    }
}
