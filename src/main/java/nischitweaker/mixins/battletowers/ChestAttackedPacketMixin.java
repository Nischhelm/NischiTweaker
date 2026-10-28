package nischitweaker.mixins.battletowers;

import atomicstryker.battletowers.common.network.ChestAttackedPacket;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChestAttackedPacket.class)
public abstract class ChestAttackedPacketMixin {

    @ModifyExpressionValue(
            method = "readBytes",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;valueOf([C)Ljava/lang/String;"),
            remap = false
    )
    private String nischitweaker_useActualSender(String playerName, @Local(argsOnly = true) ChannelHandlerContext ctx) {
        INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
        if (netHandler instanceof NetHandlerPlayServer)
            return ((NetHandlerPlayServer) netHandler).player.getName();
        return playerName;
    }
}
