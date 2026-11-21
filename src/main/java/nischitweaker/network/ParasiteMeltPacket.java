package nischitweaker.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nischitweaker.features.antilure.AntiLureClient;

public class ParasiteMeltPacket implements IMessage {
    private int entityId;
    private int remaining;
    private int duration;

    public ParasiteMeltPacket() {}

    public ParasiteMeltPacket(int entityId, int remaining, int duration) {
        this.entityId = entityId;
        this.remaining = remaining;
        this.duration = duration;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.remaining = buf.readInt();
        this.duration = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.remaining);
        buf.writeInt(this.duration);
    }

    public static class Handler implements IMessageHandler<ParasiteMeltPacket, IMessage> {
        @Override
        public IMessage onMessage(ParasiteMeltPacket message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> AntiLureClient.startMelting(message.entityId, message.remaining, message.duration));
            return null;
        }
    }
}
