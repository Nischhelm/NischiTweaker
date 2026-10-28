package nischitweaker.features;

import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Distance budget for grapple position updates.
 * Refills over real time and allows big bursts for fast grappling and lag spikes (bunched up packets).
 */
public class GrappleMovementLimiter {
    //Blocks per tick, way faster than grappling/rockets move
    private static final double REFILL_PER_TICK = 5;
    //Blocks, covers ~1s of lag at max refill speed plus the initial launch
    private static final double MAX_BUDGET = 100;

    private static final Map<EntityPlayerMP, Budget> BUDGETS = new WeakHashMap<>();

    public static boolean tryMove(EntityPlayerMP player, double x, double y, double z) {
        long now = System.currentTimeMillis();
        Budget budget = BUDGETS.computeIfAbsent(player, p -> new Budget(now));
        budget.amount = Math.min(MAX_BUDGET, budget.amount + Math.max(0, now - budget.lastMillis) / 50D * REFILL_PER_TICK);
        budget.lastMillis = now;

        double distance = Math.sqrt(player.getDistanceSq(x, y, z));
        if (distance > budget.amount) return false;
        budget.amount -= distance;
        return true;
    }

    private static class Budget {
        private double amount = MAX_BUDGET;
        private long lastMillis;

        private Budget(long lastMillis) {
            this.lastMillis = lastMillis;
        }
    }
}
