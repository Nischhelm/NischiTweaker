package nischitweaker.mixins.bloodmoon;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPWorldEntitySpawner;
import com.llamalad7.mixinextras.sugar.Local;
import lumien.bloodmoon.server.BloodmoonHandler;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.SRPMixinsPlugin;

@Mixin(BloodmoonHandler.class)
public abstract class BloodmoonHandlerMixin_LostCitiesParasites {

    /**
     * Runs the SRP custom spawner alongside the blood moon spawner. With SRPMixins "Fix Spawning Entirely", SRPMixins handles parasites in the blood moon spawner itself
     */
    @Inject(
            method = "endWorldTick",
            at = @At(value = "INVOKE", target = "Llumien/bloodmoon/server/BloodmoonSpawner;findChunksForSpawning(Lnet/minecraft/world/WorldServer;ZZZ)I"),
            remap = false
    )
    private void nischitweaker_spawnParasites(TickEvent.WorldTickEvent event, CallbackInfo ci, @Local World world) {
        if (world.isRemote || !SRPConfigSystems.useEvolution || !SRPConfigSystems.phaseCustomSpawner) return;
        if (SRPMixinsPlugin.isFixSpawningEntirelyEnabled()) return;
        SRPWorldEntitySpawner.findChunksForSpawning((WorldServer) world, true, false, world.getTotalWorldTime() % 400L == 0L);
    }
}
