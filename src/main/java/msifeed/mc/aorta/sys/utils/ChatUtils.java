package msifeed.mc.aorta.sys.utils;

import java.util.regex.Pattern;

public class ChatUtils {
    private static final Pattern STRIP_FORMATTING_PATTERN = Pattern.compile("(?i)" + "§[0-9A-FK-OR]");

    public static String stripFormatting(String input) {
        return input == null ? null : STRIP_FORMATTING_PATTERN.matcher(input).replaceAll("");
    }
}
