package msifeed.mc.aorta.environment;

import msifeed.mc.aorta.commands.ExtCommand;
import msifeed.mc.aorta.config.ConfigManager;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.traits.Trait;
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
        if (!CharacterAttribute.has(player, Trait.gm)) {
            error(sender, "You are not GM!");
            return;
        }

        if (args.length < 1)
            return;

        final int dim = player.worldObj.provider.dimensionId;
        final WorldEnv worldEnv = EnvironmentManager.getEnv(dim);
        switch (args[0].toLowerCase()) {
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

        ConfigManager.INSTANCE.broadcast();
    }
}
