package nischitweaker.mixins.suikecherry;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import suike.suikecherry.packet.packets.OpenChestBoatPacket;

@Mixin(OpenChestBoatPacket.ServerHandler.class)
public abstract class OpenChestBoatPacketMixin {

    @WrapMethod(method = "lambda$onMessage$0", remap = false)
    private static void nischitweaker_onlyNearbyBoats(World world, OpenChestBoatPacket message, EntityPlayerMP player, Operation<Void> original) {
        Entity entity = world.getEntityByID(((OpenChestBoatPacketAccessor) message).getEntityID());
        if (entity != null && player.getDistanceSq(entity) > 16 * 16) return;
        original.call(world, message, player);
    }
}
