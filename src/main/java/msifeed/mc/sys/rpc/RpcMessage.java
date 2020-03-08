package msifeed.mc.sys.rpc;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class RpcMessage implements IMessage, IMessageHandler<RpcMessage, IMessage> {
    String method;
    Object[] args;

    public RpcMessage() {

    }

    RpcMessage(String method, Object... args) {
        this.method = method;
        this.args = args;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, method);
        RpcSerializer.encode(buf, args);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        method = ByteBufUtils.readUTF8String(buf);
        args = RpcSerializer.decode(buf);
    }

    @Override
    public IMessage onMessage(RpcMessage message, MessageContext ctx) {
        Rpc.onMessage(message, ctx);
        return null;
    }
}
