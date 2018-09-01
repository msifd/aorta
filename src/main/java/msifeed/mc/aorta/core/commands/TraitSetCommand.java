package msifeed.mc.aorta.core.commands;

import cpw.mods.fml.common.FMLCommonHandler;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.commons.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import java.util.Set;
import java.util.stream.Collectors;

public class TraitSetCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "trait";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/trait [@<player>] [trait]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityLivingBase)) {
            send(sender, "You should be at least entity!");
            return;
        }

        if (args.length == 0) {
            error(sender, "Usage: " + getCommandUsage(sender));
            return;
        }

        final boolean nameFlag = args[0].startsWith("@");

        if (nameFlag) {
            final String name = args[0].substring(1);
            final EntityLivingBase player = findPlayer(name);
            if (player != null) {
                if (args.length > 1)
                    toggleTrait(sender, player, args[1]);
                else
                    printTraits(sender, player);
            } else {
                error(sender, "Unknown player");
            }
        } else {
            toggleTrait(sender, (EntityLivingBase) sender, args[0]);
        }
    }

    private void printTraits(ICommandSender sender, EntityLivingBase entity) {
        CharacterAttribute.INSTANCE.get(entity).map(Character::traits).ifPresent(traits -> {
            final Set<String> names = traits.stream().map(Enum::toString).collect(Collectors.toSet());
            final String theNiceString = joinNiceStringFromCollection(names);
            title(sender, "Here %s's traits:", entity.getCommandSenderName());
            send(sender, "  " + theNiceString);
        });
    }

    private EntityLivingBase findPlayer(String name) {
        final MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (mcServer == null)
            return null;
        for (WorldServer server : mcServer.worldServers) {
            final EntityLivingBase tmp = server.getPlayerEntityByName(name);
            if (tmp != null)
                return tmp;
        }
        return null;
    }

    private void toggleTrait(ICommandSender sender, EntityLivingBase entity, String traitName) {
        try {
            final Trait trait = Trait.valueOf(traitName);
            final boolean added = CharacterAttribute.INSTANCE.toggle(entity, trait);
            info(sender, "Trait '%s' %s", traitName, added ? "added" : "removed");
        } catch (IllegalArgumentException e) {
            error(sender, "Unknown trait '%s'", traitName);
        }
    }
}
