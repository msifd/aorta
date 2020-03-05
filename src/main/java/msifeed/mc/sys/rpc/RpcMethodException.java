package msifeed.mc.sys.rpc;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class RpcMethodException extends RuntimeException {
    private final ICommandSender receiver;

    public RpcMethodException(ICommandSender receiver, String message) {
        super(message);
        this.receiver = receiver;
    }

    void sendChatMessage() {
        receiver.addChatMessage(new ChatComponentText(getMessage()));
    }
}
