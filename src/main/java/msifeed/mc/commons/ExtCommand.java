package msifeed.mc.commons;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public abstract class ExtCommand extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    protected void send(ICommandSender sender, String format, Object... args) {
        send(sender, String.format(format, args));
    }

    protected void send(ICommandSender sender, String message) {
        sender.addChatMessage(new ChatComponentText(message));
    }

    protected void error(ICommandSender sender, String message) {
        final ChatComponentText c = new ChatComponentText(message);
        c.getChatStyle().setColor(EnumChatFormatting.RED);
        sender.addChatMessage(c);
    }
}
