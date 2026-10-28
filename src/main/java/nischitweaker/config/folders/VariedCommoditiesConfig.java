package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.MixinUtil;
import nischitweaker.Tags;
import nischitweaker.config.ConfigHandler;

@MixinConfig(name = Tags.MODID, classloadEarly = true)
public class VariedCommoditiesConfig {

    static {
        MixinUtil.conditionallyAddMixin("mixin.nischitweaker.variedcommodities.musketloadtime.json", () -> ConfigHandler.variedcommodities.musketLoadingTime != 60, "variedcommodities");
    }

    @Config.Comment({
            "Musket loading time in ticks (20 ticks = 1 second)",
            "Set to default 60 ticks to disable mixin (requires restart)"
    })
    @Config.Name("Musket Loading Time")
    @Config.RangeInt(min = 1, max = 200)
    public int musketLoadingTime = 20;
}
