package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.Tags;

@MixinConfig(name = Tags.MODID)
public class ZenUtilsConfig {

    @Config.Comment("Makes ZenUtils not depend on ConfigAnytime.")
    @Config.Name("Remove ConfigAnytime Dependency (MixinToggle)")
    @MixinConfig.MixinToggle(earlyMixin = "mixins.nischitweaker.zenutils.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "zenutils", desired = true, targetVersionRange = "(,1.27.5]", reason = "ZenUtils compat requires ZenUtils")
    public boolean enableZenUtilsMixin = true;
}
