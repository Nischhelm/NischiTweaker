package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.Tags;

@MixinConfig(name = Tags.MODID)
public class QuarkConfig {

    @Config.Comment("Fix quark shulker dropoff not using the correct slot making it fail in certain situations")
    @Config.Name("Shulker Dropoff Fix (MixinToggle)")
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.autoreglib.shulkerdropoff.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "autoreglib", desired = true, warnIngame = false, reason = "Compat for Quark/AutoRegLib")
    public boolean shulkerDropoffFix = true;

    @Config.Comment({
            "Makes Quark load its modules in a fixed order.",
            "This fixes block/item registration order, otherwise Quark would change it for every new world (or when losing/breaking level.dat)"
    })
    @Config.Name("Deterministic Registration (MixinToggle)")
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.quark.deterministicregistration.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "quark", desired = true, warnIngame = false, reason = "Compat for Quark")
    public boolean deterministicRegistration = true;

    @Config.Comment("Fixes a potential exploit in Quark")
    @Config.Name("Exploit Fix (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.quark.exploitfix.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "quark", desired = true, warnIngame = false, reason = "Compat for Quark")
    public boolean exploitFix = true;
}
