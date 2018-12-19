package msifeed.mc.aorta.locks;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;

public class DigitalLockMessage implements IMessage, IMessageHandler<DigitalLockMessage, IMessage> {
    DigitalLockAction action;
    String secret;
    int x;
    int y;
    int z;

    public DigitalLockMessage() {}

    public DigitalLockMessage(LockTileEntity lock, DigitalLockAction action, String secret) {
        this.action = action;
        this.secret = secret;
        this.x = lock.xCoord;
        this.y = lock.yCoord;
        this.z = lock.zCoord;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        action = DigitalLockAction.values()[buf.readByte()];
        secret = ByteBufUtils.readUTF8String(buf);
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte((byte) action.ordinal());
        ByteBufUtils.writeUTF8String(buf, secret);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    @Override
    public IMessage onMessage(DigitalLockMessage message, MessageContext ctx) {
        final World world = ctx.getServerHandler().playerEntity.getEntityWorld();
        LockTileEntity lock = LockTileEntity.find(world, message.x, message.y, message.z);
        if (lock == null || lock.getLockType() != LockType.DIGITAL)
            return null;

        switch (message.action) {
            case LOCK:
            case UNLOCK:
                if (lock.canUnlockWith(message.secret))
                    lock.setLocked(message.action == DigitalLockAction.LOCK);
                break;
            case INSTALL:
                break;
        }

        return null;
    }

}
