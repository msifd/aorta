package msifeed.mc.extensions.chat.commands;

import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.extensions.chat.GmSpeech;
import msifeed.mc.extensions.chat.SpeechatRpc;
import msifeed.mc.extensions.chat.formatter.MiscFormatter;
import msifeed.mc.sys.cmd.GmExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GmSayCommand extends GmExtCommand {
    private static final String[] setArguments = {"?", "p", "c", "r", "s"};

    @Override
    public String getCommandName() {
        return "gms";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/gms ?";
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        return args.length == 1
                ? getListOfStringsMatchingLastWord(args, setArguments)
                : null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayerMP))
            return;

        final EntityPlayerMP player = (EntityPlayerMP) sender;
        if (args.length == 0) {
            printStatus(player);
            return;
        }

        final GmSpeech.Preferences prefs = GmSpeech.get(player.getCommandSenderName());
        switch (args[0]) {
            case "?":
                printHelp(player);
                break;
            case "s":
                prefs.replaceSpeech = !prefs.replaceSpeech;
                title(player, "GmSay - Replace speech: %b", prefs.replaceSpeech);
                break;
            case "r":
                if (args.length >= 2) {
                    prefs.range = parseIntBounded(player, args[1], 0, 10000);
                    title(player, "GmSay - Range: %d", prefs.range);
                } else {
                    error(player, "Invalid range");
                }
                break;
            case "c":
                if (args.length >= 2) {
                    final EnumChatFormatting color = EnumChatFormatting.getValueByName(args[1]);
                    if (color == null || !color.isColor()) {
                        error(player, "Invalid color name");
                    } else {
                        prefs.color = color;
                        title(player, "GmSay - Color: \u00a7%c%s", prefs.color.getFormattingCode(), prefs.color.getFriendlyName());
                    }
                } else {
                    final String colorNames = Stream.of(EnumChatFormatting.values())
                            .filter(EnumChatFormatting::isColor)
                            .map(e -> "\u00a7" + e.getFormattingCode() + e.getFriendlyName())
                            .collect(Collectors.joining(", "));
                    title(player, "GmSay - Color");
                    info(player, "Color names: " + colorNames);
                }
                break;
            case "p":
                final String input = Arrays.stream(args, 1, args.length).collect(Collectors.joining(" "));
                prefs.prefix = input.replace('&', '\u00a7');
                title(player, "GmSay - Prefix: '\u00a7r%s\u00a79'", prefs.prefix);
                break;
            default:
                final String text = String.join(" ", args);
                SpeechatRpc.sendRaw(player, prefs.range, MiscFormatter.formatGmSay(prefs, new ChatComponentText(text)));
                ExternalLogs.log(player, "gm", text);
                break;
        }
    }

    private void printHelp(EntityPlayer player) {
        title(player, "GmSay - Help");
        title(player, "  /gms - current settings");
        title(player, "  /gms ? - this help");
        title(player, "  /gms <words> - say via gmsay");
        title(player, "  /gms p <words> - set prefix");
        title(player, "  /gms c [color name] - get color names or set color");
        title(player, "  /gms r <number> - set range");
        title(player, "  /gms s <true/false> - set replace speech");
    }

    private void printStatus(EntityPlayer player) {
        final GmSpeech.Preferences pref = GmSpeech.get(player.getCommandSenderName());
        title(player, "GmSay - Settings");
        info(player, "  Replace speech: %b", pref.replaceSpeech);
        info(player, "  Range: %d", pref.range);
        info(player, "  Color: \u00a7%c%s", pref.color.getFormattingCode(), pref.color.getFriendlyName());
        info(player, "  Prefix: \u00a7r%s", pref.prefix);
        info(player, "  \u00a7r%s", makeExample(pref));
    }

    private static String makeExample(GmSpeech.Preferences prefs) {
        final IChatComponent cc = MiscFormatter.formatGmSay(prefs, new ChatComponentText("Such prety example! Wow!"));
        return "Example: " + cc.getFormattedText();
    }
}
