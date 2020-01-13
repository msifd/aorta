package msifeed.mc.extensions.chat.commands;

import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.commons.traits.Trait;
import msifeed.mc.extensions.chat.ChatHandler;
import msifeed.mc.extensions.chat.ChatMessage;
import msifeed.mc.extensions.chat.LangAttribute;
import msifeed.mc.extensions.chat.Language;
import msifeed.mc.extensions.chat.composer.Composer;
import msifeed.mc.extensions.chat.composer.SpeechType;
import msifeed.mc.sys.cmd.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Arrays;

public class SusayCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "susay";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/susay <name> <text>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer)) {
            error(sender, "You should be at least player!");
            return;
        }
        if (!CharacterAttribute.has((EntityPlayer) sender, Trait.gm)) {
            error(sender, "You are not GM!");
            return;
        }

        if (args.length < 2) {
            info(sender, getCommandUsage(sender));
            return;
        }

        final EntityPlayerMP target = getPlayer(sender, args[0]);
        if (target == null)
            return;

        final String[] textParts = Arrays.copyOfRange(args, 1, args.length);
        final String text = String.join(" ", textParts);
        final ChatMessage message = Composer.makeMessage(SpeechType.SPEECH, target, text);
        message.language = LangAttribute.get((EntityPlayer) sender).orElse(Language.VANILLA);
        ChatHandler.sendChatMessage(target, message);
    }
}
