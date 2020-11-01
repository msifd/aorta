package msifeed.mc.sys.rpc;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.INetHandler;

public class RpcChannelHandler extends SimpleChannelInboundHandler<RpcMessage> {
    private final RpcChannel rpc;
    private final Side side;

    public RpcChannelHandler(RpcChannel rpc, Side side) {
        super(RpcMessage.class);
        this.rpc = rpc;
        this.side = side;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) throws Exception {
        final INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
//        FMLCommonHandler.instance().getWorldThread(netHandler).addScheduledTask(() -> {
//            rpc.invoke(msg, netHandler, side);
//        });
        rpc.invoke(msg, netHandler, side);
    }
}
