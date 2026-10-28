package nischitweaker.mixins.quark;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nischitweaker.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.quark.base.network.message.MessageRequestEmote;

@Mixin(MessageRequestEmote.class)
public abstract class MessageRequestEmoteMixin {

    @Shadow(remap = false) public String emoteName;

    @WrapMethod(method = "handleMessage", remap = false)
    private IMessage nischitweaker_validateEmoteName(MessageContext context, Operation<IMessage> original) {
        if (this.emoteName == null || this.emoteName.contains("/") || this.emoteName.contains("\\") || this.emoteName.contains("..")) {
            Tags.LOGGER.warn("Blocked emote request with invalid name {} from {}", this.emoteName, context.getServerHandler().player.getName());
            return null;
        }
        return original.call(context);
    }
}
