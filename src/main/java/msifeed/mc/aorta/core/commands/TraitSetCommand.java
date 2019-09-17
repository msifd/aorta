package msifeed.mc.aorta.core.commands;

import cpw.mods.fml.common.FMLCommonHandler;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.core.traits.TraitType;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.sys.cmd.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;

public class TraitSetCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "trait";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/trait <trait> [player]";
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

        final String traitName = args[0].toLowerCase();
        final EntityLivingBase target;

        if (args.length > 1) {
            final EntityLivingBase player = findPlayer(args[1]);
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

        toggleTrait(sender, target, traitName);
    }

    private void toggleTrait(ICommandSender sender, EntityLivingBase entity, String traitName) {
        try {
            final Trait trait = Trait.valueOf(traitName);
            if (!isAllowedToSet(sender, trait)) {
                error(sender, "You are are not allowed to set this trait!");
                return;
            }

            final boolean added = CharacterAttribute.toggle(entity, trait);
            info(sender, "Trait '%s' %s", traitName, added ? "added" : "removed");
        } catch (IllegalArgumentException e) {
            error(sender, "Unknown trait '%s'", traitName);
        }
    }

    private boolean isAllowedToSet(ICommandSender sender, Trait trait) {
        if (FMLCommonHandler.instance().getSide().isClient())
            return true;
        if (trait.type == TraitType.SYSTEM)
            return sender instanceof MinecraftServer
                    || (sender instanceof EntityLivingBase && CharacterAttribute.has((EntityLivingBase) sender, Trait.__admin));
        return CharacterAttribute.has((EntityLivingBase) sender, Trait.gm);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 1;
    }
}
