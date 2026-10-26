package nischitweaker.config.folders;

import net.minecraftforge.common.config.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZenUtilsConfig {

    @Config.Comment("Makes #mixin Share work in ZenUtils 1.27.5. Was fixed in ZenUtils 1.28.7, this backports the fix.")
    @Config.Name("Fix Share Annotation (ASM Toggle)")
    public boolean fixShareAnnotation = true;

    @Config.Comment("List of classes to replace ConfigAnytime.register with zenutils ConfigAnytimeAnytime.register")
    @Config.Name("ConfigAnytime Dependency Replacements (ASM Toggle)")
    public List<String> configAnytimeClassPatches = new ArrayList<>(Arrays.asList(
            "youyihj.zenutils.impl.core.Configuration",
            "git.jbredwards.baubleye.PatchConfigs",
            "mod.acgaming.universaltweaks.config.UTConfigGeneral",
            "mod.acgaming.universaltweaks.config.UTConfigTweaks",
            "mod.acgaming.universaltweaks.config.UTConfigBugfixes",
            "mod.acgaming.universaltweaks.config.UTConfigMods"
            /*
                arekkuusu.betterhurttimer.BHTConfig
                btpos.dj2addons.DJ2AConfig
                citpatch.ConfigHandler
                com.invadermonky.futurefluidlogged.handlers.ConfigHandlerFF
                com.invadermonky.magicultureintegrations.config.MIConfigAdditions
                com.invadermonky.magicultureintegrations.config.MIConfigFixes
                com.invadermonky.magicultureintegrations.config.MIConfigTweaks
                com.nomiceu.nomilabs.config.LabsConfig
                com.nomiceu.nomilabs.config.LabsVersionConfig
                com.smd.gctcore.common.config.GCTCompatConfig
                com.smd.gctcore.common.config.GCTCoreConfig
                com.smd.gctcore.common.config.GCTMixinConfig
                com.vivern.arpg.ARPGConfig
                com.xinyihl.whimcraft.Configurations
                com.yor42.culllessleaveslegacy.Config
                dev.redstudio.flexipause.config.FlexiPauseConfig
                dev.redstudio.valkyrie.config.ValkyrieConfig
                dev.tianmi.sussypatches.common.SusConfig
                github.kasuminova.stellarcore.common.config.StellarCoreConfig
                github.starfall063.chainmining.ChainMiningConfig
                ink.ikx.rt.impl.internal.config.RTConfig
                io.enderdev.endermodpacktweaks.config.CfgFeatures
                io.enderdev.endermodpacktweaks.config.CfgMinecraft
                io.enderdev.endermodpacktweaks.config.CfgModpack
                io.enderdev.endermodpacktweaks.config.CfgTweaks
                io.wdsj.homoium.config.Settings
                io.wdsj.hybridfix.config.Settings
                io.wdsj.zspacket.config.Settings
                kasuminova.novaeng.common.config.NovaEngCoreConfig
                me.elephant1214.paperfixes.configuration.PaperFixesConfig
                mod.emt.thaumictweaker.config.ConfigEnhancementsTT
                mod.emt.thaumictweaker.config.ConfigOverhaulsTT
                mod.emt.thaumictweaker.config.ConfigWussModeTT
                mod.icarus.crimsonrevelations.config.ConfigHandlerNCR
                net.superkat.explosiveenhancement.config.ExplosiveEnhancementConfig
                projecthds.herodotusutils.config.HDSUConfig
                roidrole.roidtweaker.RoidTweakerConfig
                roidrole.tfctfud.TFCTFUDConfig
                roidrole.thaumicinfo.ThaumicInformationConfig
                sayys.depthsupdate.DepthsUpdateConfig
                su.terrafirmagreg.modules.animal.ConfigAnimal
                su.terrafirmagreg.modules.device.ConfigDevice
                su.terrafirmagreg.modules.rock.ConfigRock
                su.terrafirmagreg.modules.soil.ConfigSoil
                su.terrafirmagreg.modules.wood.ConfigWood
                su.terrafirmagreg.modules.food.ConfigFood
                su.terrafirmagreg.modules.core.ConfigCore
                su.terrafirmagreg.TerraFirmaGregConfig
                surreal.fixeroo.FixerooConfig
             */
    ));
}
