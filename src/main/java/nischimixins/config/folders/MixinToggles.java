package nischimixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischimixins.Tags;

@MixinConfig(name = Tags.MODID)
public class MixinToggles {

    @Config.Comment("Makes ZenUtils not depend on ConfigAnytime.")
    @Config.Name("Remove ConfigAnytime Dependency (ZenUtils)")
    @MixinConfig.MixinToggle(
            earlyMixin = "mixins.nischimixins.zenutils.json",
            defaultValue = true
    )
    @MixinConfig.CompatHandling(modid = "zenutils", desired = true, targetVersionRange = "(,1.27.5]")
    public boolean enableZenUtilsMixin = true;
}
