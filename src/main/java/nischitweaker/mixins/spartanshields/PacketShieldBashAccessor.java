package nischitweaker.mixins.spartanshields;

import com.oblivioussp.spartanshields.network.PacketShieldBash;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PacketShieldBash.class)
public interface PacketShieldBashAccessor {
    @Accessor(remap = false) int getEntityId();
}
