package msifeed.mc.aorta.chat.net;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import msifeed.mc.aorta.chat.Language;
import net.minecraft.util.IChatComponent;

public class SpeechMessage implements IMessage {
    public Type type = Type.SPEECH;
    public int radius;
    public String speaker;
    public Language language;
    public IChatComponent chatComponent;

    @Override
    public void fromBytes(ByteBuf buf) {
        type = Type.values()[buf.readByte()];
        language = Language.values()[buf.readByte()];
        speaker = ByteBufUtils.readUTF8String(buf);
        final String componentStr = ByteBufUtils.readUTF8String(buf);

        chatComponent = IChatComponent.Serializer.func_150699_a(componentStr);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        final String componentStr = IChatComponent.Serializer.func_150696_a(chatComponent);

        buf.writeByte(type.ordinal());
        buf.writeByte(language.ordinal());
        ByteBufUtils.writeUTF8String(buf, speaker);
        ByteBufUtils.writeUTF8String(buf, componentStr);
    }

    public enum Type {
        SPEECH, OFFTOP
    }
}
