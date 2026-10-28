package nischitweaker;

import fermiumbooter.FermiumRegistryAPI;
import fermiumbooter.util.FermiumJarScanner;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

public class MixinUtil {

    public static boolean isDev() {
        return FMLLaunchHandler.isDeobfuscatedEnvironment();
    }

    public static void conditionallyAddMixin(String mixinJson, Supplier<?> toggle, String... modids){
        FermiumRegistryAPI.enqueueMixin(false, mixinJson, () -> isEnabled(toggle.get()) && Arrays.stream(modids).allMatch(FermiumJarScanner::isModPresent));
    }

    private static boolean isEnabled(Object toggle) {
        if (toggle instanceof Boolean) return (Boolean) toggle;
        if (toggle instanceof Integer) return (Integer) toggle >= 0;
        if (toggle instanceof Float) return (Float) toggle > 0;
        if (toggle instanceof Collection) return !((Collection<?>) toggle).isEmpty();
        throw new IllegalArgumentException("Unsupported mixin toggle type " + toggle);
    }
}
