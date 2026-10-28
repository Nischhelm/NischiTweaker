package nischitweaker.mixins.iceandfire;

import com.github.alexthe666.iceandfire.message.MessageDragonControl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MessageDragonControl.class)
public interface MessageDragonControlAccessor {
    @Accessor(remap = false) double getPosX();
    @Accessor(remap = false) double getPosY();
    @Accessor(remap = false) double getPosZ();
    @Accessor(remap = false) void setPosX(double posX);
    @Accessor(remap = false) void setPosY(double posY);
    @Accessor(remap = false) void setPosZ(double posZ);
}
