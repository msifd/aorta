package msifeed.mc.extensions.environment;

import msifeed.mc.sys.cmd.GmExtCommand;
import msifeed.mc.sys.config.ConfigManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class EnvironmentCommand extends GmExtCommand {
    @Override
    public String getCommandName() {
        return "aenv";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/aenv";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        final EntityPlayer player = (EntityPlayer) sender;

        if (args.length < 1) {
            printHelp(sender);
            return;
        }

        final int dim = player.worldObj.provider.dimensionId;
        final WorldEnv worldEnv = EnvironmentManager.getEnv(dim);
        switch (args[0].toLowerCase()) {
            case "rain":
                final WorldEnv.Rain r = worldEnv.rain;
                title(sender, "Rain info");
                info(sender, "  acc: %d +%d/-%d", r.accumulated, r.income, r.outcome);
                info(sender, "  min/max: %d/%d", r.minThreshold, r.maxThreshold);
                info(sender, "  thunder: %d", r.thunderThreshold);
                info(sender, "  dice: %d", r.rainfallDice);
                break;
            case "add":
                final int mod = parseInt(sender, args[1]);
                worldEnv.rain.accumulated += mod;
                info(sender, "Added %d to the rain acc (current: %d)", mod, worldEnv.rain.accumulated);
                break;
//            case "snow":
//                worldEnv.snow = !worldEnv.snow;
//                sender.addChatMessage(new ChatComponentText("snow: " + worldEnv.snow));
//                break;
//            case "melt":
//                worldEnv.meltSnow = !worldEnv.meltSnow;
//                sender.addChatMessage(new ChatComponentText("meltSnow: " + worldEnv.meltSnow));
//                break;
//            case "stacksnow":
//                worldEnv.stackSnow = !worldEnv.stackSnow;
//                sender.addChatMessage(new ChatComponentText("stackSnow: " + worldEnv.stackSnow));
//                break;
        }
    }

    private void printHelp(ICommandSender sender) {
        title(sender, "AEnv help");
        info(sender, "current dim: %d", sender.getEntityWorld().provider.dimensionId);
        info(sender, "  rain - show rain info");
        info(sender, "  add - modify rain accumulator");
//        info(sender, "  snow - toggle drop random snow");
//        info(sender, "  melt - toggle melt random snow");
//        info(sender, "  stacksnow - toggle snow stack");
    }
}
