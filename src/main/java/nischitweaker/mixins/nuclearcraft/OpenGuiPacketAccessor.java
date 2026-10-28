package nischitweaker.mixins.nuclearcraft;

import nc.network.gui.OpenGuiPacket;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(OpenGuiPacket.class)
public interface OpenGuiPacketAccessor {
    @Accessor(remap = false) BlockPos getPos();
}
