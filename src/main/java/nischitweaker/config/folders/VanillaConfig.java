package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.MixinUtil;
import nischitweaker.Tags;
import nischitweaker.config.ConfigHandler;

@MixinConfig(name = Tags.MODID, classloadEarly = true)
public class VanillaConfig {

    static {
        MixinUtil.conditionallyAddMixin("mixin.nischitweaker.vanilla.frostwalkermulti.json", () -> ConfigHandler.vanilla.frostWalkerMulti != 1F);
        MixinUtil.conditionallyAddMixin("mixin.nischitweaker.vanilla.newworlddifficulty.json", () -> ConfigHandler.vanilla.newWorldDifficulty != Difficulty.NORMAL);
    }

    @Config.Comment({
            "Increases frost walker frosted size by this amount. Big multipliers can be laggy.",
            "Disable mixin by setting to 1"
    })
    @Config.Name("Frost Walker Size Multiplier")
    @Config.RangeDouble(min = 0)
    public float frostWalkerMulti = 1.75F;

    @Config.Comment({
            "Difficulty newly created singleplayer worlds start with. Dedicated servers use server.properties instead",
            "Disable mixin by setting to NORMAL",
            "This is meant to replace Global GameRules' feature, as that updates the config after each world unload"
    })
    @Config.Name("New World Difficulty")
    public Difficulty newWorldDifficulty = Difficulty.NORMAL;

    @Config.Comment({
            "If level.dat or level.dat_old fail to load, keep a copy of them as level.dat_corrupted_<date> / level.dat_old_corrupted_<date>.",
            "Without this, a fresh level.dat gets created and the corrupted files are rotated away on the next saves"
    })
    @Config.Name("Backup Corrupted level.dat (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.vanilla.corruptedleveldat.json", defaultValue = true)
    public boolean backupCorruptedLevelDat = true;

    //Own enum so the config doesn't load EnumDifficulty during coremod construction (obfuscated names aren't remapped yet)
    public enum Difficulty {
        PEACEFUL, EASY, NORMAL, HARD
    }
}
