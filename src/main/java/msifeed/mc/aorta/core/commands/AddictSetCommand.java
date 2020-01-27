package msifeed.mc.aorta.core.commands;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.sys.cmd.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;

public class AddictSetCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "addict";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/addict <drugType> <0 - 4> [player]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            error(sender, "Usage: " + getCommandUsage(sender));
            return;
        }

        final String drugType = args[0].toLowerCase();
        final EntityLivingBase target;
        final int value;

        if (args.length > 2) {
            final EntityLivingBase player = findPlayer(args[2]);
            if (player == null) {
                error(sender, "Unknown player");
                return;
            }
            target = player;
        } else {
            if (!(sender instanceof EntityLivingBase)) {
                error(sender, "You should be at least entity!");
                return;
            }
            target = (EntityLivingBase) sender;
        }

        try {
            value = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return;
        }

        setAddict(sender, target, drugType, value);
    }

    private void setAddict(ICommandSender sender, EntityLivingBase entity, String drugType, int value) {
        final Character c = CharacterAttribute.require(entity);
        if (value == 0)
            c.addictions.remove(drugType);
        else
            c.addictions.put(drugType, value - 1);

        CharacterAttribute.INSTANCE.set(entity, c);

        info(sender, "Addiction to '%s' was set to %d", drugType, value);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 1;
    }
}
