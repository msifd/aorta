package msifeed.mc.more.commands;

import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.extensions.chat.ChatHandler;
import msifeed.mc.extensions.chat.composer.Composer;
import msifeed.mc.extensions.chat.composer.SpeechType;
import msifeed.mc.more.crabs.rolls.Dices;
import msifeed.mc.sys.cmd.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class RollCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "roll";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/roll coin | <sides>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0)
            return;

        final int sides;
        if (args[0].equals("coin")) {
            sides = 2;
        } else {
            try {
                sides = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                return;
            }
        }

        if (sides <= 0 || sides > 1000)
            return;

        final int roll = Dices.dice(sides);
        final String name = sender instanceof EntityPlayer
                ? ((EntityPlayer) sender).getDisplayName()
                : sender.getCommandSenderName();
        final String text = String.format("\u00a7r%s\u00a76 d%d = %d", name, sides, roll);

        if (sender instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) sender;
            ChatHandler.sendSystemChatMessage(player, Composer.makeMessage(SpeechType.ROLL, player, text));
            ExternalLogs.log(player, "dice", String.format("%s d%d = %d", name, sides, roll));
        } else {
            sender.addChatMessage(new ChatComponentText(text));
        }
    }
}
