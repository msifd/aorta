package msifeed.mc.extensions.chat.commands;

import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.extensions.chat.GmSpeech;
import msifeed.mc.extensions.chat.SpeechatRpc;
import msifeed.mc.extensions.chat.formatter.MiscFormatter;
import msifeed.mc.sys.cmd.GmExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GmPmCommand extends GmExtCommand {
    @Override
    public String getCommandName() {
        return "gmpm";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/gmpm <player> <text>";
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        return args.length == 1
                ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames())
                : null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayerMP)) {
            error(sender, "You should be at least player!");
            return;
        }
        if (!isGm(sender)) {
            error(sender, "You are not GM!");
            return;
        }
        if (args.length < 2) {
            error(sender, getCommandUsage(sender));
            return;
        }

        final EntityPlayerMP target = getPlayer(sender, args[0]);
        if (target == null) {
            error(sender, "Player '%s' is not found.", args[0]);
            return;
        }

        final EntityPlayerMP player = (EntityPlayerMP) sender;
        final GmSpeech.Preferences prefs = GmSpeech.get(player.getCommandSenderName());
        final String text = Stream.of(args).skip(1).collect(Collectors.joining(" "));
        final String senderText = "gmpm " + target.getDisplayName() + ": " + text;

        SpeechatRpc.sendRawTo(player, new ChatComponentText(senderText));
        SpeechatRpc.sendRawTo(target, MiscFormatter.formatGmSay(prefs, new ChatComponentText(text)));
        ExternalLogs.log(sender, "gm", senderText);
    }
}
