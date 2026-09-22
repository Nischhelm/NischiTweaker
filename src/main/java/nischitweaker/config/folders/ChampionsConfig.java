package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import meldexun.betterconfig.api.Order;
import net.minecraftforge.common.config.Config;
import nischitweaker.Tags;

@MixinConfig(name = Tags.MODID)
public class ChampionsConfig {

    @Config.Comment({
            "Will cap out added dmg for champions to a modifiable amount.",
            "When base dmg above threshold, will add flat damage instead of multiplying."
    })
    @Config.Name("Cap Increased Damage (MixinToggle)")
    @MixinConfig.MixinToggle(lateMixin = "mixins.nischitweaker.champions.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "champions", desired = true, warnIngame = false, reason = "Champions compat requires Champions")
    @Order(0)
    public boolean enableChampionsDamageScalingMixin = true;

    @Config.Comment({
            "Champions will not be able to get more added damage than this value times the growthFactor of their rank.",
            "Example: A 5 star with growthFactor of 15 wont get more than +750 dmg.",
            "The breakpoint is reached at this value divided by champions attackDamage cfg,",
            " so by default 50/0.5 = 100 base dmg"
    })
    @Config.Name("Damage Cap per growthFactor")
    @Config.RangeDouble(min = 0.0)
    @Order(1)
    public double flatDamageAmount = 50;
}
