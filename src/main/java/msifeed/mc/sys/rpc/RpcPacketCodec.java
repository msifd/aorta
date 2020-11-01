package msifeed.mc.sys.rpc;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import net.minecraft.network.PacketBuffer;

import java.lang.ref.WeakReference;
import java.util.List;

@ChannelHandler.Sharable
public class RpcPacketCodec extends MessageToMessageCodec<FMLProxyPacket, RpcMessage> {
    private final RpcCodec codec;

    public RpcPacketCodec(RpcCodec codec) {
        this.codec = codec;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception
    {
        super.handlerAdded(ctx);
        ctx.attr(FMLIndexedMessageToMessageCodec.INBOUNDPACKETTRACKER).set(new ThreadLocal<>());
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage msg, List<Object> out) throws Exception {
        final String channel = ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get();
        final FMLProxyPacket proxy = new FMLProxyPacket(new PacketBuffer(msg.encode(codec)), channel);

        final WeakReference<FMLProxyPacket> ref = ctx.attr(FMLIndexedMessageToMessageCodec.INBOUNDPACKETTRACKER).get().get();
        final FMLProxyPacket old = ref == null ? null : ref.get();
        if (old != null)
            proxy.setDispatcher(old.getDispatcher());
        out.add(proxy);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception {
        final ByteBuf payload = msg.payload();
        if (payload.readableBytes() < 1) {
            FMLLog.getLogger().error("The RpcPacketCodec has received an empty buffer on channel {}, likely a result of a LAN server issue. Pipeline parts : {}", ctx.channel().attr(NetworkRegistry.FML_CHANNEL), ctx.pipeline().toString());
            return;
        }

        final RpcMessage message = new RpcMessage(codec, payload);
        ctx.attr(FMLIndexedMessageToMessageCodec.INBOUNDPACKETTRACKER).get().set(new WeakReference<>(msg));
        out.add(message);
    }
}
