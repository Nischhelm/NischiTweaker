package nischitweaker.mixins.lostcities;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import mcjty.lostcities.ForgeEventHandlers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import nischitweaker.config.ConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(ForgeEventHandlers.class)
public abstract class ForgeEventHandlersMixin_PortalPhaseLock {

    @Inject(
            method = "onPlayerSleepInBedEvent",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/entity/player/PlayerSleepInBedEvent;setResult(Lnet/minecraftforge/fml/common/eventhandler/Event$Result;)V", ordinal = 1),
            remap = false,
            cancellable = true
    )
    private void nischitweaker_lockPortalBehindPhase(PlayerSleepInBedEvent event, CallbackInfo ci) {
        if (!SRPConfigSystems.useEvolution) return;
        int lockedPhase = ConfigHandler.lostcities.portalPhaseLock;
        if (lockedPhase < 0) return;

        EntityPlayer player = event.getEntityPlayer();
        if (SRPSaveDataInterface.get(player.getEntityWorld(), player, null).getEvolutionPhase(player.dimension) >= lockedPhase) return;

        player.sendStatusMessage(new TextComponentTranslation("nischitweaker.msg.fearincapacitates").setStyle(new Style().setColor(TextFormatting.RED)), true);

        Potion fearEffect = Potion.getPotionFromResourceLocation("lycanitesmobs:fear");
        if (fearEffect != null) player.addPotionEffect(new PotionEffect(fearEffect, 100, 0));

        // OTHER_PROBLEM sends no "can't sleep" message that would overwrite the fear message
        event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
        ci.cancel();
    }
}
