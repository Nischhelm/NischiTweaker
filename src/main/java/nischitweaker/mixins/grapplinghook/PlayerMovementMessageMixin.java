package nischitweaker.mixins.grapplinghook;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.yyon.grapplinghook.network.PlayerMovementMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nischitweaker.Tags;
import nischitweaker.features.GrappleMovementLimiter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerMovementMessage.Handler.runner.class)
public abstract class PlayerMovementMessageMixin {

    @Shadow(remap = false) PlayerMovementMessage message;
    @Shadow(remap = false) MessageContext ctx;

    @WrapMethod(method = "run", remap = false)
    private void nischitweaker_limitMovement(Operation<Void> original) {
        EntityPlayerMP player = this.ctx.getServerHandler().player;
        //original ignores other entities anyway
        if (this.message.entityId == player.getEntityId() && !GrappleMovementLimiter.tryMove(player, this.message.x, this.message.y, this.message.z)) {
            Tags.LOGGER.warn("Blocked grapple movement of {} moving too fast", player.getName());
            return;
        }
        original.call();
    }
}
