package msifeed.mc.sys.rpc;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;

final class RpcSerializer {
    static void encode(ByteBuf buf, Object[] args) {
        buf.writeByte(args.length);
        for (Object o : args) {
            final TypeId t = getTypeId(o.getClass());
            if (t == null)
                throw new RuntimeException("unsupported rpc method arg type: " + o.getClass().getName());
            buf.writeByte(t.ordinal());
            switch (t) {
                case BOOL:
                    buf.writeBoolean((boolean) o);
                    break;
                case SHORT:
                    buf.writeShort((short) o);
                    break;
                case INT:
                    buf.writeInt((int) o);
                    break;
                case LONG:
                    buf.writeLong((long) o);
                    break;
                case FLOAT:
                    buf.writeFloat((float) o);
                    break;
                case DOUBLE:
                    buf.writeDouble((double) o);
                    break;
                case STRING:
                    ByteBufUtils.writeUTF8String(buf, (String) o);
                    break;
                case NBT:
                    ByteBufUtils.writeTag(buf, (NBTTagCompound) o);
                    break;
                case CHAT:
                    ByteBufUtils.writeUTF8String(buf, IChatComponent.Serializer.func_150696_a((IChatComponent) o));
                    break;
            }
        }
    }

    static Object[] decode(ByteBuf buf) {
        final int len = buf.readByte();
        final Object[] args = new Object[len];
        final TypeId[] types = TypeId.values();

        for (int i = 0; i < len; i++) {
            final int typeByte = buf.readByte();
            if (typeByte >= types.length)
                throw new RuntimeException("unknown rpc type byte: " + typeByte);
            final TypeId t = types[typeByte];

            switch (t) {
                case BOOL:
                    args[i] = buf.readBoolean();
                    break;
                case SHORT:
                    args[i] = buf.readShort();
                    break;
                case INT:
                    args[i] = buf.readInt();
                    break;
                case LONG:
                    args[i] = buf.readLong();
                    break;
                case FLOAT:
                    args[i] = buf.readFloat();
                    break;
                case DOUBLE:
                    args[i] = buf.readDouble();
                    break;
                case STRING:
                    args[i] = ByteBufUtils.readUTF8String(buf);
                    break;
                case NBT:
                    args[i] = ByteBufUtils.readTag(buf);
                    break;
                case CHAT:
                    args[i] = IChatComponent.Serializer.func_150699_a(ByteBufUtils.readUTF8String(buf));
                    break;
            }
        }

        return args;
    }

    static TypeId getTypeId(Class<?> c) {
        for (TypeId t : TypeId.values()) {
            if (t.type.isAssignableFrom(c) || c == t.boxed)
                return t;
        }
        return null;
    }

    enum TypeId {
        BOOL(Boolean.TYPE, Boolean.class),
        BYTE(Byte.TYPE, Byte.class), SHORT(Short.TYPE, Short.class), INT(Integer.TYPE, Integer.class), LONG(Long.TYPE, Long.class),
        FLOAT(Float.TYPE, Float.class), DOUBLE(Double.TYPE, Double.class),
        STRING(String.class),
        NBT(NBTTagCompound.class),
        CHAT(IChatComponent.class)
        ;

        final Class<?> type;
        final Class<?> boxed;

        TypeId(Class<?> type) {
            this.type = type;
            this.boxed = null;
        }

        TypeId(Class<?> type, Class<?> boxed) {
            this.type = type;
            this.boxed = boxed;
        }
    }
}
