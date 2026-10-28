package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.Tags;

@MixinConfig(name = Tags.MODID)
public class RebornCoreConfig {

    @Config.Comment("Fixes a potential exploit in RebornCore")
    @Config.Name("Exploit Fix (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixin.nischitweaker.reborncore.exploitfix.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "reborncore", desired = true, warnIngame = false, reason = "Compat for RebornCore")
    public boolean exploitFix = true;
}
