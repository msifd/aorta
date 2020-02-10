package msifeed.mc.more.commands;

import msifeed.mc.sys.cmd.ExtCommand;
import msifeed.mc.sys.config.ConfigManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class MoreCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "morgana";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/morgana [reload]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!isAdmin(sender)) {
            error(sender, "Not enough permissions!");
            return;
        }
        if (args.length < 1) {
            sender.addChatMessage(new ChatComponentText("No arguments!"));
            return;
        }

        switch (args[0]) {
            case "reload":
                ConfigManager.reload();
                ConfigManager.broadcast();
                sender.addChatMessage(new ChatComponentText("Aorta reloaded"));
                break;
        }
    }
}
