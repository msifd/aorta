package msifeed.mc.extensions.chat.commands;

import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.extensions.chat.SpeechatRpc;
import msifeed.mc.extensions.chat.formatter.SpeechFormatter;
import msifeed.mc.sys.cmd.GmExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SuSayCommand extends GmExtCommand {
    @Override
    public String getCommandName() {
        return "susay";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/susay <player> <text>";
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        return args.length == 1
                ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames())
                : null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer))
            return;

        if (args.length < 2) {
            info(sender, getCommandUsage(sender));
            return;
        }

        final EntityPlayerMP target = getPlayer(sender, args[0]);
        if (target == null)
            return;

        final EntityPlayerMP player = (EntityPlayerMP) sender;
        final String text = Stream.of(args).skip(1).collect(Collectors.joining(" "));
        final int range = SpeechFormatter.getSpeechRange(text);
        final String senderText = "susay " + target.getDisplayName() + ": " + text;

        SpeechatRpc.sendRawTo(player, new ChatComponentText(senderText));
        SpeechatRpc.sendSpeech(target, range, new ChatComponentText(text));
        ExternalLogs.log(sender, "gm", senderText);
        ExternalLogs.log(sender, "speech", text);
    }
}
