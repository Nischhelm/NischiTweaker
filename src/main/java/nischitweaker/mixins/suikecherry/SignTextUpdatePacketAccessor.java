package nischitweaker.mixins.suikecherry;

import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import suike.suikecherry.packet.packets.SignTextUpdatePacket;

@Mixin(SignTextUpdatePacket.class)
public interface SignTextUpdatePacketAccessor {
    @Accessor(remap = false) BlockPos getPos();
}
