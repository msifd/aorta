package msifeed.mc.extensions.chat.commands;

import msifeed.mc.extensions.chat.ChatHandler;
import msifeed.mc.extensions.chat.ChatMessage;
import msifeed.mc.extensions.chat.composer.Composer;
import msifeed.mc.extensions.chat.composer.SpeechType;
import msifeed.mc.sys.cmd.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GmPmCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "gmpm";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/gmpm <player> <text>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 2) {
            error(sender, getCommandUsage(sender));
            return;
        }

        final EntityPlayer player = (EntityPlayer) sender;
        if (!isGm(sender)) {
            error(sender, "You are not GM!");
            return;
        }

        final EntityPlayerMP target = getPlayer(sender, args[0]);
        if (target == null) {
            error(sender, "Player '%s' is not found.", args[0]);
            return;
        }

        final String text = Stream.of(args).skip(1).collect(Collectors.joining(" "));
        final ChatMessage message = Composer.makeMessage(SpeechType.GM, player, text);

        if (player instanceof EntityPlayerMP) {
            ChatHandler.sendMessageTo((EntityPlayerMP) player, target, message);
            message.text = target.getDisplayName() + " << " + message.text;
            player.addChatMessage(Composer.formatMessage(player, message));
        } else {
            player.addChatMessage(Composer.formatMessage(player, message));
        }
    }
}
