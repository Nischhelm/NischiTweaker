package nischitweaker;

import fermiumbooter.util.FermiumJarScanner;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import nischitweaker.asm.zenutils.ConfigAnytimeClassTransformer;
import nischitweaker.asm.indev.bloodmoon.BloodmoonDevMappingsTransformer;
import nischitweaker.asm.DistantHorizonsMixinConnectorTransformer;
import nischitweaker.asm.EnchanterToolsClassTransformer;
import nischitweaker.asm.indev.invtweaks.InvTweaksDevAccessTransformer;
import nischitweaker.asm.zenutils.ZenUtilsShareTransformer;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class NischiTweakerPlugin implements IFMLLoadingPlugin {

	public NischiTweakerPlugin() {
		if(FermiumJarScanner.isModVersionInRange("zenutils", "[1.25.0,)"))
			Launch.classLoader.registerTransformer(ConfigAnytimeClassTransformer.class.getName());
		if(FermiumJarScanner.isModVersionInRange("zenutils", "[1.26.0,1.28.7)"))
			Launch.classLoader.registerTransformer(ZenUtilsShareTransformer.class.getName());
		if(FermiumJarScanner.isModPresent("distanthorizons"))
			Launch.classLoader.registerTransformer(DistantHorizonsMixinConnectorTransformer.class.getName());

		if(MixinUtil.isDev() && FermiumJarScanner.isModPresent("bloodmoon"))
			Launch.classLoader.registerTransformer(BloodmoonDevMappingsTransformer.class.getName());
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{
				EnchanterToolsClassTransformer.class.getName()
		};
	}
	
	@Override public String getModContainerClass() {return null;}
	@Override public String getSetupClass() {return null;}
	@Override public void injectData(Map<String, Object> data) {}
	
	@Override
	public String getAccessTransformerClass()
	{
		if(MixinUtil.isDev() && FermiumJarScanner.isModPresent("inventorytweaks"))
			return InvTweaksDevAccessTransformer.class.getName();
		return null;
	}
}