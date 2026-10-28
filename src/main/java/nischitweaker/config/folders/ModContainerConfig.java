package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.MixinUtil;
import nischitweaker.Tags;
import nischitweaker.config.ConfigHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@MixinConfig(name = Tags.MODID, classloadEarly = true)
public class ModContainerConfig {

    static {
        MixinUtil.conditionallyAddMixin("mixin.nischitweaker.dependencies.json", () -> ConfigHandler.modContainers.removedDependencies.values());
    }

    @Config.Comment({
            "Map of modids to dependencies that should be removed.",
            "This will not magically remove actual dependencies, just the declaration in @Mod or mcmod.info"
    })
    @Config.Name("Removed declared Dependencies")
    public Map<String, ArrayList<String>> removedDependencies = new LinkedHashMap<>();
    private void initRemovedDependencies() {
        removedDependencies.put("zenutils", new ArrayList<>(Collections.singletonList("configanytime")));
        removedDependencies.put("distanthorizons", new ArrayList<>(Collections.singletonList("mixinbooter")));
        removedDependencies.put("universaltweaks", new ArrayList<>(Collections.singletonList("configanytime")));
    }

    public ModContainerConfig() {
        initRemovedDependencies();
    }
}
