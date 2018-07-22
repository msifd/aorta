package msifeed.mc.commons;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public abstract class ExtCommand extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    protected void send(ICommandSender sender, String message) {
        sender.addChatMessage(new ChatComponentText(message));
    }

    protected void send(ICommandSender sender, String format, Object... args) {
        sender.addChatMessage(new ChatComponentText(String.format(format, args)));
    }
}
