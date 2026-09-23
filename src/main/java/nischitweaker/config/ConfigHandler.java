package nischitweaker.config;

import meldexun.betterconfig.api.BetterConfig;
import meldexun.betterconfig.api.BetterConfigManager;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nischitweaker.Tags;
import nischitweaker.config.folders.BaubleyElytraConfig;
import nischitweaker.config.folders.ChampionsConfig;
import nischitweaker.config.folders.ZenUtilsConfig;

@BetterConfig(
		modid = Tags.MODID,
		version = Tags.CFG_VERSION,
		bigCategoryComments = false,
		lowerCaseCategories = false
)
public class ConfigHandler {

	@Config.Name("ZenUtils")
	public static ZenUtilsConfig zenutils = new ZenUtilsConfig();

	@Config.Name("Baubley Elytra")
	public static BaubleyElytraConfig baubleyElytra = new BaubleyElytraConfig();

	@Config.Name("Champions")
	public static ChampionsConfig champions = new ChampionsConfig();

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