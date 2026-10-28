package nischitweaker.mixins.suikecherry;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.spongepowered.asm.mixin.Mixin;
import suike.suikecherry.packet.packets.HangingSignUpdatePacket;

@Mixin(HangingSignUpdatePacket.ServerHandler.class)
public abstract class HangingSignUpdatePacketMixin {

    @WrapMethod(
            method = "onMessage(Lsuike/suikecherry/packet/packets/HangingSignUpdatePacket;Lnet/minecraftforge/fml/common/network/simpleimpl/MessageContext;)Lnet/minecraftforge/fml/common/network/simpleimpl/IMessage;",
            remap = false
    )
    private IMessage nischitweaker_ignoreOnServer(HangingSignUpdatePacket message, MessageContext ctx, Operation<IMessage> original) {
        return null;
    }
}
