package nischitweaker.mixins.spartanshields;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.oblivioussp.spartanshields.network.PacketShieldBash;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PacketShieldBash.class)
public abstract class PacketShieldBashMixin {

    @WrapMethod(method = "handleServerSide(Lcom/oblivioussp/spartanshields/network/PacketShieldBash;Lnet/minecraft/entity/player/EntityPlayerMP;)V", remap = false)
    private void nischitweaker_onlyNearbyTargets(PacketShieldBash message, EntityPlayerMP player, Operation<Void> original) {
        if (message != null && player != null) {
            Entity victim = player.world.getEntityByID(((PacketShieldBashAccessor) message).getEntityId());
            if (victim != null) {
                double range = 8 + victim.width;
                if (player.getDistanceSq(victim) > range * range) return;
            }
        }
        original.call(message, player);
    }
}
