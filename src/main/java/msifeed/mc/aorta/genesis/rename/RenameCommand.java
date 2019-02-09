package msifeed.mc.aorta.genesis.rename;

import msifeed.mc.aorta.commands.ExtCommand;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.traits.Trait;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class RenameCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "rename";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/rename [title|remove|clear|set <key> :] [text]";
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

        if (args.length == 0) {
            printHelp(sender);
            return;
        }
        if (itemStack == null) {
            error(sender, "I don't see any item in your hand =_=");
            return;
        }

        switch (args[0]) {
            default:
                RenameProvider.addDescription(itemStack, joinText(args, 0));
                break;
            case "title":
                RenameProvider.setTitle(itemStack, args.length > 1 ? joinText(args, 1) : null);
                break;
            case "remove":
                RenameProvider.removeDescriptionLine(itemStack);
                break;
            case "clear":
                RenameProvider.clearDescription(itemStack);
                break;
            case "set":
                if (!CharacterAttribute.has(player, Trait.gm)) {
                    error(sender, "You are not GM!");
                    return;
                }
                if (args.length < 2) {
                    error(sender, "You should pass a key!");
                    return;
                }
                final String[] parts = joinText(args, 1).split(":");
                final String key = parts[0].trim();
                final String value = parts.length > 1 ? parts[1].trim() : null;
                RenameProvider.setValue(itemStack, key, value);
                break;
        }
    }

    private void printHelp(ICommandSender sender) {
        title(sender, "Rename help:");
        info(sender, " Take item in hand.");
        info(sender, " Adding title or description hides default one.");
        info(sender, " /rename - Show this help.");
        info(sender, " /rename title <text> - Set item's title. Resets if empty.");
        info(sender, " /rename <text> - Add new line to desctiprion.");
        info(sender, " /rename remove - Remove last line");
        info(sender, " /rename clear - Clear description");
        info(sender, " GM only:");
        info(sender, "  /rename set <key> : [value] - Set custom field. ':' breaks key and value.");
    }
}
