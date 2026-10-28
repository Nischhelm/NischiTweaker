package nischitweaker.mixins.chunkpregenerator;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.server.permission.PermissionAPI;
import nischitweaker.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import pregenerator.base.api.network.PregenPacket;
import pregenerator.base.impl.networking.PacketHandler;
import pregenerator.impl.network.packets.DimRequestPacket;
import pregenerator.impl.network.packets.PermissionRequestPacket;
import pregenerator.impl.network.packets.RequestPacket;
import pregenerator.impl.network.packets.TrackerRequestPacket;
import pregenerator.impl.network.packets.chunkRequest.*;
import pregenerator.impl.network.packets.gui.*;
import pregenerator.impl.network.packets.retrogen.RetrogenChangePacket;
import pregenerator.impl.network.packets.retrogen.RetrogenCheckPacket;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Mixin(PacketHandler.class)
public abstract class PacketHandlerMixin {

    //Read-only overlay packets, the client sends these for everyone
    @Unique
    private static final Set<Class<?>> nischitweaker$unrestrictedPackets = new HashSet<>(Arrays.asList(
            PermissionRequestPacket.class, RequestPacket.class, TrackerRequestPacket.class, DimRequestPacket.class
    ));

    @Unique
    private static final Set<Class<?>> nischitweaker$restrictedPackets = new HashSet<>(Arrays.asList(
            ChunkRequest.class, TPChunkPacket.class, KillRequest.class, KillWorldRequest.class, EntityRequestPacket.class,
            StructureRequestPacket.class, RemoveStructurePacket.class, RetrogenCheckPacket.class, RetrogenChangePacket.class,
            PregenTaskPacket.class, MassPregenTaskPacket.class, DeletionTaskPacket.class, DimensionTaskPacket.class,
            ProcessRequestPacket.class, ManualTaskPacket.class
    ));

    @WrapMethod(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lpregenerator/base/api/network/PregenPacket;)V", remap = false)
    private void nischitweaker_checkServerPermission(ChannelHandlerContext ctx, PregenPacket msg, Operation<Void> original) {
        INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
        if (netHandler instanceof NetHandlerPlayServer && !nischitweaker$unrestrictedPackets.contains(msg.getClass())) {
            EntityPlayerMP player = ((NetHandlerPlayServer) netHandler).player;
            if (!nischitweaker$restrictedPackets.contains(msg.getClass()) || !PermissionAPI.hasPermission(player, "pregen-chunkview")) {
                Tags.LOGGER.warn("Blocked Chunk Pregenerator packet {} from {}", msg.getClass().getSimpleName(), player.getName());
                return;
            }
        }
        original.call(ctx, msg);
    }
}
