package nischitweaker.mixins.reskillable;

import codersafterdark.reskillable.network.InvalidateRequirementPacket;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InvalidateRequirementPacket.class)
public abstract class InvalidateRequirementPacketMixin {

    @WrapOperation(method = "fromBytes", at = @At(value = "INVOKE", target = "Ljava/lang/Class;forName(Ljava/lang/String;)Ljava/lang/Class;"), remap = false)
    private Class<?> nischitweaker_dontInitialize(String name, Operation<Class<?>> original) throws ClassNotFoundException {
        return Class.forName(name, false, InvalidateRequirementPacket.class.getClassLoader());
    }
}
