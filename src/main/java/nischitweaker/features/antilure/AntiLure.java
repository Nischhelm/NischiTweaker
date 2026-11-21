package nischitweaker.features.antilure;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.init.SRPSounds;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import nischitweaker.Tags;
import nischitweaker.config.ConfigHandler;
import nischitweaker.config.folders.SRParasitesConfig;
import srpmixins.config.SRPConfigProvider;
import srpmixins.config.providers.SRPMobConfigProvider;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

import java.util.List;
import java.util.stream.Collectors;

public class AntiLure {
    public static Block BLOCK;
    public static Item ITEM;

    public static boolean isEnabled() {
        return Loader.isModLoaded("srparasites") && Loader.isModLoaded("srpmixins") && ConfigHandler.srparasites.antiLure.enabled;
    }

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        BLOCK = new BlockAntiLure()
                .setRegistryName(new ResourceLocation(Tags.MODID, "anti_lure"))
                .setTranslationKey(Tags.MODID + ".anti_lure");
        event.getRegistry().register(BLOCK);
    }

    public static void registerItems(RegistryEvent.Register<Item> event) {
        ITEM = new ItemBlock(BLOCK) {
            @Override
            public int getMetadata(int damage) {
                return damage;
            }

            @Override
            public String getTranslationKey(ItemStack stack) {
                return super.getTranslationKey(stack) + "_" + stack.getMetadata();
            }
        }.setHasSubtypes(true).setRegistryName(BLOCK.getRegistryName());
        event.getRegistry().register(ITEM);
    }

    /** @return whether the anti-lure was used up */
    public static boolean activate(World world, BlockPos pos, EntityPlayer player, int tier) {
        SRParasitesConfig.AntiLureConfig config = ConfigHandler.srparasites.antiLure;
        int dim = world.provider.getDimension();
        if (config.dimensionBlacklist.contains(dim)) {
            sendMessage(player, new TextComponentTranslation("nischitweaker.msg.antilure.dimension"));
            return false;
        }

        SRPSaveData data = SRPSaveDataInterface.get(world, player, null);
        byte phase = data.getEvolutionPhase(dim);

        // Melting without the hive gaining the points would just be free kills
        if (!canGainPoints(world, data, dim, phase)) {
            sendMessage(player, new TextComponentTranslation("nischitweaker.msg.antilure.silent"));
            return false;
        }

        double rangeSq = config.radius * config.radius;
        List<EntityParasiteBase> parasites = world.getEntitiesWithinAABB(EntityParasiteBase.class, new AxisAlignedBB(pos).grow(config.radius), parasite ->
                parasite != null && parasite.isEntityAlive() && !ParasiteMelting.isMelting(parasite)
                        && parasite.getDistanceSqToCenter(pos) <= rangeSq
                        && !config.blacklist.contains(getName(parasite)) && !config.blacklist.contains(getGroup(parasite)));

        if (parasites.isEmpty()) {
            sendMessage(player, new TextComponentTranslation("nischitweaker.msg.antilure.none"));
            return false;
        }

        double basePoints = 0;
        for (EntityParasiteBase parasite : parasites) {
            Integer value = config.penalties.get(getName(parasite));
            if (value == null) value = config.penalties.getOrDefault(getGroup(parasite), config.defaultPoints);
            basePoints += value;
            ParasiteMelting.startMelting(parasite);
        }

        int points = (int) Math.min(Integer.MAX_VALUE, Math.ceil(basePoints * getPhaseMultiplier(config.phaseMultipliers, phase) * config.tierMultipliers.getOrDefault(tier, 1)));
        data.setTotalKills(dim, points, true, world, true);

        world.playSound(null, pos, SRPSounds.INFECTED_MELT, SoundCategory.PLAYERS, 1.0F, 0.6F);
        sendMessage(player, new TextComponentTranslation("nischitweaker.msg.antilure.used"));
        return true;
    }

    public static List<Integer> getTiers() {
        return ConfigHandler.srparasites.antiLure.tierMultipliers.keySet().stream()
                .filter(tier -> tier >= 0 && tier <= 15)
                .sorted()
                .collect(Collectors.toList());
    }

    private static boolean canGainPoints(World world, SRPSaveData data, int dim, byte phase) {
        return SRPConfigSystems.useEvolution
                && phase >= -1 && phase < SRPConfigProvider.getMaxPhase()
                && data.getCanGain(dim)
                && data.getCooldown(world, dim) == 0;
    }

    private static double getPhaseMultiplier(double[] multipliers, byte phase) {
        if (multipliers.length == 0) return 1;
        return multipliers[MathHelper.clamp(phase, 0, multipliers.length - 1)];
    }

    private static void sendMessage(EntityPlayer player, TextComponentTranslation message) {
        player.sendStatusMessage(message.setStyle(new Style().setColor(TextFormatting.DARK_RED)), true);
    }

    public static String normalizeConfig(String entry) {
        entry = entry.trim();
        if (entry.contains(":") || SRPMobConfigProvider.mobGroupToParaIdMap.containsKey(entry)) return entry;
        return "srparasites:" + entry;
    }

    private static String getName(EntityParasiteBase parasite) {
        ResourceLocation id = EntityList.getKey(parasite);
        return id == null ? "" : id.toString();
    }

    private static String getGroup(EntityParasiteBase parasite) {
        return SRPMobConfigProvider.getParaGroup(parasite.getParasiteIDRegister());
    }
}
