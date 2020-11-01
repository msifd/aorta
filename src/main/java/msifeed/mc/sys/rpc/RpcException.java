package msifeed.mc.sys.rpc;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class RpcException extends RuntimeException {
    private final ICommandSender sender;

    public RpcException(ICommandSender sender, String msg) {
        super(msg);
        this.sender = sender;
    }

    public void send() {
        sender.addChatMessage(new ChatComponentText("Error: " + getMessage()));
    }
}
