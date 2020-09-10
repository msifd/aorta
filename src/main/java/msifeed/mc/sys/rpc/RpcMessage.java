package msifeed.mc.sys.rpc;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class RpcMessage implements IMessage {
    String method;
    Object[] args;

    public RpcMessage() {
        // For incoming messages
    }

    RpcMessage(String method, Object... args) {
        this.method = method;
        this.args = args;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, method);
        buf.writeByte(args.length);
        for (Object obj : args)
            RpcCodec.INSTANCE.encode(buf, obj);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        method = ByteBufUtils.readUTF8String(buf);
        args = new Object[buf.readByte()];
        for (int i = 0; i < args.length; i++)
            args[i] = RpcCodec.INSTANCE.decode(buf);
    }
}
