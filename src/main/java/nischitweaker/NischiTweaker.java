package nischitweaker;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import nischitweaker.config.ConfigHandler;
import nischitweaker.features.GreaterXPTome;
import nischitweaker.features.bookwyrmbucket.BucketedBookWyrm;
import nischitweaker.network.PacketHandler;

@Mod(
        modid = Tags.MODID,
        version = Tags.VERSION,
        name = Tags.NAME,
        dependencies =
                "required-after:fermiumbooter@[1.6.0,);" +
                "required:betterconfig@[1.3.0,)",
        acceptableRemoteVersions = "*"
)
public class NischiTweaker {
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PacketHandler.init();
        if(Loader.isModLoaded("xpbook") && !ConfigHandler.xptome.additionalTomes.isEmpty())
            MinecraftForge.EVENT_BUS.register(GreaterXPTome.class);
        if(Loader.isModLoaded("defiledlands") && ConfigHandler.defiledlands.bookwyrmBucket.bucketedBookWyrm) {
            MinecraftForge.EVENT_BUS.register(BucketedBookWyrm.class);
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ConfigHandler.updateFolders();
    }
}