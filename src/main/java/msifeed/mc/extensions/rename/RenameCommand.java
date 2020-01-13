package msifeed.mc.extensions.rename;

import msifeed.mc.sys.cmd.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class RenameCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "rename";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/rename";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer)) {
            error(sender, "You should be at least player!");
            return;
        }

        final EntityPlayer player = (EntityPlayer) sender;
        final ItemStack itemStack = player.getHeldItem();

        if (itemStack == null) {
            error(sender, "I don't see any item in your hand =_=");
            return;
        }

        if (sender instanceof EntityPlayerMP)
            RenameRpc.openRenameGui((EntityPlayerMP) sender);
    }
}
