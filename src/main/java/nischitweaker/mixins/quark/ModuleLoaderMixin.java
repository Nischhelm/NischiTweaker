package nischitweaker.mixins.quark;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleLoader;

import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(ModuleLoader.class)
public abstract class ModuleLoaderMixin {

    @Shadow(remap = false) @Final @Mutable
    public static Map<Class<? extends Module>, Module> moduleInstances;

    // moduleInstances is a HashMap keyed by Class (identity hash) -> module preInit order, and thereby block/item registration order, is random per launch
    @Inject(method = "preInit", at = @At("HEAD"), remap = false)
    private static void nischitweaker_deterministicModuleOrder(FMLPreInitializationEvent event, CallbackInfo ci) {
        moduleInstances = new LinkedHashMap<>(moduleInstances);
    }
}
