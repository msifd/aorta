package msifeed.mc.aorta.environment;

import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.sys.cmd.ExtCommand;
import msifeed.mc.aorta.sys.config.ConfigManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class EnvironmentCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "aenv";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        final EntityPlayer player = (EntityPlayer) sender;
        if (!CharacterAttribute.has(player, Trait.__admin)) {
            error(sender, "You are not admin!");
            return;
        }

        if (args.length < 1) {
            printHelp(sender);
            return;
        }

        final int dim = player.worldObj.provider.dimensionId;
        final WorldEnv worldEnv = EnvironmentManager.getEnv(dim);
        switch (args[0].toLowerCase()) {
            case "help":
                break;
            case "rain":
                final WorldEnv.Rain r = worldEnv.rain;
                title(sender, "Rain info");
                info(sender, "  acc: %d +%d/-%d", r.accumulated, r.income, r.outcome);
                info(sender, "  min/max: %d/%d", r.minThreshold, r.maxThreshold);
                info(sender, "  thunder: %d", r.thunderThreshold);
                info(sender, "  dice: %d", r.rainfallDice);
                break;
            case "snow":
                worldEnv.snow = !worldEnv.snow;
                sender.addChatMessage(new ChatComponentText("snow: " + worldEnv.snow));
                break;
            case "melt":
                worldEnv.meltSnow = !worldEnv.meltSnow;
                sender.addChatMessage(new ChatComponentText("meltSnow: " + worldEnv.meltSnow));
                break;
            case "stacksnow":
                worldEnv.stackSnow = !worldEnv.stackSnow;
                sender.addChatMessage(new ChatComponentText("stackSnow: " + worldEnv.stackSnow));
                break;
        }

        ConfigManager.broadcast();
    }

    private void printHelp(ICommandSender sender) {
        title(sender, "AEnv help");
        info(sender, "current dim: %d", sender.getEntityWorld().provider.dimensionId);
        info(sender, "  rain - show rain info");
        info(sender, "  snow - toggle drop random snow");
        info(sender, "  melt - toggle melt random snow");
        info(sender, "  stacksnow - toggle snow stack");
    }
}
