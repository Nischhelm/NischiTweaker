package nischitweaker.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import nischitweaker.Tags;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Tags.MODID);

    public static void init() {
        int id = 0;
        INSTANCE.registerMessage(CreeperConfettiPacket.Handler.class, CreeperConfettiPacket.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(ParasiteMeltPacket.Handler.class, ParasiteMeltPacket.class, id++, Side.CLIENT);
    }
}
