package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.Tags;

@MixinConfig(name = Tags.MODID)
public class MixinToggles {

    @Config.Comment("Makes ZenUtils not depend on ConfigAnytime.")
    @Config.Name("Remove ConfigAnytime Dependency (ZenUtils)")
    @MixinConfig.MixinToggle(
            earlyMixin = "mixins.nischitweaker.zenutils.json",
            defaultValue = true
    )
    @MixinConfig.CompatHandling(modid = "zenutils", desired = true, targetVersionRange = "(,1.27.5]")
    public boolean enableZenUtilsMixin = true;
}
