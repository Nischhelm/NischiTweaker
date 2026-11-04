package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.MixinUtil;
import nischitweaker.Tags;
import nischitweaker.config.ConfigHandler;

@MixinConfig(name = Tags.MODID, classloadEarly = true)
public class LostCitiesConfig {

    static {
        MixinUtil.conditionallyAddMixin("mixin.nischitweaker.lostcities.portalphaselock.json", () -> ConfigHandler.lostcities.portalPhaseLock, "lostcities", "srparasites", "srpmixins");
    }

    @Config.Comment({
            "In Lost Cities space worlds, allows players without a bed to (re)spawn at a random position within the spawnRadius gamerule around the world spawn",
            "Positions are always inside a city sphere and below its dome, never in the void or on top of the dome. Respects spawnNotInBuilding of the profile"
    })
    @Config.Name("Space Spawn Radius (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.lostcities.spacespawnradius.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "lostcities", desired = true, warnIngame = false, reason = "Compat for The Lost Cities")
    public boolean spaceSpawnRadius = true;

    @Config.Comment("Activating an SRP Lure in the Lost Cities dimension turns it into a Dispatcher Nidus instead")
    @Config.Name("Lures Become Nidus (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.lostcities.lurenidus.json", defaultValue = false)
    @MixinConfig.CompatHandling(modid = "lostcities", desired = true, warnIngame = false, reason = "Compat for The Lost Cities")
    @MixinConfig.CompatHandling(modid = "srparasites", desired = true, warnIngame = false, reason = "Compat for SRParasites")
    public boolean luresBecomeNidus = false;

    @Config.Comment({
            "Lost Cities portals (sleeping in a bed) are locked until the player reaches this SRP evolution phase",
            "Disable mixin by setting to -1 (requires restart)"
    })
    @Config.Name("Portal Phase Lock (Mixin Toggle)")
    @Config.RangeInt(min = -1, max = 10)
    public int portalPhaseLock = -1;
}
