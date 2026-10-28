package nischitweaker.mixins.iceandfire;

import com.github.alexthe666.iceandfire.message.MessagePlayerHitMultipart;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MessagePlayerHitMultipart.class)
public abstract class MessagePlayerHitMultipartMixin {

    @ModifyExpressionValue(
            method = "onServerReceived(Lnet/minecraft/server/MinecraftServer;Lcom/github/alexthe666/iceandfire/message/MessagePlayerHitMultipart;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraftforge/fml/common/network/simpleimpl/MessageContext;)V",
            at = @At(value = "CONSTANT", args = "doubleValue=100.0"),
            remap = false
    )
    private double nischitweaker_lowerRange(double original) {
        return 32.0;
    }
}
