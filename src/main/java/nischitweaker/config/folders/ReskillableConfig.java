package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import nischitweaker.MixinUtil;
import nischitweaker.Tags;
import nischitweaker.config.ConfigFolder;
import nischitweaker.config.ConfigHandler;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@MixinConfig(name = Tags.MODID, classloadEarly = true)
public class ReskillableConfig extends ConfigFolder {

    static {
        MixinUtil.conditionallyAddMixin("mixin.nischitweaker.reskillable.goldenosmosis.json", () -> ConfigHandler.reskillable.goldenOsmosisRepairableItems, "reskillable");
    }

    @Config.Comment("Only drop extra wheat when fully grown")
    @Config.Name("More Wheat Fix (MixinToggle)")
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.reskillable.morewheatfix.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "reskillable", desired = true, warnIngame = false, reason = "Compat for Reskillable")
    public boolean moreWheatFix = true;

    @Config.Comment({
            "Items that can be repaired with Golden Osmosis trait",
            "Empty list to disable mixin (requires restart)"
    })
    @Config.Name("Golden Osmosis Repairable Items")
    public Set<String> goldenOsmosisRepairableItems = new LinkedHashSet<>();

    @Config.Comment("Fixes a potential exploit in Reskillable")
    @Config.Name("Exploit Fix: Reskillable (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.reskillable.exploitfix.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "reskillable", desired = true, warnIngame = false, reason = "Compat for Reskillable")
    public boolean exploitFix = true;

    private final Set<Item> osmosisRepairableItems = new HashSet<>();

    public ReskillableConfig() {
        super("reskillable");
    }

    @Override
    protected void updateConfig() {
        osmosisRepairableItems.clear();
        for (String name : goldenOsmosisRepairableItems) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name.trim()));
            if (item != null) osmosisRepairableItems.add(item);
        }
    }

    public boolean isOsmosisRepairable(Item item) {
        return osmosisRepairableItems.contains(item);
    }
}
