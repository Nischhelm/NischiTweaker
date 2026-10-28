package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.MixinUtil;
import nischitweaker.Tags;
import nischitweaker.config.ConfigHandler;

import java.util.LinkedHashSet;
import java.util.Set;

@MixinConfig(name = Tags.MODID, classloadEarly = true)
public class ReskillableConfig {

    static {
        MixinUtil.conditionallyAddMixin("mixin.nischitweaker.reskillable.goldenosmosis.json", () -> ConfigHandler.reskillable.goldenOsmosisRepairableItems, "reskillable");
    }

    @Config.Comment("Only drop extra wheat when fully grown")
    @Config.Name("More Wheat Fix (MixinToggle)")
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.reskillable.morewheatfix.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "reskillable", desired = true, warnIngame = false, reason = "Compat for Reskillable")
    public boolean moreWheatFix = true;

    @Config.Comment({
            "Items that can be repaired with Golden Osmosis trait",
            "Empty list to disable mixin (requires restart)"
    })
    @Config.Name("Golden Osmosis Repairable Items")
    public Set<String> goldenOsmosisRepairableItems = new LinkedHashSet<>();

    @Config.Comment("Fixes a potential exploit in Reskillable")
    @Config.Name("Exploit Fix (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.reskillable.exploitfix.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "reskillable", desired = true, warnIngame = false, reason = "Compat for Reskillable")
    public boolean exploitFix = true;
}
