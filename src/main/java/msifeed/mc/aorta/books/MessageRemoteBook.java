package msifeed.mc.aorta.books;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageRemoteBook implements IMessage, IMessageHandler<MessageRemoteBook, IMessage> {
    private Type type;
    private String text;

    public MessageRemoteBook() {
    }

    public MessageRemoteBook(boolean check) {
        this.type = Type.CHECK;
        this.text = check ? "y" : "n";
    }

    public MessageRemoteBook(Type type, String text) {
        this.type = type;
        this.text = text;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        type = Type.values()[buf.readByte()];
        text = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(type.ordinal());
        ByteBufUtils.writeUTF8String(buf, text);
    }

    @Override
    public IMessage onMessage(MessageRemoteBook message, MessageContext ctx) {
        if (ctx.side.isServer()) {
            switch (message.type) {
                case CHECK:
                    return new MessageRemoteBook(RemoteBookManager.INSTANCE.checkBook(message.text));
                case SIGN:
                    RemoteBookManager.INSTANCE.signBook(ctx.getServerHandler().playerEntity, message.text);
                    return null;
                case REQUEST_RESPONSE:
                    return new MessageRemoteBook(Type.REQUEST_RESPONSE, RemoteBookManager.INSTANCE.loadBook(message.text));
            }
        } else {
            switch (message.type) {
                case CHECK:
                    RemoteBookManager.INSTANCE.receiveCheck(message.text.equals("y"));
                    break;
                case REQUEST_RESPONSE:
                    RemoteBookManager.INSTANCE.receiveResponse(message.text);
                    break;
            }
        }
        return null;
    }

    public enum Type {
        CHECK, REQUEST_RESPONSE, SIGN
    }
}
