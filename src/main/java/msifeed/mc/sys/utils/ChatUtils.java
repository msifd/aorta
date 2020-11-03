package msifeed.mc.sys.utils;

import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.regex.Pattern;

public class ChatUtils {
    private static final Pattern STRIP_FORMATTING_PATTERN = Pattern.compile("(?i)" + "§[0-9A-FK-OR]");

    public static String stripFormatting(String input) {
        return input == null ? null : STRIP_FORMATTING_PATTERN.matcher(input).replaceAll("");
    }

    public static String getPrettyName(EntityLivingBase entity) {
        final Character chr = CharacterAttribute.get(entity).orElse(null);
        if (chr != null && !chr.name.isEmpty())
            return chr.name;

        return entity.getCommandSenderName();
    }

    public static String getPrettyName(EntityLivingBase entity, Character chr) {
        if (entity instanceof EntityPlayer)
            return ((EntityPlayer) entity).getDisplayName();

        if (chr != null && !chr.name.isEmpty())
            return chr.name;

        return entity.getCommandSenderName();
    }

    public static String fromAmpersandFormatting(String str) {
        return str.replace('&', '\u00A7');
    }

    public static String intoAmpersandFormatting(String str) {
        return str.replace('\u00A7', '&');
    }
}
