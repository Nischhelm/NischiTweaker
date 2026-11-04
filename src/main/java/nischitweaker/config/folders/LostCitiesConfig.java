package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.Tags;

@MixinConfig(name = Tags.MODID)
public class LostCitiesConfig {

    @Config.Comment({
            "In Lost Cities space worlds, allows players without a bed to (re)spawn at a random position within the spawnRadius gamerule around the world spawn",
            "Positions are always inside a city sphere and below its dome, never in the void or on top of the dome. Respects spawnNotInBuilding of the profile"
    })
    @Config.Name("Space Spawn Radius (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.lostcities.spacespawnradius.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "lostcities", desired = true, warnIngame = false, reason = "Compat for The Lost Cities")
    public boolean spaceSpawnRadius = true;
}
