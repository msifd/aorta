package msifeed.mc.extensions.chat.commands;

import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.extensions.chat.SpeechatRpc;
import msifeed.mc.more.More;
import msifeed.mc.sys.cmd.PlayerExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public class WhisperCommand extends PlayerExtCommand {
    @Override
    public String getCommandName() {
        return "whisper";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/whisper <text>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0 || !(sender instanceof EntityPlayerMP))
            return;

        final int[] ranges = More.DEFINES.get().chat.speechRadius;
        final int rangeLevel = -2;
        final int range = ranges[(ranges.length - 1) / 2 + rangeLevel];

        final EntityPlayerMP player = (EntityPlayerMP) sender;
        final String text = "((" + String.join(" ", args) + "))";
        SpeechatRpc.sendSpeech(player, range, new ChatComponentText(text));
        ExternalLogs.log(sender, "speech",  text);
    }
}
