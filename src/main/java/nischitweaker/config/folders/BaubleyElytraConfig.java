package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.Tags;

@MixinConfig(name = Tags.MODID)
public class BaubleyElytraConfig {

    @Config.Comment("Makes Baubley Elytra not depend on ConfigAnytime but on ZenUtils.")
    @Config.Name("Remove ConfigAnytime Dependency (MixinToggle)")
    public boolean removeConfigAnytimeDependency = true;
}
