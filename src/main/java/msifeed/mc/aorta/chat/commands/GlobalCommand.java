package msifeed.mc.aorta.chat.commands;

import msifeed.mc.aorta.chat.ChatHandler;
import msifeed.mc.aorta.chat.composer.Composer;
import msifeed.mc.aorta.chat.composer.SpeechType;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.sys.cmd.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Collections;
import java.util.List;

public class GlobalCommand extends ExtCommand {
    public static boolean receiveMessages = true;

    @Override
    public String getCommandName() {
        return "global";
    }

    @Override
    public List getCommandAliases() {
        return Collections.singletonList("g");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/global <text>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            receiveMessages = !receiveMessages;
            return;
        }

        if (!receiveMessages)
            return;

        final EntityPlayer player = (EntityPlayer) sender;
        final String text = String.join(" ", args);
        final ChatMessage message = Composer.makeMessage(SpeechType.GLOBAL, player, text);

        if (player instanceof EntityPlayerMP)
            ChatHandler.sendGlobalChatMessage((EntityPlayerMP) player, message);
        else {
            player.addChatMessage(Composer.formatMessage(player, message));
        }
    }
}
