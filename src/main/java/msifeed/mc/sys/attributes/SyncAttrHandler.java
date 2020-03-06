package msifeed.mc.sys.attributes;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class SyncAttrHandler implements IMessageHandler<SyncAttrMessage, IMessage> {
    @Override
    public IMessage onMessage(SyncAttrMessage message, MessageContext ctx) {
        return null;
    }
}
