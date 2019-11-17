package msifeed.mc.aorta.chat.net;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.composer.SpeechType;

public class ChatMessage implements IMessage {
    public SpeechType type;
    public int radius;
    public int senderId;
    public String speaker;
    public Language language;
    public String text;

    @Override
    public void fromBytes(ByteBuf buf) {
        type = SpeechType.values()[buf.readByte()];
        language = Language.values()[buf.readByte()];
        radius = buf.readInt();
        senderId = buf.readInt();
        speaker = ByteBufUtils.readUTF8String(buf);
        text = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(type.ordinal());
        buf.writeByte(language.ordinal());
        buf.writeInt(radius);
        buf.writeInt(senderId);
        ByteBufUtils.writeUTF8String(buf, speaker);
        ByteBufUtils.writeUTF8String(buf, text);
    }

}
