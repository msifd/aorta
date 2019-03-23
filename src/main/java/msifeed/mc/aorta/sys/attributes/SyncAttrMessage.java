package msifeed.mc.aorta.sys.attributes;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class SyncAttrMessage implements IMessage {
    int entityId;
    String attrName;
    NBTTagCompound compound;

    public SyncAttrMessage() {
    }

    SyncAttrMessage(Entity entity, EntityAttribute attribute) {
        this.entityId = entity.getEntityId();
        this.attrName = attribute.getName();
        this.compound = new NBTTagCompound();
        attribute.toNBT(entity, this.compound);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        attrName = ByteBufUtils.readUTF8String(buf);
        compound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        ByteBufUtils.writeUTF8String(buf, attrName);
        ByteBufUtils.writeTag(buf, compound);
    }
}