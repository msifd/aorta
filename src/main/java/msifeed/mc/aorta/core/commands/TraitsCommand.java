package msifeed.mc.aorta.core.commands;

import msifeed.mc.aorta.core.props.TraitsAttribute;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.commons.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;

import java.util.Set;
import java.util.stream.Collectors;

public class TraitsCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "trait";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/trait [trait]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityLivingBase)) {
            send(sender, "You should be at least entity!");
            return;
        }

        switch (args.length) {
            case 0:
                printTraits(sender);
                break;
            case 1:
                toggleTrait(sender, args[0]);
                break;
        }
    }

    private void printTraits(ICommandSender sender) {
        final EntityLivingBase entity = (EntityLivingBase) sender;
        TraitsAttribute.INSTANCE.get(entity).ifPresent(traits -> {
            final Set<String> names = traits.stream().map(Enum::toString).collect(Collectors.toSet());
            final String theNiceString = joinNiceStringFromCollection(names);
            send(sender, "Here your traits (%d):", names.size());
            send(sender, "  " + theNiceString);
        });
    }

    private void toggleTrait(ICommandSender sender, String traitName) {
        try {
            final Trait trait = Trait.valueOf(traitName);
            final EntityLivingBase entity = (EntityLivingBase) sender;
            final boolean added = TraitsAttribute.INSTANCE.toggle(entity, trait);
            send(sender, "Trait '%s' %s", traitName, added ? "added" : "removed");
        } catch (IllegalArgumentException e) {
            send(sender, "Unknown trait '%s'", traitName);
        }
    }
}
