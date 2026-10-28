package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.MixinUtil;
import nischitweaker.Tags;
import nischitweaker.config.ConfigHandler;

@MixinConfig(name = Tags.MODID, classloadEarly = true)
public class InfernalMobsConfig {

    static {
        MixinUtil.conditionallyAddMixin("mixin.nischitweaker.infernalmobs.parasitechance.json", () -> ConfigHandler.infernalmobs.infernalParasiteChanceIncrease, "infernalmobs", "srparasites", "srpmixins");
    }

    @Config.Comment({
            "Infernal spawn chance increase for parasites in phase 9+ (0.5 = 50% increase)",
            "Set to 0 to disable mixin (requires restart)"
    })
    @Config.Name("Infernal Parasite Chance Increase (MixinToggle)")
    @Config.RangeDouble(min = 0.0)
    public float infernalParasiteChanceIncrease = 0.5F;
}
