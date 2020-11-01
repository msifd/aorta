package msifeed.mc.sys.rpc;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class RpcMessage {
    final String method;
    final Object[] args;

    RpcMessage(String method, Object... args) {
        this.method = method;
        this.args = args;
    }

    RpcMessage(RpcCodec codec, ByteBuf buf) {
        method = ByteBufUtils.readUTF8String(buf);
        args = new Object[buf.readByte()];
        for (int i = 0; i < args.length; i++)
            args[i] = codec.decode(buf);
    }

    ByteBuf encode(RpcCodec codec) {
        final ByteBuf buf = Unpooled.buffer();

        ByteBufUtils.writeUTF8String(buf, method);
        buf.writeByte(args.length);
        for (Object obj : args)
            codec.encode(buf, obj);

        return buf;
    }
}
