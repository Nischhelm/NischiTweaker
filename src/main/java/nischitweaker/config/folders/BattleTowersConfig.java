package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.Tags;

@MixinConfig(name = Tags.MODID)
public class BattleTowersConfig {

    @Config.Comment("Fixes a potential exploit in Battle Towers")
    @Config.Name("Exploit Fix (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.battletowers.exploitfix.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "battletowers", desired = true, warnIngame = false, reason = "Compat for Battle Towers")
    public boolean exploitFix = true;
}
