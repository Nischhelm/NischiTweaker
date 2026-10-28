package nischitweaker.config.folders;

import com.google.common.collect.ImmutableMap;
import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.MixinUtil;
import nischitweaker.Tags;
import nischitweaker.config.ConfigHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@MixinConfig(name = Tags.MODID, classloadEarly = true)
public class XPTomeConfig {

    static {
        MixinUtil.conditionallyAddMixin("mixin.nischitweaker.xptome.greatertome.json", () -> ConfigHandler.xptome.additionalTomes.keySet(), "xpbook");
    }

    @Config.Comment({
            "Adds more XP Tomes with the given names and xp storage abilities.",
            "To add more, you will need to provide a lang key, a texture, an item model json and a recipe json.",
            "Default: greater_xp_book with 30970 = 100 lvl storage",
            "Clear map to disable mixin.",
            "Works for XP Tome v1.1.6"
    })
    @Config.Name("Additional XP Tomes (MixinToggle)")
    @Config.RequiresMcRestart
    public Map<String, Integer> additionalTomes = new LinkedHashMap<>(ImmutableMap.of(
            "greater_xp_book", 30970
    ));
}
