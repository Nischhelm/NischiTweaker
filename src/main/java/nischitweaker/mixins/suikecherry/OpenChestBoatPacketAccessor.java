package nischitweaker.mixins.suikecherry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import suike.suikecherry.packet.packets.OpenChestBoatPacket;

@Mixin(OpenChestBoatPacket.class)
public interface OpenChestBoatPacketAccessor {
    @Accessor(remap = false) int getEntityID();
}
