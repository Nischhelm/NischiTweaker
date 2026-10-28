package nischitweaker.mixins.iceandfire;

import com.github.alexthe666.iceandfire.message.MessageDragonControl;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MessageDragonControl.class)
public abstract class MessageDragonControlMixin {

    @WrapMethod(method = "onServerReceived(Lnet/minecraft/server/MinecraftServer;Lcom/github/alexthe666/iceandfire/message/MessageDragonControl;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraftforge/fml/common/network/simpleimpl/MessageContext;)V", remap = false)
    private void nischitweaker_validateControl(MinecraftServer server, MessageDragonControl message, EntityPlayer player, MessageContext ctx, Operation<Void> original) {
        Entity entity = player.world.getEntityByID(message.dragonId);
        if (entity != null) {
            if (!entity.isPassenger(player) && !player.isPassenger(entity)) return;

            //Generous for fast flying mounts and lag
            MessageDragonControlAccessor accessor = (MessageDragonControlAccessor) message;
            if (entity.getDistanceSq(accessor.getPosX(), accessor.getPosY(), accessor.getPosZ()) > 32 * 32) {
                accessor.setPosX(entity.posX);
                accessor.setPosY(entity.posY);
                accessor.setPosZ(entity.posZ);
            }
        }
        original.call(server, message, player, ctx);
    }
}
