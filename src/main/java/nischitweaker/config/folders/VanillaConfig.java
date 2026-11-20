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
        MixinUtil.conditionallyAddMixin("mixin.nischitweaker.srparasites.cylindricaldespawn.json", () -> ConfigHandler.vanilla.cylindricalDespawn, "srparasites");
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

    @Config.Comment({
            "Repairing two items in a crafting grid gives back each input's container item.",
            "For repairable items that have one (like No Tree Punching saws) this keeps both inputs in the grid, duping them.",
            "This makes the repair recipe consume both inputs fully."
    })
    @Config.Name("Fix Repair Recipe Container Item Dupe (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.vanilla.repaircontaineritems.json", defaultValue = true)
    public boolean fixRepairContainerItems = true;

    @Config.Comment({
            "Shift-clicking stacked potions or water bottles puts one into each empty bottle slot of the brewing stand.",
            "Shift-clicking blaze powder puts it into the fuel slot first and only into the ingredient slot once the fuel slot is full"
    })
    @Config.Name("Brewing Stand Shift-Click Fix (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.vanilla.brewingstandshiftclick.json", defaultValue = true)
    public boolean brewingStandShiftClick = true;

    @Config.Comment({
            "Stops sprinting when riding an entity like a boat.",
            "Without this, the server still thinks the player is sprinting while riding."
    })
    @Config.Name("Stop Sprinting While Riding (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.vanilla.ridingsprint.json", defaultValue = true)
    public boolean stopSprintingWhileRiding = true;

    @Config.Comment({
            "Mobs only check the horizontal distance to the closest player for despawning, like mob spawning does.",
            "Without this, mobs on the ground despawn instantly while a player is more than 128 blocks above them.",
            "Also applies to SRParasites' own despawn logic if it is installed"
    })
    @Config.Name("Cylindrical Despawn Distance (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.vanilla.cylindricaldespawn.json", defaultValue = false)
    public boolean cylindricalDespawn = false;

    //Own enum so the config doesn't load EnumDifficulty during coremod construction (obfuscated names aren't remapped yet)
    public enum Difficulty {
        PEACEFUL, EASY, NORMAL, HARD
    }
}
