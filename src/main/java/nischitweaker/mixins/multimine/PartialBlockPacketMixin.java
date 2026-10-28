package nischitweaker.mixins.multimine;

import atomicstryker.multimine.common.network.PartialBlockPacket;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PartialBlockPacket.class)
public abstract class PartialBlockPacketMixin {

    @ModifyExpressionValue(
            method = "readBytes",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/network/ByteBufUtils;readUTF8String(Lio/netty/buffer/ByteBuf;)Ljava/lang/String;"),
            remap = false
    )
    private String nischitweaker_useActualSender(String user, @Local(argsOnly = true) ChannelHandlerContext ctx) {
        INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
        if (netHandler instanceof NetHandlerPlayServer)
            return ((NetHandlerPlayServer) netHandler).player.getName();
        return user;
    }
}
