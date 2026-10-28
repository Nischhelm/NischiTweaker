package nischitweaker.mixins.iceandfire;

import com.github.alexthe666.iceandfire.message.MessageMultipartInteract;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MessageMultipartInteract.class)
public abstract class MessageMultipartInteractMixin {

    @WrapMethod(method = "onServerReceived(Lnet/minecraft/server/MinecraftServer;Lcom/github/alexthe666/iceandfire/message/MessageMultipartInteract;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraftforge/fml/common/network/simpleimpl/MessageContext;)V", remap = false)
    private void nischitweaker_validateInteract(MinecraftServer server, MessageMultipartInteract message, EntityPlayer player, MessageContext ctx, Operation<Void> original) {
        Entity entity = player.world.getEntityByID(message.creatureID);
        if (entity instanceof EntityLivingBase) {
            //Multipart hitboxes reach outside the main hitbox, so be generous with the parent size and lag
            double reach = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + entity.width * 2 + 16;
            if (player.getDistanceSq(entity) > reach * reach) return;

            if (message.attack) {
                float weaponDamage = (float) player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
                message.dmg = Math.min(message.dmg, weaponDamage * 10F);
            }
        }
        original.call(server, message, player, ctx);
    }
}
