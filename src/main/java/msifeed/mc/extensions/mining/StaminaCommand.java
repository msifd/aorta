package msifeed.mc.extensions.mining;

import msifeed.mc.sys.cmd.ExtCommand;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class StaminaCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "stamina";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return isGm(sender) ? "/stamina [player [percents]]" : "/stamina";
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1 && isGm(sender))
            return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        else
            return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0 || !isGm(sender)) {
            if (sender instanceof EntityPlayer)
                get(sender, (EntityPlayer) sender);
        } else {
            if (args[0].equals("?")) {
                info(sender, getCommandUsage(sender));
                return;
            }

            final EntityPlayer target = findPlayer(args[0]);
            if (target == null) {
                error(sender, "Unknown player");
                return;
            }

            if (args.length == 1) {
                get(sender, target);
            } else {
                try {
                    final int percents = Integer.valueOf(args[1]);
                    set(sender, target, percents);
                } catch (NumberFormatException e) {
                    error(sender, "Invalid number. Must be an integer.");
                }
            }
        }
    }

    private void get(ICommandSender sender, EntityPlayer target) {
        final MiningInfo info = MiningNerf.INSTANCE.updateStamina(target, false);
        final int staminaPercent = (int) (info.stamina * 100);

        final String msg = sender == target
                ? L10n.fmt("more.mining.self", staminaPercent)
                : L10n.fmt("more.mining.someone", target.getDisplayName(), staminaPercent);

        sender.addChatMessage(new ChatComponentText(msg));
    }

    private void set(ICommandSender sender, EntityPlayer target, int percents) {
        final MiningInfo info = MiningNerf.INSTANCE.setStamina(target, percents);
        final int staminaPercent = (int) (info.stamina * 100);

        final String msg = L10n.fmt("more.mining.set", target.getDisplayName(), staminaPercent);
        sender.addChatMessage(new ChatComponentText(msg));
    }
}
