package msifeed.mc.more.commands;

import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.extensions.chat.SpeechatRpc;
import msifeed.mc.extensions.chat.formatter.MiscFormatter;
import msifeed.mc.more.More;
import msifeed.mc.more.crabs.rolls.Dices;
import msifeed.mc.sys.cmd.PlayerExtCommand;
import msifeed.mc.sys.utils.ChatUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

public class RollCommand extends PlayerExtCommand {
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
        if (args.length == 0 || !(sender instanceof EntityPlayerMP))
            return;

        final int sides;
        if (args[0].equals("coin")) {
            sides = 2;
        } else {
            try {
                sides = MathHelper.clamp_int(Integer.parseInt(args[0]), 2, 1000);
            } catch (NumberFormatException e) {
                return;
            }
        }

        final int range = More.DEFINES.get().chat.rollRadius;

        final EntityPlayerMP player = (EntityPlayerMP) sender;
        final String name = ChatUtils.getPrettyName(player);
        final int roll = Dices.dice(sides);
        final IChatComponent cc = MiscFormatter.formatRoll(name, sides, roll);

        SpeechatRpc.sendRaw(player, range, cc);
        ExternalLogs.log(player, "dice", cc.getUnformattedText());
    }
}
