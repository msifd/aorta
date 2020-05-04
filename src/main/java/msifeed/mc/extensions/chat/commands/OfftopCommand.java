package msifeed.mc.extensions.chat.commands;

import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.extensions.chat.SpeechatRpc;
import msifeed.mc.extensions.chat.formatter.MiscFormatter;
import msifeed.mc.more.More;
import msifeed.mc.sys.cmd.PlayerExtCommand;
import msifeed.mc.sys.utils.ChatUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public class OfftopCommand extends PlayerExtCommand {
    @Override
    public String getCommandName() {
        return "o";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/o <text>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0 || !(sender instanceof EntityPlayerMP))
            return;

        final int range = More.DEFINES.get().chat.offtopRadius;
        final EntityPlayerMP player = (EntityPlayerMP) sender;
        final String name = ChatUtils.getPrettyName(player);
        final String text = String.join(" ", args);
        SpeechatRpc.sendRaw(player, range, MiscFormatter.formatOfftop(name, text));
        ExternalLogs.log(sender, "offtop",  text);
    }
}
