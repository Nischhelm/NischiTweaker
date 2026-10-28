package nischitweaker.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import nischitweaker.Tags;

@MixinConfig(name = Tags.MODID)
public class ChunkPregeneratorConfig {

    @Config.Comment("Fixes a potential exploit in Chunk Pregenerator 2.x")
    @Config.Name("Exploit Fix: Chunk Pregenerator 2.x (MixinToggle)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(mixinJson = "mixin.nischitweaker.chunkpregenerator.packetpermissions.json", defaultValue = true)
    @MixinConfig.CompatHandling(modid = "chunkpregenerator", targetVersionRange = "[2.0,3.0)", desired = true, warnIngame = false, reason = "Compat for Chunk Pregenerator 2.x")
    public boolean exploitFix = true;
}
