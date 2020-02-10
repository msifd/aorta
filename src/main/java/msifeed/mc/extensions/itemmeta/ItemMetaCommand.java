package msifeed.mc.extensions.itemmeta;

import msifeed.mc.sys.cmd.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemMetaCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "meta";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/meta < owner <value> | add <text> | remove | clear >";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer)) {
            error(sender, "You should be at least player!");
            return;
        }

        final EntityPlayer player = (EntityPlayer) sender;
        final ItemStack itemStack = player.getHeldItem();

        if (!isGm(sender)) {
            error(sender, "You are not GM!");
            return;
        }
        if (args.length == 0) {
            printHelp(sender);
            return;
        }
        if (itemStack == null) {
            error(sender, "I don't see any item in your hand =_=");
            return;
        }

        switch (args[0]) {
            case "owner":
                if (args.length > 1)
                    ItemMetaProvider.setOwner(itemStack, args[1]);
                else
                    error(sender, "You should pass a value");
                break;
            case "add":
                ItemMetaProvider.addLine(itemStack, joinText(args, 1));
                break;
            case "remove":
                ItemMetaProvider.removeLine(itemStack);
                break;
            case "clear":
                ItemMetaProvider.clear(itemStack);
                break;
            default:
                printHelp(sender);
                break;
        }
    }

    private void printHelp(ICommandSender sender) {
        title(sender, "Meta help:");
        info(sender, " Take item in hand.");
        info(sender, " /meta owner <value> - Set owner");
        info(sender, " /meta add <text> - Add new line");
        info(sender, " /meta remove - Remove last line");
        info(sender, " /meta clear - Clear meta data");
    }
}
