package nischitweaker.mixins.iceandfire;

import com.github.alexthe666.iceandfire.message.MessageDragonArmor;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MessageDragonArmor.class)
public abstract class MessageDragonArmorMixin {

    @WrapMethod(method = "onServerReceived(Lnet/minecraft/server/MinecraftServer;Lcom/github/alexthe666/iceandfire/message/MessageDragonArmor;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraftforge/fml/common/network/simpleimpl/MessageContext;)V", remap = false)
    private void nischitweaker_ignoreOnServer(MinecraftServer server, MessageDragonArmor message, EntityPlayer player, MessageContext ctx, Operation<Void> original) {
        //no op
    }
}
