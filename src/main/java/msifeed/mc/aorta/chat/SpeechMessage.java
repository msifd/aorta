package msifeed.mc.aorta.chat;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.IChatComponent;

public class SpeechMessage implements IMessage {
    public Language language;
    public int radius;
    public String sender;
    public IChatComponent chatComponent;

    @Override
    public void fromBytes(ByteBuf buf) {
        final byte languageOrd = buf.readByte();
        radius = buf.readInt();
        sender = ByteBufUtils.readUTF8String(buf);
        final String componentStr = ByteBufUtils.readUTF8String(buf);

        language = Language.values()[languageOrd];
        chatComponent = IChatComponent.Serializer.func_150699_a(componentStr);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        final String componentStr = IChatComponent.Serializer.func_150696_a(chatComponent);

        buf.writeByte(language.ordinal());
        buf.writeInt(radius);
        ByteBufUtils.writeUTF8String(buf, sender);
        ByteBufUtils.writeUTF8String(buf, componentStr);
    }
}
