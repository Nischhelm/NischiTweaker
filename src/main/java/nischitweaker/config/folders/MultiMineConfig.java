package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.Tags;

@MixinConfig(name = Tags.MODID)
public class MultiMineConfig {

    @Config.Comment("Fixes a potential exploit in Multi Mine")
    @Config.Name("Exploit Fix: Multi Mine (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.multimine.exploitfix.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "multimine", desired = true, warnIngame = false, reason = "Compat for Multi Mine")
    public boolean exploitFix = true;
}
