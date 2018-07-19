package msifeed.mc.aorta.core.commands;

import cpw.mods.fml.common.FMLCommonHandler;
import msifeed.mc.aorta.core.props.TraitsProperty;
import msifeed.mc.aorta.core.traits.Trait;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentText;

import java.util.Set;
import java.util.stream.Collectors;

public class TraitsCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "trait";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/trait [trait]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
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
        final TraitsProperty prop = TraitsProperty.get(entity);
        if (prop == null)
            return;

        final Set<String> traits = prop.traits.stream().map(Enum::toString).collect(Collectors.toSet());
        final String theNiceString = joinNiceStringFromCollection(traits);
        send(sender, String.format("Here your traits (%d):", traits.size()));
        send(sender, "  " + theNiceString);
    }

    private void toggleTrait(ICommandSender sender, String traitName) {
        try {
            final Trait trait = Trait.valueOf(traitName);
            final EntityLivingBase entity = (EntityLivingBase) sender;
            final TraitsProperty prop = TraitsProperty.get(entity);
            if (prop == null)
                return;

            final boolean added = prop.toggle(trait);
            if (FMLCommonHandler.instance().getEffectiveSide().isClient())
                prop.syncServer(entity);
            else
                prop.sync(entity.worldObj, entity);

            send(sender, String.format("Trait '%s' %s", traitName, added ? "added" : "removed"));
        } catch (IllegalArgumentException e) {
            send(sender, String.format("Unknown trait '%s'", traitName));
        }
    }

    private void send(ICommandSender sender, String msg) {
        sender.addChatMessage(new ChatComponentText(msg));
    }
}
