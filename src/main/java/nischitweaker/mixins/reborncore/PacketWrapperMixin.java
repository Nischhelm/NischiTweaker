package nischitweaker.mixins.reborncore;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import reborncore.common.network.INetworkPacket;
import reborncore.common.network.PacketWrapper;

@Mixin(PacketWrapper.class)
public abstract class PacketWrapperMixin {

    @WrapOperation(method = "fromBytes", at = @At(value = "INVOKE", target = "Ljava/lang/Class;forName(Ljava/lang/String;)Ljava/lang/Class;"), remap = false)
    private Class<?> nischitweaker_onlyPacketClasses(String name, Operation<Class<?>> original) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(name, false, PacketWrapper.class.getClassLoader());
        if (!INetworkPacket.class.isAssignableFrom(clazz))
            throw new ClassNotFoundException("Not a RebornCore packet: " + name);
        return clazz;
    }
}
