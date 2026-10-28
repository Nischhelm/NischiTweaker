package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.Tags;

@MixinConfig(name = Tags.MODID)
public class LevelUp2Config {

    @Config.Comment("Makes stealth attacks require the target to not face the player, instead of incorrectly checking the opposite: whether the player is facing the target")
    @Config.Name("Fix Stealth (LevelUp2)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixin.nischitweaker.levelup2.fixstealth.json", defaultValue = true)
    @MixinConfig.CompatHandling(
            modid = "levelup2",
            targetVersionRange = "(,1.1.23]",
            desired = true,
            warnIngame = false,
            reason = "Requires mod to properly function"
    )
    public boolean fixStealth = true;

    @Config.Comment("Fixes a potential exploit in Level Up! Reloaded")
    @Config.Name("Exploit Fix (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixin.nischitweaker.levelup2.exploitfix.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "levelup2", targetVersionRange = "(,1.1.23]", desired = true, warnIngame = false, reason = "Compat for Level Up! Reloaded")
    public boolean exploitFix = true;
}
