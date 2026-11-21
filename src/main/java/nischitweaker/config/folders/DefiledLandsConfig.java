package nischitweaker.config.folders;

import meldexun.betterconfig.api.Order;
import net.minecraftforge.common.config.Config;
import nischitweaker.config.ConfigFolder;
import nischitweaker.features.bookwyrmbucket.BucketedBookWyrm;

public class DefiledLandsConfig extends ConfigFolder {

    @Config.Name("Book Wyrm in a Bucket")
    public BookWyrmBucketConfig bookwyrmBucket = new BookWyrmBucketConfig();

    public DefiledLandsConfig() {
        super("defiledlands");
    }

    public static class BookWyrmBucketConfig extends ConfigFolder {

        @Config.Comment({
                "Adds a Book Wyrm in a Bucket. Right click an adult Book Wyrm with an empty bucket (see \"Book Wyrm Bucket Item\") to catch it.",
                "Feed it enchanted books in a crafting grid. While in a player inventory, it burps out new enchanted books into the inventory, then needs to digest for a while.",
                "Sneak + right click a block to release it again."
        })
        @Config.Name("Enable Book Wyrm in a Bucket")
        @Config.RequiresMcRestart
        @Order(1)
        public boolean bucketedBookWyrm = true;

        @Config.Comment({
                "Item used to catch Book Wyrms, as modid:name or modid:name:meta.",
                "The bucket needs to have no NBT data to count as empty.",
                "Examples: minecraft:bucket, iceandfire:jar_empty, familiarfauna:bug_habitat"
        })
        @Config.Name("Bucket Item")
        @Order(2)
        public String bucketItem = "minecraft:bucket";

        @Config.Comment({
                "Cooldown after a bucketed Book Wyrm burps out books is its enchanting level * its digest time (in ticks) * this multiplier.",
                "With a multiplier of 1 this matches the speed of a Book Wyrm entity."
        })
        @Config.Name("Cooldown Multiplier")
        @Config.RangeDouble(min = 0)
        @Order(3)
        public float cooldownMultiplier = 1.0F;

        @Config.Comment({
                "When to show the stats of a bucketed Book Wyrm in its tooltip.",
                "ANALYZER: only while a Book Wyrm Analyzer is in the inventory"
        })
        @Config.Name("Stats Tooltip")
        @Order(4)
        public StatsTooltip statsTooltip = StatsTooltip.ANALYZER;
        public enum StatsTooltip { ALWAYS, ANALYZER, NEVER }

        @Config.Comment("Chance per minute for a bucketed Book Wyrm in a player inventory to hop out of its bucket. Digesting Book Wyrms never escape.")
        @Config.Name("Escape Chance")
        @Config.RangeDouble(min = 0, max = 1)
        @Order(5)
        public float escapeChance = 0.01F;

        @Override
        protected void updateConfig() {
            BucketedBookWyrm.updateBucketItem();
        }
    }
}
