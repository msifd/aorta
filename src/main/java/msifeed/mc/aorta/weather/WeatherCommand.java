package msifeed.mc.aorta.weather;

import msifeed.mc.aorta.commands.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class WeatherCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "aweather";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        EntityPlayer player = (EntityPlayer) sender;

        if (args.length < 1)
            return;

        switch (args[0]) {
            case "winter":
                WeatherManager.INSTANCE.toggleWinter(player.worldObj);
                break;
            case "snow":
                WeatherManager.INSTANCE.toggleSnow(player.worldObj);
                break;
        }
    }
}
