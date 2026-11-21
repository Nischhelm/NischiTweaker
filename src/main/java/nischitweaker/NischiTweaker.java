package nischitweaker;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import nischitweaker.config.ConfigHandler;
import nischitweaker.features.GreaterXPTome;
import nischitweaker.features.antilure.AntiLure;
import nischitweaker.features.antilure.AntiLureClient;
import nischitweaker.features.antilure.ParasiteMelting;
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
        if(GreaterXPTome.isEnabled())
            MinecraftForge.EVENT_BUS.register(GreaterXPTome.class);
        if(BucketedBookWyrm.isEnabled()) {
            MinecraftForge.EVENT_BUS.register(BucketedBookWyrm.class);
        }
        if(AntiLure.isEnabled()) {
            MinecraftForge.EVENT_BUS.register(AntiLure.class);
            MinecraftForge.EVENT_BUS.register(ParasiteMelting.class);
            if(event.getSide().isClient()) MinecraftForge.EVENT_BUS.register(AntiLureClient.class);
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ConfigHandler.updateFolders();
    }
}