package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.Tags;

@MixinConfig(name = Tags.MODID)
public class CharmConfig {

    @Config.Comment({
            "Fixes a ClassCastException crash when a Charm crate gets broken/replaced while holding a foreign tile entity",
            "(e.g. a Varied Commodities crate tile placed by OTG worldgen)."
    })
    @Config.Name("Fix Crate Cast Crash (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.charm.cratecastcrash.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "charm", desired = true, warnIngame = false, reason = "Compat for Charm")
    public boolean fixCrateCastCrash = true;
}
