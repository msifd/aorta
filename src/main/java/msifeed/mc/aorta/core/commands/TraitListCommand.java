package msifeed.mc.aorta.core.commands;

import msifeed.mc.aorta.core.attributes.TraitsAttribute;
import msifeed.mc.commons.ExtCommand;
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
        return "/traits";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityLivingBase)) {
            send(sender, "You should be at least entity!");
            return;
        }
        printTraits(sender, (EntityLivingBase) sender);
    }

    private void printTraits(ICommandSender sender, EntityLivingBase entity) {
        TraitsAttribute.INSTANCE.get(entity).ifPresent(traits -> {
            final Set<String> names = traits.stream().map(Enum::toString).collect(Collectors.toSet());
            final String theNiceString = joinNiceStringFromCollection(names);
            title(sender, "Your traits:", entity.getCommandSenderName());
            send(sender, "  " + theNiceString);
        });
    }
}
