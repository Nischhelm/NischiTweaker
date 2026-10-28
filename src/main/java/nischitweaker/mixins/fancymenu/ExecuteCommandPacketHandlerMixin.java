package nischitweaker.mixins.fancymenu;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import de.keksuccino.fancymenu.networking.packets.command.execute.ClientboundExecuteCommandPacketMessageHandler;
import de.keksuccino.fancymenu.networking.packets.command.execute.ExecuteCommandPacketMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nischitweaker.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;
import java.util.List;

@Mixin(ClientboundExecuteCommandPacketMessageHandler.class)
public abstract class ExecuteCommandPacketHandlerMixin {

    //The only commands FancyMenus server side sends
    @Unique
    private static final List<String> nischitweaker$allowedCommands = Arrays.asList("/openguiscreen", "/closeguiscreen", "/fmvariable");

    @WrapMethod(
            method = "onMessage(Lde/keksuccino/fancymenu/networking/packets/command/execute/ExecuteCommandPacketMessage;Lnet/minecraftforge/fml/common/network/simpleimpl/MessageContext;)Lnet/minecraftforge/fml/common/network/simpleimpl/IMessage;",
            remap = false
    )
    private IMessage nischitweaker_onlyFancyMenuCommands(ExecuteCommandPacketMessage msg, MessageContext ctx, Operation<IMessage> original) {
        String command = msg.command == null ? "" : msg.command.split(" ", 2)[0];
        if (!nischitweaker$allowedCommands.contains(command)) {
            Tags.LOGGER.warn("Blocked server request to run command: {}", msg.command);
            return null;
        }
        return original.call(msg, ctx);
    }
}
