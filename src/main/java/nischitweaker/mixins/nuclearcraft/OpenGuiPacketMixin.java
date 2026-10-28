package nischitweaker.mixins.nuclearcraft;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import nc.network.gui.OpenGuiPacket;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nischitweaker.features.NuclearCraftPacketRange;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(OpenGuiPacket.Handler.class)
public abstract class OpenGuiPacketMixin {

    @WrapMethod(method = "processMessage", remap = false)
    private void nischitweaker_onlyNearbyGuis(OpenGuiPacket message, MessageContext ctx, Operation<Void> original) {
        if (!NuclearCraftPacketRange.isInRange(ctx.getServerHandler().player, ((OpenGuiPacketAccessor) message).getPos())) return;
        original.call(message, ctx);
    }
}
