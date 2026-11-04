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
        MixinUtil.conditionallyAddMixin("mixin.nischitweaker.bloodmoon.lostcitiesparasites.json", () -> ConfigHandler.bloodmoon.lostCitiesBloodmoons && ConfigHandler.bloodmoon.lostCitiesParasites, "bloodmoon", "lostcities", "srparasites", "srpmixins");
    }

    @Config.Comment({
            "Prevent bloodmoons if all players are below this SRP evolution phase (and evolution is enabled)",
            "Disable mixin by setting to -1 (requires restart)"
    })
    @Config.Name("Bloodmoon Minimum Phase (Mixin Toggle)")
    @Config.RangeInt(min = -1, max = 10)
    @Config.SlidingOption
    public int bloodmoonMinPhase = 3;

    @Config.Comment({
            "Blood moons also happen in the Lost Cities dimension, at the same time as in the overworld",
            "Requires Bloodmoon on the client to see the red moon"
    })
    @Config.Name("Lost Cities Bloodmoons (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.bloodmoon.lostcities.json", defaultValue = false)
    @MixinConfig.CompatHandling(modid = "bloodmoon", desired = true, warnIngame = false, reason = "Compat for Bloodmoon")
    @MixinConfig.CompatHandling(modid = "lostcities", desired = true, warnIngame = false, reason = "Compat for The Lost Cities")
    public boolean lostCitiesBloodmoons = false;

    @Config.Comment({
            "During Lost Cities blood moons, the SRP custom spawner also runs and the parasite mob cap is multiplied by \"Lost Cities Bloodmoon Parasite Cap Multiplier\"",
            "Requires \"Lost Cities Bloodmoons\" and SRPMixins",
            "With SRPMixins \"Fix Spawning Entirely\", SRPMixins already spawns parasites during blood moons. Use its mob cap rules with [bloodmoon = true] instead"
    })
    @Config.Name("Lost Cities Bloodmoon Parasites (Mixin Toggle)")
    @Config.RequiresMcRestart
    public boolean lostCitiesParasites = false;

    @Config.Comment("Parasite mob cap multiplier during blood moons in the Lost Cities dimension")
    @Config.Name("Lost Cities Bloodmoon Parasite Cap Multiplier")
    @Config.RangeDouble(min = 0)
    public float lostCitiesParasiteCapMultiplier = 4;
}
