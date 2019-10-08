package msifeed.mc.aorta.chat.commands;

import msifeed.mc.aorta.chat.ChatHandler;
import msifeed.mc.aorta.chat.composer.Composer;
import msifeed.mc.aorta.chat.composer.SpeechType;
import msifeed.mc.aorta.chat.gm.GmSpeech;
import msifeed.mc.aorta.chat.gm.GmsaySettings;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.logs.Logs;
import msifeed.mc.aorta.sys.cmd.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Arrays;
import java.util.stream.Collectors;

public class GmsayCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "gms";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/gms ?";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer)) {
            send(sender, "Can be used only by a player!");
            return;
        }
        final EntityPlayer player = (EntityPlayer) sender;

        if (!CharacterAttribute.has(player, Trait.gm)) {
            error(sender, "You are not GM!");
            return;
        }

        if (args.length == 0) {
            printStatus(player);
            return;
        }

        if (args.length == 1 && args[0].equals("?")) {
            printHelp(player);
            return;
        }

        if (args.length > 1) {
            final GmsaySettings settings = GmSpeech.get(player.getCommandSenderName());
            switch (args[0]) {
                case "s":
                    settings.replaceSpeech = parseBoolean(player, args[1]);
                    title(player, "GmSay - Replace speech: %b", settings.replaceSpeech);
                    return;
                case "r":
                    settings.radius = parseIntBounded(player, args[1], 0, 10000);
                    title(player, "GmSay - Radius: %d", settings.radius);
                    return;
                case "p":
                    final String input = Arrays.stream(args, 1, args.length).collect(Collectors.joining(" "));
                    settings.prefix = input.replace('&', '\u00a7');
                    title(player, "GmSay - Prefix: '\u00a7r%s\u00a79'", settings.prefix);
                    return;
            }
        }

        final String text = String.join(" ", args);
        final ChatMessage message = Composer.makeMessage(SpeechType.GM, player, text);

        if (player instanceof EntityPlayerMP)
            ChatHandler.sendSystemChatMessage(player, "gms", message);
        else
            player.addChatMessage(Composer.formatMessage(player, message));
    }

    private void printHelp(EntityPlayer player) {
        title(player, "GmSay - Help");
        title(player, "  /gms - current settings");
        title(player, "  /gms ? - this help");
        title(player, "  /gms <words> - say via gmsay");
        title(player, "  /gms s <true/false> - set replace speech");
        title(player, "  /gms r <number> - set radius");
        title(player, "  /gms p <words> - set prefix");
    }

    private void printStatus(EntityPlayer player) {
        final GmsaySettings settings = GmSpeech.get(player.getCommandSenderName());
        title(player, "GmSay - Settings");
        info(player, "  Replace speech: %b", settings.replaceSpeech);
        info(player, "  Radius: %d", settings.radius);
        info(player, "  Prefix: '\u00a7r%s\u00a7r'", settings.prefix);
    }
}
