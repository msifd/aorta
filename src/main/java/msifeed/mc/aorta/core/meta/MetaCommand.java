package msifeed.mc.aorta.core.meta;

import msifeed.mc.aorta.commands.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class MetaCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "meta";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/meta < owner <name> | add <text> | remove | clear >";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer)) {
            error(sender, "You should be at least player!");
            return;
        }
        if (args.length == 0) {
            printHelp(sender);
            return;
        }

        final EntityPlayer player = (EntityPlayer) sender;
        final ItemStack itemStack = player.getHeldItem();

        if (itemStack == null) {
            error(sender, "I don't see any item in your hand =_=");
            return;
        }

        switch (args[0]) {
            case "owner":
                if (args.length > 1)
                    MetaProvider.setOwner(itemStack, args[1]);
                else
                    error(sender, "You should pass a name");
                break;
            case "add":
                MetaProvider.addLine(itemStack, joinText(args, 1));
                break;
            case "remove":
                MetaProvider.removeLine(itemStack);
                break;
            case "clear":
                MetaProvider.clear(itemStack);
                break;
            default:
                printHelp(sender);
                break;
        }
    }

    private void printHelp(ICommandSender sender) {
        title(sender, "Meta help:");
        info(sender, " Take item in hand.");
        info(sender, " /meta owner <name> - Set owner");
        info(sender, " /meta add <text> - Add new line");
        info(sender, " /meta remove - Remove last line");
        info(sender, " /meta clear - Clear meta data");
    }

    private String joinText(String[] args, int offset) {
        final StringBuilder sb = new StringBuilder();
        for (int i = offset; i < args.length; i++) {
            sb.append(args[i]);
            if (i < args.length - 1)
                sb.append(' ');
        }
        return sb.toString();
    }
}
