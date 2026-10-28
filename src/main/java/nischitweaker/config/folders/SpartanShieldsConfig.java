package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.Tags;

@MixinConfig(name = Tags.MODID)
public class SpartanShieldsConfig {

    @Config.Comment("Fixes a potential exploit in Spartan Shields")
    @Config.Name("Exploit Fix (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.spartanshields.exploitfix.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "spartanshields", desired = true, warnIngame = false, reason = "Compat for Spartan Shields")
    public boolean exploitFix = true;
}
