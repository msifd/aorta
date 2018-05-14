package msifeed.mc.aorta.props;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class MessageSyncProp implements IMessage {
    int entityId;
    String propName;
    NBTTagCompound compound;

    public MessageSyncProp() {
    }

    MessageSyncProp(Entity entity, ISyncableExtProp prop) {
        this.entityId = entity.getEntityId();
        this.propName = prop.getName();
        this.compound = new NBTTagCompound();
        prop.saveNBTData(this.compound);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        propName = ByteBufUtils.readUTF8String(buf);
        compound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        ByteBufUtils.writeUTF8String(buf, propName);
        ByteBufUtils.writeTag(buf, compound);
    }

}
