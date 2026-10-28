package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.Tags;

@MixinConfig(name = Tags.MODID)
public class FancyMenuConfig {

    @Config.Comment("Fixes a potential exploit in FancyMenu")
    @Config.Name("Exploit Fix (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.fancymenu.exploitfix.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "fancymenu", desired = true, warnIngame = false, reason = "Compat for FancyMenu")
    public boolean exploitFix = true;
}
