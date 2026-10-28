package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.MixinUtil;
import nischitweaker.Tags;
import nischitweaker.config.ConfigHandler;

@MixinConfig(name = Tags.MODID, classloadEarly = true)
public class BloodmoonConfig {

    static {
        MixinUtil.conditionallyAddMixin("mixin.nischitweaker.bloodmoon.disablebeforephase.json", () -> ConfigHandler.bloodmoon.bloodmoonMinPhase, "bloodmoon", "srparasites", "srpmixins");
    }

    @Config.Comment({
            "Prevent bloodmoons if all players are below this SRP evolution phase (and evolution is enabled)",
            "Disable mixin by setting to -1 (requires restart)"
    })
    @Config.Name("Bloodmoon Minimum Phase (Mixin Toggle)")
    @Config.RangeInt(min = -1, max = 10)
    @Config.SlidingOption
    public int bloodmoonMinPhase = 3;
}
