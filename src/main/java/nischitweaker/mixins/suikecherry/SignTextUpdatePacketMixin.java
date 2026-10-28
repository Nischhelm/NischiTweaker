package nischitweaker.mixins.suikecherry;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.spongepowered.asm.mixin.Mixin;
import suike.suikecherry.packet.packets.SignTextUpdatePacket;

@Mixin(SignTextUpdatePacket.ServerHandler.class)
public abstract class SignTextUpdatePacketMixin {

    @WrapMethod(
            method = "onMessage(Lsuike/suikecherry/packet/packets/SignTextUpdatePacket;Lnet/minecraftforge/fml/common/network/simpleimpl/MessageContext;)Lnet/minecraftforge/fml/common/network/simpleimpl/IMessage;",
            remap = false
    )
    private IMessage nischitweaker_onlyNearbySigns(SignTextUpdatePacket message, MessageContext ctx, Operation<IMessage> original) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        BlockPos pos = ((SignTextUpdatePacketAccessor) message).getPos();
        if (player.getDistanceSqToCenter(pos) > 16 * 16) return null;
        return original.call(message, ctx);
    }
}
