package msifeed.mc.aorta.chat.net;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import msifeed.mc.aorta.chat.Language;

public class SpeechMessage implements IMessage {
    public Type type = Type.SPEECH;
    public int radius;
    public String speaker;
    public Language language;
    public String text;

    @Override
    public void fromBytes(ByteBuf buf) {
        type = Type.values()[buf.readByte()];
        language = Language.values()[buf.readByte()];
        speaker = ByteBufUtils.readUTF8String(buf);
        text = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(type.ordinal());
        buf.writeByte(language.ordinal());
        ByteBufUtils.writeUTF8String(buf, speaker);
        ByteBufUtils.writeUTF8String(buf, text);
    }

    public enum Type {
        SPEECH, OFFTOP
    }
}
