package msifeed.mc.aorta.chat.usage;

import msifeed.mc.aorta.chat.ChatHandler;
import msifeed.mc.aorta.chat.composer.Composer;
import msifeed.mc.aorta.chat.composer.SpeechType;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.commands.ExtCommand;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.traits.Trait;
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
            ChatHandler.sendGlobalChatMessage((EntityPlayerMP) player, message);
        else {
            player.addChatMessage(Composer.formatMessage(player, message));
        }
    }
}
