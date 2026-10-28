package nischitweaker.mixins.iceandfire;

import com.github.alexthe666.iceandfire.message.MessageDragonSyncFire;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MessageDragonSyncFire.class)
public abstract class MessageDragonSyncFireMixin {

    @WrapMethod(method = "onServerReceived(Lnet/minecraft/server/MinecraftServer;Lcom/github/alexthe666/iceandfire/message/MessageDragonSyncFire;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraftforge/fml/common/network/simpleimpl/MessageContext;)V", remap = false)
    private void nischitweaker_ignoreOnServer(MinecraftServer server, MessageDragonSyncFire message, EntityPlayer player, MessageContext ctx, Operation<Void> original) {
        //no op
    }
}
