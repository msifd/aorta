package msifeed.mc.aorta.core.commands;

import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.sys.cmd.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;

import java.util.Set;
import java.util.stream.Collectors;

public class TraitListCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "traits";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/traits [player]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        final EntityLivingBase target;

        if (args.length > 0) {
            final EntityLivingBase player = findPlayer(args[0]);
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

        printTraits(sender, target);
    }

    private void printTraits(ICommandSender sender, EntityLivingBase entity) {
        final Character c = CharacterAttribute.require(entity);
        final Set<String> names = c.traits.stream().map(Enum::toString).collect(Collectors.toSet());
        final String theNiceString = joinNiceStringFromCollection(names);
        title(sender, "%s's traits:", entity.getCommandSenderName(), entity.getCommandSenderName());
        send(sender, "  " + theNiceString);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}
