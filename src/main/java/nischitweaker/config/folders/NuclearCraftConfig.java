package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.Tags;

@MixinConfig(name = Tags.MODID)
public class NuclearCraftConfig {

    @Config.Comment("Fixes a potential exploit in NuclearCraft")
    @Config.Name("Exploit Fix (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.nuclearcraft.exploitfix.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "nuclearcraft", modName = "NuclearCraft", desired = true, warnIngame = false, reason = "Compat for NuclearCraft")
    public boolean exploitFix = true;
}
