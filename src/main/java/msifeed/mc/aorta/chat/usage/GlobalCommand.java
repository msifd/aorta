package msifeed.mc.aorta.chat.usage;

import msifeed.mc.aorta.chat.ChatHandler;
import msifeed.mc.aorta.chat.composer.Composer;
import msifeed.mc.aorta.chat.composer.SpeechType;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.commands.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Arrays;
import java.util.stream.Collectors;

public class GlobalCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "global";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/global <text>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0)
            return;

        final EntityPlayer player = (EntityPlayer) sender;
        final String text = Arrays.stream(args).collect(Collectors.joining(" "));
        final ChatMessage message = Composer.makeMessage(SpeechType.GLOBAL, player, text);

        if (player instanceof EntityPlayerMP)
            ChatHandler.sendGlobalChatMessage((EntityPlayerMP) player, message);
        else {
            player.addChatMessage(Composer.formatMessage(player, message));
        }
    }
}
