package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.MixinUtil;
import nischitweaker.Tags;
import nischitweaker.config.ConfigHandler;

@MixinConfig(name = Tags.MODID, classloadEarly = true)
public class FamiliarFaunaConfig {

    static {
        MixinUtil.conditionallyAddMixin("mixin.nischitweaker.familiarfauna.pixiespawnheight.json", () -> ConfigHandler.familiarfauna.pixieMaxSpawnHeight, "familiarfauna");
    }

    @Config.Comment({
            "Maximum height for pixie spawning",
            "Set to -1 to disable mixin (requires restart)"
    })
    @Config.Name("Pixie Max Spawn Height (Mixin Toggle)")
    @Config.RangeInt(min = -1, max = 256)
    public int pixieMaxSpawnHeight = 110;
}
