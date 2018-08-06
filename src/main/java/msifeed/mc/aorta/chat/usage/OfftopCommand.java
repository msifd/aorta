package msifeed.mc.aorta.chat.usage;

import msifeed.mc.aorta.chat.ChatHandler;
import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.SpeechFormatter;
import msifeed.mc.aorta.chat.net.SpeechMessage;
import msifeed.mc.commons.ExtCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Arrays;
import java.util.stream.Collectors;

public class OfftopCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "o";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/o <text>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer)) {
            error(sender, "You should be at least player!");
            return;
        }

        if (args.length == 0)
            return;

        final EntityPlayer player = (EntityPlayer) sender;
        final String text = Arrays.stream(args).collect(Collectors.joining(" "));

        final SpeechMessage message = new SpeechMessage();
        message.type = SpeechMessage.Type.OFFTOP;
        message.language = LangAttribute.INSTANCE.get(player).orElse(Language.VANILLA);
        message.radius = 20;
        message.speaker = sender.getCommandSenderName();
        message.text = text;

        if (player instanceof EntityPlayerMP)
            ChatHandler.sendSpeechMessage((EntityPlayerMP) player, message);
        else {
            Minecraft.getMinecraft().thePlayer.addChatMessage(SpeechFormatter.formatSpeech(message));
        }
    }
}
