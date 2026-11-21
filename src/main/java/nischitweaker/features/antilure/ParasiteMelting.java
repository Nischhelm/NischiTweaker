package nischitweaker.features.antilure;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.init.SRPSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nischitweaker.Tags;
import nischitweaker.config.ConfigHandler;
import nischitweaker.network.PacketHandler;
import nischitweaker.network.ParasiteMeltPacket;

/**
 * Parasites melt away without dying: no death handling, so no loot, xp, point loss, beckons or cysts.
 * The remaining melt time is stored in the entity data so it survives chunk unloads.
 */
public class ParasiteMelting {
    private static final String REMAINING_KEY = Tags.MODID + ":meltRemaining";
    private static final String DURATION_KEY = Tags.MODID + ":meltDuration";

    public static boolean isMelting(Entity entity) {
        return entity.getEntityData().hasKey(REMAINING_KEY);
    }

    public static void startMelting(EntityParasiteBase parasite) {
        int duration = Math.max(1, ConfigHandler.srparasites.antiLure.meltDuration);
        NBTTagCompound data = parasite.getEntityData();
        data.setInteger(REMAINING_KEY, duration);
        data.setInteger(DURATION_KEY, duration);
        parasite.setNoAI(true);
        parasite.setAttackTarget(null);
        PacketHandler.INSTANCE.sendToAllTracking(new ParasiteMeltPacket(parasite.getEntityId(), duration, duration), parasite);
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity.world.isRemote || !(entity instanceof EntityParasiteBase)) return;
        NBTTagCompound data = entity.getEntityData();
        if (!data.hasKey(REMAINING_KEY)) return;

        int remaining = data.getInteger(REMAINING_KEY) - 1;
        if (remaining % 20 == 0) entity.playSound(SRPSounds.INFECTED_MELT, 1.0F, 1.0F);
        if (remaining <= 0) entity.setDead();
        else data.setInteger(REMAINING_KEY, remaining);
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntityLiving() instanceof EntityParasiteBase && isMelting(event.getEntityLiving())) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        if (!(target instanceof EntityParasiteBase) || !isMelting(target)) return;
        NBTTagCompound data = target.getEntityData();
        PacketHandler.INSTANCE.sendTo(new ParasiteMeltPacket(target.getEntityId(), data.getInteger(REMAINING_KEY), data.getInteger(DURATION_KEY)), (EntityPlayerMP) event.getEntityPlayer());
    }
}
