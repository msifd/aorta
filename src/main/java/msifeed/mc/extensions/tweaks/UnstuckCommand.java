package msifeed.mc.extensions.tweaks;

import msifeed.mc.sys.cmd.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class UnstuckCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "unstuck";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/unstuck";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer))
            return;

        final EntityPlayer player = (EntityPlayer) sender;
        player.setPositionAndUpdate(player.posX, player.posY + 1, player.posZ);
    }
}
