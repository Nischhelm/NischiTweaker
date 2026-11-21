package nischitweaker.config.folders;

import com.google.common.collect.ImmutableMap;
import fermiumbooter.annotations.MixinConfig;
import meldexun.betterconfig.api.Order;
import net.minecraftforge.common.config.Config;
import nischitweaker.Tags;
import nischitweaker.config.ConfigFolder;
import nischitweaker.features.antilure.AntiLure;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@MixinConfig(name = Tags.MODID)
public class SRParasitesConfig extends ConfigFolder {

    @Config.Comment("Ignore light level spawn check for parasites in End dimension")
    @Config.Name("Light Ignore in End Dim (MixinToggle)")
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.srparasites.endlightcheck.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "srparasites", desired = true, warnIngame = false, reason = "Compat for SRParasites")
    public boolean endLightCheckSkip = true;

    @Config.Comment("Add Pivot effect to nearby parasites when Dispatcher Nidus fails to create a Dispatcher entity")
    @Config.Name("Nidus Spreads Pivot (MixinToggle)")
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.srparasites.niduspivot.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "srparasites", desired = true, warnIngame = false, reason = "Compat for SRParasites")
    public boolean dispatcherNidusPivot = true;

    @Config.Comment("Adds an Anti-Lure block (requires SRPMixins)")
    @Config.Name("Anti-Lure")
    public AntiLureConfig antiLure = new AntiLureConfig();

    public SRParasitesConfig() {
        super("srparasites");
    }

    public static class AntiLureConfig extends ConfigFolder {

        @Config.Comment({
                "Right clicking an Anti-Lure melts all parasites around it and breaks it.",
                "Melted parasites drop no loot or xp and don't count as dying, instead the player phase increases by some points.",
                "Can't be used while the parasites can't gain points, for example during the phase cooldown or in the max phase."
        })
        @Config.Name("Enable Anti-Lure")
        @Config.RequiresMcRestart
        @Order(1)
        public boolean enabled = false;

        @Config.Comment("Radius in blocks around the Anti-Lure in which parasites melt")
        @Config.Name("Radius")
        @Config.RangeDouble(min = 0)
        @Order(2)
        public double radius = 24;

        @Config.Comment("How many ticks parasites take to melt")
        @Config.Name("Melt Duration")
        @Config.RangeInt(min = 1)
        @Order(3)
        public int meltDuration = 60;

        @Config.Comment("Base points for parasites that have neither a group nor a name listed in \"Points\"")
        @Config.Name("Default Points")
        @Config.RangeInt(min = 0)
        @Order(4)
        public int defaultPoints = 2;

        @Config.Comment({
                "Base evolution points gained per melted parasite, depending on parasite group or parasite. Gets multiplied by the phase multiplier",
                "Pattern: group or name=value",
                "Examples: PURE=5, rupter=0.5, someaddonmodid:someaddonparaname=4.2",
                "If a parasite has a group and is named separately here, the parasite specific value will be used, the group value will be ignored"
        })
        @Config.Name("Penalties")
        @Order(5)
        public LinkedHashMap<String, Integer> penalties = new LinkedHashMap<>(ImmutableMap.<String, Integer>builder()
                .put("INBORN", 1)
                .put("carrier_light", 5)
                .put("carrier_flying", 5)
                .put("carrier_heavy", 5)
                .put("ASSIMILATED", 5)
                .put("CRUDE", 20)
                .put("FERAL", 20)
                .put("HIJACKED", 20)
                .put("PRIMITIVE", 20)
                .put("DETERRENT", 20)
                .put("mangler", 20)
                .put("ADAPTED", 50)
                .put("PURE", 200)
                .put("PREEMINENT", 500)
                .put("ANCIENT", 1000)
                .put("sim_dragone", 1000)
                .put("sim_dragonehead", 200)
                .build()
        );

        @Config.Comment({
                "Registered Anti-Lure tiers (block metadata 0 to 15) and their multiplier on the gained points",
                "Pattern: tier=multiplier"
        })
        @Config.Name("Tier Multipliers")
        @Config.RequiresMcRestart
        @Order(6)
        public LinkedHashMap<Integer, Integer> tierMultipliers = new LinkedHashMap<>(ImmutableMap.<Integer, Integer>builder()
                .put(1, 1)
                .put(2, 3)
                .put(3, 10)
                .put(4, 40)
                .put(5, 100)
                .put(6, 1000)
                .build()
        );

        @Config.Comment({
                "Multiplier on the summed up base points of all melted parasites, depending on the current phase",
                "First entry is for phase 0 (and below), the last entry is used for all phases beyond the list"
        })
        @Config.Name("Phase Multipliers")
        @Order(7)
        public double[] phaseMultipliers = {
                4,
                4,
                10,
                50,
                50,
                300,
                1500,
                1500,
                1500,
                1500
        };

        @Config.Comment({
                "Parasite groups or names that don't melt",
                "Examples: NEXUS, beckon_si, someaddonmodid:someaddonparaname"
        })
        @Config.Name("Blacklist")
        @Order(8)
        public Set<String> blacklist = new LinkedHashSet<>(Arrays.asList(
                "beckon_siii",
                "beckon_siv",
                "dispatcher_siii",
                "dispatcher_siv"
        ));

        @Config.Comment("Dimension ids in which the Anti-Lure can't be used")
        @Config.Name("Dimension Blacklist")
        @Order(9)
        public Set<Integer> dimensionBlacklist = new LinkedHashSet<>();

        public AntiLureConfig() {
            super("srpmixins");
        }

        @Override
        protected void updateConfig() {
            LinkedHashMap<String, Integer> normalizedPoints = new LinkedHashMap<>();
            penalties.forEach((name, value) -> normalizedPoints.put(AntiLure.normalizeConfig(name), value));
            penalties = normalizedPoints;
            blacklist = blacklist.stream().map(AntiLure::normalizeConfig).collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }
}
