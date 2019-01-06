package msifeed.mc.aorta.rpc;

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

    public RpcMessage(String method, Object... args) {
        this.method = method;
        this.args = args;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, method);
        buf.writeByte(args.length);
        for (Object o : args)
            writeSome(buf, o);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        method = ByteBufUtils.readUTF8String(buf);
        args = new Object[buf.readByte()];
        for (int i = 0; i < args.length; ++i)
            args[i] = readSome(buf);
    }

    @Override
    public IMessage onMessage(RpcMessage message, MessageContext ctx) {
        Rpc.onMessage(message, ctx);
        return null;
    }

    private static void writeSome(ByteBuf buf, Object o) {
        buf.writeByte(getTypeByte(o));
        if (o instanceof Byte)
            buf.writeByte((byte) o);
        else if (o instanceof Short)
            buf.writeShort((short) o);
        else if (o instanceof Integer)
            buf.writeInt((int) o);
        else if (o instanceof String)
            ByteBufUtils.writeUTF8String(buf, (String) o);
    }

    private static Object readSome(ByteBuf buf) {
        final byte type = buf.readByte();
        switch (type) {
            case 0x01:
                return buf.readByte();
            case 0x02:
                return buf.readShort();
            case 0x03:
                return buf.readInt();
            case 0x04:
                return ByteBufUtils.readUTF8String(buf);
            default:
                return null;
        }
    }

    private static byte getTypeByte(Object o) {
        if (o instanceof Byte)
            return 0x01;
        else if (o instanceof Short)
            return 0x02;
        else if (o instanceof Integer)
            return 0x03;
        else if (o instanceof String)
            return 0x04;
        else
            throw new RuntimeException("unsupported type " + o.getClass().getName());
    }
}
