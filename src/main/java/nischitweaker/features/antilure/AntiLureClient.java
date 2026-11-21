package nischitweaker.features.antilure;

import com.dhanantry.scapeandrunparasites.client.particle.SRPEnumParticle;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class AntiLureClient {
    // entity id -> {world time when melting started, duration}
    private static final Map<Integer, long[]> MELTING = new HashMap<>();

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomStateMapper(AntiLure.BLOCK, new StateMap.Builder().ignore(BlockAntiLure.TIER).build());
        ModelResourceLocation model = new ModelResourceLocation(AntiLure.ITEM.getRegistryName(), "inventory");
        for (int tier : AntiLure.getTiers()) ModelLoader.setCustomModelResourceLocation(AntiLure.ITEM, tier, model);
    }

    public static void startMelting(int entityId, int remaining, int duration) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) return;
        MELTING.put(entityId, new long[]{mc.world.getTotalWorldTime() - (duration - remaining), duration});
    }

    private static float getProgress(Entity entity, float partialTicks) {
        long[] melting = MELTING.get(entity.getEntityId());
        if (melting == null) return -1;
        return MathHelper.clamp((entity.world.getTotalWorldTime() - melting[0] + partialTicks) / melting[1], 0, 1);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderPre(RenderLivingEvent.Pre<?> event) {
        float progress = getProgress(event.getEntity(), Minecraft.getMinecraft().getRenderPartialTicks());
        if (progress < 0) return;
        float widthScale = 1 + 0.4F * progress;
        GlStateManager.pushMatrix();
        GlStateManager.translate(event.getX(), event.getY(), event.getZ());
        GlStateManager.scale(widthScale, 1 - 0.65F * progress, widthScale);
        GlStateManager.translate(-event.getX(), -event.getY(), -event.getZ());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderPost(RenderLivingEvent.Post<?> event) {
        if (MELTING.containsKey(event.getEntity().getEntityId())) GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || MELTING.isEmpty()) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.isGamePaused()) return;

        Iterator<Map.Entry<Integer, long[]>> iterator = MELTING.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, long[]> entry = iterator.next();
            Entity entity = mc.world.getEntityByID(entry.getKey());
            if (entity == null || entity.isDead) {
                iterator.remove();
                continue;
            }
            if (!(entity instanceof EntityParasiteBase)) continue;
            ((EntityParasiteBase) entity).spawnParticles(SRPEnumParticle.GCLOUD, 127, 106, 0);
            ((EntityParasiteBase) entity).spawnParticles(SRPEnumParticle.GCLOUD, 127, 0, 0);
        }
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        if (event.getWorld().isRemote) MELTING.clear();
    }
}
