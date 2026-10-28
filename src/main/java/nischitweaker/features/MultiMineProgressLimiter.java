package nischitweaker.features;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Limits reported mining progress to what could have been mined since starting on that block.
 * Uses real time instead of server ticks so server lag doesn't count against the player, plus leeway for network jitter and dig speed desyncs.
 */
public class MultiMineProgressLimiter {
    //Client sends a progress packet every tick while mining, sessions without packets for this long are dropped
    private static final long SESSION_TIMEOUT_MILLIS = 5000;
    private static final float SPEED_LEEWAY = 2F;
    //Server applies the in-air dig penalty if it thinks the player isn't on ground, the client might disagree
    private static final float AIR_PENALTY = 5F;
    private static final long TICK_LEEWAY = 20;

    private static final Map<EntityPlayerMP, Map<BlockPos, Session>> SESSIONS = new WeakHashMap<>();

    public static float limit(EntityPlayerMP player, BlockPos pos, float storedProgress, float relativeHardness, float value) {
        long now = System.currentTimeMillis();
        Map<BlockPos, Session> sessions = SESSIONS.computeIfAbsent(player, p -> new HashMap<>());
        sessions.values().removeIf(session -> now - session.lastMillis > SESSION_TIMEOUT_MILLIS || now < session.lastMillis);

        Session session = sessions.computeIfAbsent(pos, p -> new Session(now, storedProgress));
        session.lastMillis = now;

        float speed = relativeHardness * SPEED_LEEWAY;
        if (!player.onGround) speed *= AIR_PENALTY;
        long ticks = (now - session.startMillis) / 50 + TICK_LEEWAY;
        return Math.min(value, session.startProgress + speed * ticks);
    }

    private static class Session {
        private final long startMillis;
        private final float startProgress;
        private long lastMillis;

        private Session(long startMillis, float startProgress) {
            this.startMillis = startMillis;
            this.startProgress = startProgress;
            this.lastMillis = startMillis;
        }
    }
}
