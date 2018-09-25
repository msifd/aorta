package msifeed.mc.aorta.config;

import msifeed.mc.commons.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class ReloadCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "aorta";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/aorta [reload]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.addChatMessage(new ChatComponentText("No arguments!"));
            return;
        }

        switch (args[0]) {
            case "reload":
                ConfigManager.INSTANCE.reloadConfig();
                ConfigManager.INSTANCE.broadcastConfig();
                sender.addChatMessage(new ChatComponentText("Aorta reloaded"));
                break;
        }
    }
}
