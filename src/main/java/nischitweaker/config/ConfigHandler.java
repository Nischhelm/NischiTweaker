package nischitweaker.config;

import meldexun.betterconfig.api.BetterConfig;
import meldexun.betterconfig.api.BetterConfigManager;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nischitweaker.Tags;
import nischitweaker.config.folders.ZenUtilsConfig;

@BetterConfig(
		modid = Tags.MODID,
		version = Tags.CFG_VERSION,
		bigCategoryComments = false,
		lowerCaseCategories = false
)
public class ConfigHandler {
	
	@Config.Comment("Mixin Toggles")
	@Config.Name("Mixin Toggles")
	public static ZenUtilsConfig mixins = new ZenUtilsConfig();

	@Mod.EventBusSubscriber
	private static class EventHandler{
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(Tags.MODID)) {
				BetterConfigManager.sync(Tags.MODID);
			}
		}
	}
}