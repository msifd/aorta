package msifeed.mc.aorta.chat.selection;

import msifeed.mc.aorta.chat.ChatHandler;
import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.SpeechFormatter;
import msifeed.mc.aorta.chat.net.SpeechMessage;
import msifeed.mc.commons.ExtCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.stream.Collectors;

public class OfftopCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "o";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/o <message>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer)) {
            send(sender, "You should be at least player!");
            return;
        }

        if (args.length == 0)
            return;

        final ChatComponentText rootComponent = new ChatComponentText("");
        rootComponent.getChatStyle().setColor(EnumChatFormatting.GRAY);

        final EntityPlayer player = (EntityPlayer) sender;
        final String text = Arrays.stream(args).collect(Collectors.joining(" "));
        rootComponent.appendText(text);

        final SpeechMessage message = new SpeechMessage();
        message.type = SpeechMessage.Type.OFFTOP;
        message.language = LangAttribute.INSTANCE.get(player).orElse(Language.VANILLA);
        message.radius = 20;
        message.speaker = sender.getCommandSenderName();
        message.chatComponent = rootComponent;

        if (player instanceof EntityPlayerMP)
            ChatHandler.sendSpeechMessage((EntityPlayerMP) player, message);
        else {
            SpeechFormatter.formatSpeech(message);
            Minecraft.getMinecraft().thePlayer.addChatMessage(message.chatComponent);
        }
    }
}
