package msifeed.mc.aorta.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public abstract class ExtCommand extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        if (getRequiredPermissionLevel() <= 0)
            return true;
        return super.canCommandSenderUseCommand(sender);
    }

    protected void title(ICommandSender sender, String format, Object... args) {
        sendColored(sender, EnumChatFormatting.BLUE, format, args);
    }

    protected void info(ICommandSender sender, String format, Object... args) {
        send(sender, format, args);
    }

    protected void success(ICommandSender sender, String format, Object... args) {
        sendColored(sender, EnumChatFormatting.GREEN, format, args);
    }

    protected void error(ICommandSender sender, String format, Object... args) {
        sendColored(sender, EnumChatFormatting.RED, format, args);
    }

    protected void send(ICommandSender sender, String format, Object... args) {
        sender.addChatMessage(new ChatComponentText(args.length == 0 ? format : String.format(format, args)));
    }

    protected void sendColored(ICommandSender sender, EnumChatFormatting color, String format, Object... args) {
        final ChatComponentText c = new ChatComponentText(args.length == 0 ? format : String.format(format, args));
        c.getChatStyle().setColor(color);
        sender.addChatMessage(c);
    }

    protected static String joinText(String[] args, int offset) {
        final StringBuilder sb = new StringBuilder();
        for (int i = offset; i < args.length; i++) {
            sb.append(args[i]);
            if (i < args.length - 1)
                sb.append(' ');
        }
        return sb.toString();
    }
}
