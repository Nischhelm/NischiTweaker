package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.MixinUtil;
import nischitweaker.Tags;
import nischitweaker.config.ConfigHandler;

import java.util.LinkedHashSet;
import java.util.Set;

@MixinConfig(name = Tags.MODID, classloadEarly = true)
public class WaystonesConfig {

    static {
        MixinUtil.conditionallyAddMixin("mixin.nischitweaker.waystones.villagedimensionblacklist.json", () -> ConfigHandler.waystones.villageDimensionBlacklist, "waystones");
    }

    @Config.Comment({
            "Dimension ids in which villages don't generate a waystone",
            "Waystones generating just anywhere (without a structure) are already limited to the overworld by Waystones itself",
            "Empty list to disable mixin (requires restart)"
    })
    @Config.Name("Village Dimension Blacklist (MixinToggle)")
    public Set<Integer> villageDimensionBlacklist = new LinkedHashSet<>();
}
