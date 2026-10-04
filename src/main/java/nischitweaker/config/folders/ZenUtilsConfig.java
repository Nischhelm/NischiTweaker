package nischitweaker.config.folders;

import net.minecraftforge.common.config.Config;

public class ZenUtilsConfig {

    @Config.Comment("Makes #mixin Share work in ZenUtils 1.27.5. Was fixed in ZenUtils 1.28.7, this backports the fix.")
    @Config.Name("Fix Share Annotation (ASM Toggle)")
    public boolean fixShareAnnotation = true;
}
