package msifeed.mc.sys.attributes;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class SyncAttrMessage implements IMessage {
    int entityId;
    String attrName;
    NBTTagCompound compound = null;

    static SyncAttrMessage create(Entity entity, EntityAttribute attribute) {
        final SyncAttrMessage msg = new SyncAttrMessage();

        msg.entityId = entity.getEntityId();
        msg.attrName = attribute.getName();
        msg.compound = attribute.toNBT(entity);

        return msg;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        attrName = ByteBufUtils.readUTF8String(buf);
        if (buf.readBoolean())
            compound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        ByteBufUtils.writeUTF8String(buf, attrName);
        buf.writeBoolean(compound != null);
        if (compound != null)
            ByteBufUtils.writeTag(buf, compound);
    }
}