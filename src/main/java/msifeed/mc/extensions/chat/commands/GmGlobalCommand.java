package msifeed.mc.extensions.chat.commands;

import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.commons.traits.Trait;
import msifeed.mc.extensions.chat.ChatHandler;
import msifeed.mc.extensions.chat.ChatMessage;
import msifeed.mc.extensions.chat.composer.Composer;
import msifeed.mc.extensions.chat.composer.SpeechType;
import msifeed.mc.sys.cmd.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class GmGlobalCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "s";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/s <text>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0)
            return;

        final EntityPlayer player = (EntityPlayer) sender;
        if (!CharacterAttribute.has(player, Trait.gm)) {
            error(sender, "You are not GM!");
            return;
        }

        final String text = String.join(" ", args);
        final ChatMessage message = Composer.makeMessage(SpeechType.GM_GLOBAL, player, text);

        if (player instanceof EntityPlayerMP)
            ChatHandler.sendGlobalChatMessage(player, message);
        else {
            player.addChatMessage(Composer.formatMessage(player, message));
        }
    }
}
